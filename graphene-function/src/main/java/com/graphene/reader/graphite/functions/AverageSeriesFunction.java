package com.graphene.reader.graphite.functions;

import com.graphene.reader.beans.TimeSeries;
import com.graphene.reader.exceptions.EvaluationException;
import com.graphene.reader.exceptions.InvalidArgumentException;
import com.graphene.reader.graphite.Target;
import com.graphene.reader.graphite.evaluation.TargetEvaluator;
import com.graphene.reader.utils.CollectionUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

/**
 * @author Andrei Ivanov
 * @author jerome89
 */
public class AverageSeriesFunction extends GrapheneFunction {


    public AverageSeriesFunction(String text) {
        super(text, "averageSeries");
    }

    @Override
    public List<TimeSeries> evaluate(TargetEvaluator evaluator) throws EvaluationException {
        List<TimeSeries> processedArguments = new ArrayList<>();
        for (Object argument : arguments) {
            processedArguments.addAll(evaluator.eval((Target) argument));
        }

        if (processedArguments.size() == 0) return new ArrayList<>();

        return compute(processedArguments);
    }

    private List<TimeSeries> compute(List<TimeSeries> timeSeriesList) {
        TimeSeries resultTimeSeries = createResultTimeSeries(timeSeriesList.get(0));
        for (int i = 0; i < resultTimeSeries.getValues().length; i++) {
            List<Double> pointsToAvg = new ArrayList<>(timeSeriesList.size());
            for (TimeSeries ts : timeSeriesList) {
                pointsToAvg.add(ts.getValues()[i]);
            }
            resultTimeSeries.getValues()[i] = CollectionUtils.average(pointsToAvg);
        }
        return Collections.singletonList(resultTimeSeries);
    }

    private TimeSeries createResultTimeSeries(TimeSeries representative) {
        int step = representative.getStep();
        int length = representative.getValues().length;
        TimeSeries resultTimeSeries = new TimeSeries(getText(), from, to, step);
        resultTimeSeries.setValues(new Double[length]);
        return resultTimeSeries;
    }

    @Override
    public List<TimeSeries> computeDirectly(List<TimeSeries> timeSeriesList) {
        return compute(timeSeriesList);
    }

    @Override
    public void checkArguments() throws InvalidArgumentException {
        check(arguments.size() >= 1,
            "averageSeries: number of arguments is " +
                arguments.size() + ". Must be at least one.");

        for (Object argument : arguments) {
            Object argSeries = Optional.ofNullable(argument).orElse(null);
            check(argSeries instanceof Target,
                "averageSeries: argument is " +
                    getClassName(argSeries) + ". Must be series");
        }
    }
}
