package net.iponweb.disthene.reader.graphite.functions;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import net.iponweb.disthene.reader.beans.TimeSeries;
import net.iponweb.disthene.reader.exceptions.EvaluationException;
import net.iponweb.disthene.reader.exceptions.InvalidArgumentException;
import net.iponweb.disthene.reader.exceptions.UnsupportedMethodException;
import net.iponweb.disthene.reader.graphite.Target;
import net.iponweb.disthene.reader.graphite.evaluation.TargetEvaluator;
import net.iponweb.disthene.reader.utils.TimeSeriesUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class MapSeriesFunction extends DistheneFunction {

    public MapSeriesFunction(String text) {
        super(text, "mapSeries");
    }

    @Override
    public List<TimeSeries> evaluate(TargetEvaluator evaluator) throws EvaluationException {
        throw new EvaluationException(new UnsupportedMethodException("Not allowed to call evaluate method in MapSeriesFunction."));
    }

    // We need to consider the case when TAG is supported.
    @Override
    public List<List<TimeSeries>> evalByGroup(TargetEvaluator evaluator) throws EvaluationException {
        List<TimeSeries> processedArguments = new ArrayList<>(evaluator.eval((Target) arguments.get(0)));

        int[] mapNodes = new int[arguments.size() - 1];
        for (int i = 1; i < arguments.size(); i++) {
            mapNodes[i] = (int) arguments.get(i);
        }

        List<List<TimeSeries>> result = Lists.newArrayList();
        Map<String, List<TimeSeries>> mappedTimeSeriesList = Maps.newHashMap();

        for (int node : mapNodes) {
            for (TimeSeries ts : processedArguments) {
                String[] nameSplitNodes = TimeSeriesUtils.DOT_PATTERN.split(ts.getName());
                String mappingKey;

                if (node < 0) {
                    mappingKey = nameSplitNodes[node + nameSplitNodes.length];
                } else {
                    mappingKey = nameSplitNodes[node];
                }

                if (mappedTimeSeriesList.containsKey(mappingKey)) {
                    mappedTimeSeriesList.get(mappingKey).add(ts);
                } else {
                    mappedTimeSeriesList.put(mappingKey, Lists.newArrayList(ts));
                }
            }
        }

        for (Map.Entry<String, List<TimeSeries>> entry : mappedTimeSeriesList.entrySet()) {
            result.add(entry.getValue());
        }

        return result;
    }


    @Override
    public void checkArguments() throws InvalidArgumentException {
        check(arguments.size() >= 2,
            "mapSeries: At least 2 arguments are required.");

        Optional<Object> argSeries = Optional.ofNullable(arguments.get(0));
        check(argSeries.orElse(null) instanceof Target,
            "mapSeries: First argument is " +
                getClassName(argSeries.orElse(null)) + ". Must be a Series.");

        for (int i = 1; i < arguments.size(); i++) {
            Optional<Object> argMatchNode = Optional.ofNullable(arguments.get(i));
            Object arg = argMatchNode.orElse(null);
            check(arg instanceof Integer || arg instanceof String,
                "mapSeries: Argument is " +
                getClassName(arg) + ". Must be a number or String.");
        }
    }
}
