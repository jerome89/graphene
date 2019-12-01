package net.iponweb.disthene.reader.handler;

import com.google.common.base.Stopwatch;
import com.google.common.util.concurrent.SimpleTimeLimiter;
import com.google.common.util.concurrent.TimeLimiter;
import com.google.common.util.concurrent.UncheckedTimeoutException;
import com.graphene.reader.handler.RenderParameter;
import com.graphene.reader.store.key.ElasticsearchKeySearchHandler;
import net.iponweb.disthene.reader.beans.TimeSeries;
import net.iponweb.disthene.reader.config.ReaderConfiguration;
import net.iponweb.disthene.reader.exceptions.EvaluationException;
import net.iponweb.disthene.reader.exceptions.InvalidParameterValueException;
import net.iponweb.disthene.reader.exceptions.LogarithmicScaleNotAllowed;
import net.iponweb.disthene.reader.exceptions.ParameterParsingException;
import net.iponweb.disthene.reader.format.ResponseFormatter;
import net.iponweb.disthene.reader.graphite.Target;
import net.iponweb.disthene.reader.graphite.evaluation.EvaluationContext;
import net.iponweb.disthene.reader.graphite.evaluation.TargetEvaluator;
import net.iponweb.disthene.reader.graphite.evaluation.TargetVisitor;
import net.iponweb.disthene.reader.graphite.grammar.GraphiteLexer;
import net.iponweb.disthene.reader.graphite.grammar.GraphiteParser;
import net.iponweb.disthene.reader.graphite.utils.ValueFormatter;
import net.iponweb.disthene.reader.service.metric.CassandraMetricService;
import net.iponweb.disthene.reader.service.stats.StatsService;
import net.iponweb.disthene.reader.service.throttling.ThrottlingService;
import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.misc.ParseCancellationException;
import org.antlr.v4.runtime.tree.ParseTree;
import org.apache.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

/**
 * @author Andrei Ivanov
 */
@Component
public class RenderHandler {

    final static Logger logger = Logger.getLogger(RenderHandler.class);

    private TargetEvaluator evaluator;
    private StatsService statsService;
    private ThrottlingService throttlingService;
    private ReaderConfiguration readerConfiguration;

    private static final ExecutorService executor = Executors.newCachedThreadPool();
    private TimeLimiter timeLimiter = SimpleTimeLimiter.create(executor);


    public RenderHandler(CassandraMetricService cassandraMetricService, ElasticsearchKeySearchHandler elasticsearchKeySearchHandler, StatsService statsService, ThrottlingService throttlingService, ReaderConfiguration readerConfiguration) {
        this.evaluator = new TargetEvaluator(cassandraMetricService, elasticsearchKeySearchHandler);
        this.statsService = statsService;
        this.throttlingService = throttlingService;
        this.readerConfiguration = readerConfiguration;
    }

    public ResponseEntity<?> handle(RenderParameter parameters) throws ParameterParsingException, ExecutionException, InterruptedException, EvaluationException, LogarithmicScaleNotAllowed {
        logger.debug("Redner Got request: " + parameters + " / parameters: " + parameters.toString());
        Stopwatch timer = Stopwatch.createStarted();

        double throttled = throttlingService.throttle(parameters.getTenant());

        statsService.incRenderRequests(parameters.getTenant());

        if (throttled > 0) {
            statsService.incThrottleTime(parameters.getTenant(), throttled);
        }

        final List<Target> targets = new ArrayList<>();

        EvaluationContext context = new EvaluationContext(
                readerConfiguration.isHumanReadableNumbers() ? ValueFormatter.getInstance(parameters.getFormat()) : ValueFormatter.getInstance(ValueFormatter.ValueFormatterType.MACHINE)
        );

        // Let's parse the targets
        for(String targetString : parameters.getTargets()) {
            GraphiteLexer lexer = new GraphiteLexer(new ANTLRInputStream(targetString));
            CommonTokenStream tokens = new CommonTokenStream(lexer);
            GraphiteParser parser = new GraphiteParser(tokens);
            ParseTree tree = parser.expression();
            try {
                targets.add(new TargetVisitor(parameters.getTenant(), parameters.getFrom(), parameters.getUntil(), context).visit(tree));
            } catch (ParseCancellationException e) {
                String additionalInfo = null;
                if (e.getMessage() != null) additionalInfo = e.getMessage();
                if (e.getCause() != null) additionalInfo = e.getCause().getMessage();
                throw new InvalidParameterValueException("Could not parse target: " + targetString + " (" + additionalInfo + ")");
            }
        }

        logger.info("targets : " + targets);
        ResponseEntity<?> response;
        try {
            response = timeLimiter.callWithTimeout(new Callable<ResponseEntity>() {
                @Override
                public ResponseEntity<?> call() throws EvaluationException, LogarithmicScaleNotAllowed {
                    return handleInternal(targets, parameters);
                }
            }, readerConfiguration.getRequestTimeout(), TimeUnit.SECONDS);
        } catch (TimeoutException e) {
            logger.debug("Request timed out: " + parameters);
            statsService.incTimedOutRequests(parameters.getTenant());
            response = ResponseEntity.status(HttpStatus.REQUEST_ENTITY_TOO_LARGE).build();
        } catch (Exception e) {
            logger.error(e);
            response = ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }

        timer.stop();
        logger.debug("Request took " + timer.elapsed(TimeUnit.MILLISECONDS) + " milliseconds (" + parameters + ")");

        logger.info("Response status : " + response.getStatusCode());
        return response;
    }
    private ResponseEntity<?> handleInternal(List<Target> targets, RenderParameter parameters) throws EvaluationException, LogarithmicScaleNotAllowed {
        // now evaluate each target producing list of TimeSeries
        List<TimeSeries> results = new ArrayList<>();

        for(Target target : targets) {
            List<TimeSeries> eval = evaluator.eval(target);
            results.addAll(eval);
        }

        return ResponseFormatter.formatResponse(results, parameters);
    }
}
