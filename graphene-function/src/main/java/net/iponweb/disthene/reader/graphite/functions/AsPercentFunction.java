package net.iponweb.disthene.reader.graphite.functions;

import net.iponweb.disthene.reader.beans.TimeSeries;
import net.iponweb.disthene.reader.exceptions.EvaluationException;
import net.iponweb.disthene.reader.exceptions.InvalidArgumentException;
import net.iponweb.disthene.reader.graphite.Target;
import net.iponweb.disthene.reader.graphite.evaluation.TargetEvaluator;
import org.apache.log4j.Logger;

import java.util.*;

/**
 * @author Andrei Ivanov
 * @author jerome89
 */
public class AsPercentFunction extends DistheneFunction {

    private boolean isTotalPresent = false;
    private boolean isTotalSeries = false;

    public AsPercentFunction(String text) {
        super(text, "asPercent");
    }

    private final static Logger logger = Logger.getLogger(AsPercentFunction.class);

    @Override
    public List<TimeSeries> evaluate(TargetEvaluator evaluator) throws EvaluationException {
        List<TimeSeries> processedArguments = new ArrayList<>(evaluator.eval((Target) arguments.get(0)));

        if (processedArguments.size() == 0) return new ArrayList<>();

        setParameters();

        double[] total = new double[processedArguments.get(0).getValues().length];

        if (! isTotalPresent) {
            setTotalFromSeries(processedArguments, total);
        }

        if (isTotalPresent && isTotalSeries) {
            List<TimeSeries> totalSeriesList = new ArrayList<>(evaluator.eval((Target) arguments.get(1)));
            if (totalSeriesList.size() == 0) {
                return new ArrayList<>();
            }
            setTotalFromSeries(totalSeriesList, total);
        }

        if (isTotalPresent && ! isTotalSeries) {
            Arrays.fill(total, (Double) arguments.get(1));
        }

        return compute(processedArguments, total);
    }

    @Override
    public List<TimeSeries> computeDirectly(List<TimeSeries> timeSeriesList) {
        if (timeSeriesList.size() != 2) {
            logger.warn("AsPercent: computeDirectly method needs to take 2 series to calculate result properly.");
            return new ArrayList<>();
        }

        TimeSeries dividend = timeSeriesList.get(0);
        double[] total = new double[dividend.getValues().length];
        setTotalFromSeries(Collections.singletonList(timeSeriesList.get(1)), total);

        return compute(Collections.singletonList(timeSeriesList.get(0)), total);
    }

    private List<TimeSeries> compute(List<TimeSeries> timeSeriesList, double[] total) {
        for (TimeSeries ts : timeSeriesList) {
            for (int i = 0; i < total.length; i++) {
                if (null != ts.getValues()[i]) {
                    if (total[i] != 0) {
                        ts.getValues()[i] = (ts.getValues()[i] / total[i]) * 100;
                    } else {
                        ts.getValues()[i] = null;
                    }
                }
            }

            if (arguments.size() > 1 && (arguments.get(1) instanceof Target)) {
                ts.setName("asPercent(" + ts.getName() + "," + ((Target) arguments.get(1)).getText() + ")");
            } else if (arguments.size() > 1 && (arguments.get(1) instanceof Double)) {
                ts.setName("asPercent(" + ts.getName() + "," + arguments.get(1) + ")");
            } else {
                setResultingName(ts);
            }
        }
        return timeSeriesList;
    }

    private void setTotalFromSeries(List<TimeSeries> totalSeriesList, double[] total) {
        for (int i = 0; i < total.length; i++) {
            total[i] = 0;
            for (TimeSeries ts : totalSeriesList) {
                if (null != ts.getValues()[i]) {
                    total[i] += ts.getValues()[i];
                }
            }
        }
    }

    private void setParameters() {
        if (arguments.size() > 1) {
            isTotalPresent = true;
        }

        if (arguments.get(1) instanceof Target) {
            isTotalSeries = true;
        }
    }

    @Override
    public void checkArguments() throws InvalidArgumentException {
        check(arguments.size() > 0,
            "asPercent: number of arguments is " + arguments.size() + ". Must be at least one.");

        Optional<Object> argSeries = Optional.ofNullable(arguments.get(0));
        check(argSeries.orElse(null) instanceof Target,
            "asPercent: First argument is  " +
            getClassName(argSeries.orElse(null)) + ". Must be a series.");

        Optional<Object> argTotal = Optional.ofNullable(arguments.get(1));
        check(argTotal.orElse(null) instanceof Target || argTotal.orElse(null) instanceof Double,
            "asPercent: Second argument is " +
            getClassName(argTotal.orElse(null)) + ". Must be series or number.");

        for (int i = 2; i < arguments.size(); i++) {
            Optional<Object> argNode = Optional.ofNullable(arguments.get(i));
            check(argNode.orElse(null) instanceof Double,
                "asPercent: argument is " + getClassName(argNode.orElse(null)) + ". Must be a number.");
        }
    }
}
