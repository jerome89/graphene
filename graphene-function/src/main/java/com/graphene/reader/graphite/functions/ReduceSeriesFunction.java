package com.graphene.reader.graphite.functions;

import com.graphene.reader.beans.TimeSeries;
import com.graphene.reader.exceptions.EvaluationException;
import com.graphene.reader.exceptions.InvalidArgumentException;
import com.graphene.reader.exceptions.InvalidFunctionException;
import com.graphene.reader.exceptions.UnsupportedMethodException;
import com.graphene.reader.graphite.Target;
import com.graphene.reader.graphite.evaluation.TargetEvaluator;
import com.graphene.reader.utils.TimeSeriesUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.regex.Pattern;

import static com.graphene.reader.graphite.functions.registry.FunctionRegistry.getFunction;

/**
 * @author jerome89
 * @author dark
 */
public class ReduceSeriesFunction extends GrapheneFunction {

    public ReduceSeriesFunction(String text) {
        super(text, "reduceSeries");
    }

    private static List<String> allowedFunctions =
        Arrays.asList("sumSeries", "averageSeries", "diffSeries", "asPercent", "divideSeries");

    @Override
    public List<TimeSeries> evaluate(TargetEvaluator evaluator) throws EvaluationException {
        List<List<TimeSeries>> processedArguments = new ArrayList<>(evaluator.evalByGroup((Target) arguments.get(0)));

        String functionName = (String) arguments.get(1);
        GrapheneFunction functionToReduce;
        try {
            functionToReduce = getFunction(getContext(), functionName, tenant, from, to);
        } catch (InvalidFunctionException e) {
            throw new EvaluationException(e);
        }

        int argReduceNode = ((Double) arguments.get(2)).intValue();

        List<Pattern> patterns = new ArrayList<>(arguments.size() - 3);

        for (int i = 3; i < arguments.size(); i++) {
            patterns.add(Pattern.compile((String) arguments.get(i)));
        }

        return reduce(processedArguments, functionToReduce, argReduceNode, patterns);
    }

    private List<TimeSeries> reduce(List<List<TimeSeries>> processedArguments,
                                    GrapheneFunction functionToReduce,
                                    int argReduceNode,
                                    List<Pattern> patterns) throws EvaluationException {
        List<TimeSeries> resultTimeSeriesList = new ArrayList<>(processedArguments.size());

        for (List<TimeSeries> timeSeriesList : processedArguments) {
            List<TimeSeries> seriesListToCompute = new ArrayList<>();
            int reduceNode = argReduceNode;

            if (reduceNode < 0) {
                reduceNode += TimeSeriesUtils.DOT_PATTERN.split(timeSeriesList.get(0).getName()).length;
            }

            for (TimeSeries ts : timeSeriesList) {
                String[] nodes = TimeSeriesUtils.DOT_PATTERN.split(ts.getName());
                for (Pattern pattern : patterns) {
                    if (pattern.matcher(nodes[reduceNode]).matches()) {
                        seriesListToCompute.add(ts);
                    }
                }
            }

            try {
                resultTimeSeriesList.addAll(functionToReduce.computeDirectly(seriesListToCompute));
            } catch (UnsupportedMethodException e) {
                throw new EvaluationException(e);
            }
        }

        for (TimeSeries ts : resultTimeSeriesList) {
            ts.setName(getText());
        }
        return resultTimeSeriesList;
    }


    @Override
    public void checkArguments() throws InvalidArgumentException {
        check(arguments.size() >= 4,
            "reduceSeries: number of arguments is " +
                arguments.size() + ". Must be at least five.");

        Optional<Object> argSeries = Optional.ofNullable(arguments.get(0));
        check(argSeries.orElse(null) instanceof Target,
            "reduceSeries: First argument is " +
                getClassName(argSeries.orElse(null)) + ". Must be series.");

        Optional<Object> argFunctionName = Optional.ofNullable(arguments.get(1));
        check(argFunctionName.orElse(null) instanceof String,
            "reduceSeries: Second argument is " +
            getClassName(argFunctionName.orElse(null)) + ". Must be String.");

        String functionName = (String) argFunctionName.get();
        check(allowedFunctions.contains(functionName),
            "reduceSeries: function " +
                functionName + " is not allowed to be used with reduceSeries.");

        Optional<Object> argReduceNode = Optional.ofNullable(arguments.get(2));
        check(argReduceNode.orElse(null) instanceof Double,
        "reduceSeries: Third argument is " +
            getClassName(argReduceNode.orElse(null)) + ". Must be a Number.");

        for (int i = 3; i < arguments.size(); i++) {
            Optional<Object> argReduceMatcher = Optional.ofNullable(arguments.get(i));
            check(argReduceMatcher.orElse(null) instanceof String,
                "reduceSeries: " + (i + 1) + "-th argument is " +
                getClassName(argReduceMatcher.orElse(null)) + ". Must be String.");
        }
    }
}
