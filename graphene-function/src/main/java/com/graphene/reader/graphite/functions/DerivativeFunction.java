package com.graphene.reader.graphite.functions;

import com.graphene.reader.exceptions.EvaluationException;
import com.graphene.reader.exceptions.InvalidArgumentException;
import com.graphene.reader.exceptions.TimeSeriesNotAlignedException;
import com.graphene.reader.graphite.Target;
import com.graphene.reader.graphite.evaluation.TargetEvaluator;
import com.graphene.reader.beans.TimeSeries;
import com.graphene.reader.utils.TimeSeriesUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Andrei Ivanov
 */
public class DerivativeFunction extends GrapheneFunction {


    public DerivativeFunction(String text) {
        super(text, "derivative");
    }

    @Override
    public List<TimeSeries> evaluate(TargetEvaluator evaluator) throws EvaluationException {
        List<TimeSeries> processedArguments = new ArrayList<>();
        processedArguments.addAll(evaluator.eval((Target) arguments.get(0)));

        if (processedArguments.size() == 0) return new ArrayList<>();

        if (!TimeSeriesUtils.checkAlignment(processedArguments)) {
            throw new TimeSeriesNotAlignedException();
        }

        int length = processedArguments.get(0).getValues().length;

        for(TimeSeries ts : processedArguments) {
            Double[] values = new Double[length];
            Double previous = null;
            for (int i = 0; i < length; i++) {
                if (previous != null && ts.getValues()[i] != null) {
                    values[i] = ts.getValues()[i] - previous;
                }

                previous = ts.getValues()[i];
            }

            ts.setValues(values);
            setResultingName(ts);
        }

        return processedArguments;
    }

    @Override
    public void checkArguments() throws InvalidArgumentException {
        if (arguments.size() != 1) throw new InvalidArgumentException("derivative: number of arguments is " + arguments.size() + ". Must be exactly one.");
        if (!(arguments.get(0) instanceof Target)) throw new InvalidArgumentException("derivative: argument is " + arguments.get(0).getClass().getName() + ". Must be series");
    }
}
