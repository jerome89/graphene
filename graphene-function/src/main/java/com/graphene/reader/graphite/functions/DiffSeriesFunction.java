package com.graphene.reader.graphite.functions;

import com.graphene.reader.beans.TimeSeries;
import com.graphene.reader.exceptions.EvaluationException;
import com.graphene.reader.exceptions.InvalidArgumentException;
import com.graphene.reader.graphite.Target;
import com.graphene.reader.graphite.evaluation.TargetEvaluator;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

/**
 * @author Andrei Ivanov
 * @author jerome89
 */
public class DiffSeriesFunction extends GrapheneFunction {


    public DiffSeriesFunction(String text) {
        super(text, "diffSeries");
    }

    @Override
    public List<TimeSeries> evaluate(TargetEvaluator evaluator) throws EvaluationException {
        List<TimeSeries> processedArguments = new ArrayList<>();
        for(Object target : arguments) {
            processedArguments.addAll(evaluator.eval((Target) target));
        }

        if (processedArguments.size() <= 1) return processedArguments;

        return compute(processedArguments);
    }

    private List<TimeSeries> compute(List<TimeSeries> timeSeriesList) {
        TimeSeries resultTimeSeries = timeSeriesList.get(0);
        for (int i = 1; i < timeSeriesList.size(); i++) {
            for (int j = 0; j < resultTimeSeries.getValues().length; j++) {
                if (null != resultTimeSeries.getValues()[j] && null != timeSeriesList.get(i).getValues()[j]) {
                    resultTimeSeries.getValues()[j] -= timeSeriesList.get(i).getValues()[j];
                }
            }
        }
        resultTimeSeries.setName(getText());
        return Collections.singletonList(resultTimeSeries);
    }

    @Override
    public List<TimeSeries> computeDirectly(List<TimeSeries> timeSeriesList) {
        return compute(timeSeriesList);
    }

    @Override
    public void checkArguments() throws InvalidArgumentException {
        check(arguments.size() >= 2,
            "diffSeries: number of arguments is " +
                arguments.size() + ". Must be at least 2.");

        for (Object argument : arguments) {
            Object argSeries = Optional.ofNullable(argument).orElse(null);
            check(argSeries instanceof Target,
                "diffSeries: argument is " +
                    getClassName(argSeries) + ". Must be series");
        }
    }
}
