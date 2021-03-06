package com.graphene.reader.graphite.functions;

import com.graphene.reader.beans.TimeSeries;
import com.graphene.reader.exceptions.EvaluationException;
import com.graphene.reader.exceptions.InvalidArgumentException;
import com.graphene.reader.exceptions.TimeSeriesNotAlignedException;
import com.graphene.reader.graphite.Target;
import com.graphene.reader.graphite.evaluation.TargetEvaluator;
import com.graphene.reader.utils.CollectionUtils;
import com.graphene.reader.utils.TimeSeriesUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author Andrei Ivanov
 */
public class MaximumAboveFunction extends GrapheneFunction {


    public MaximumAboveFunction(String text) {
        super(text, "maximumAbove");
    }

    @Override
    public List<TimeSeries> evaluate(TargetEvaluator evaluator) throws EvaluationException {
        List<TimeSeries> processedArguments = new ArrayList<>();
        processedArguments.addAll(evaluator.eval((Target) arguments.get(0)));

        if (processedArguments.size() == 0) return new ArrayList<>();

        if (!TimeSeriesUtils.checkAlignment(processedArguments)) {
            throw new TimeSeriesNotAlignedException();
        }

        double threshold = (Double) arguments.get(1);

        List<TimeSeries> result = new ArrayList<>();

        for(TimeSeries ts : processedArguments) {
            Double max = CollectionUtils.max(Arrays.asList(ts.getValues()));
            if (max != null && max > threshold) {
                result.add(ts);
            }
        }

        return result;
    }

    @Override
    public void checkArguments() throws InvalidArgumentException {
        if (arguments.size() != 2) throw new InvalidArgumentException("maximumAbove: number of arguments is " + arguments.size() + ". Must be one.");
        if (!(arguments.get(0) instanceof Target)) throw new InvalidArgumentException("maximumAbove: argument is " + arguments.get(0).getClass().getName() + ". Must be series");
        if (!(arguments.get(1) instanceof Double)) throw new InvalidArgumentException("maximumAbove: argument is " + arguments.get(1).getClass().getName() + ". Must be a number");
    }
}
