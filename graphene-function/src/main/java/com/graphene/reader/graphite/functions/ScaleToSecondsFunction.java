package com.graphene.reader.graphite.functions;

import com.graphene.reader.beans.TimeSeries;
import com.graphene.reader.exceptions.EvaluationException;
import com.graphene.reader.exceptions.InvalidArgumentException;
import com.graphene.reader.exceptions.TimeSeriesNotAlignedException;
import com.graphene.reader.graphite.Target;
import com.graphene.reader.graphite.evaluation.TargetEvaluator;
import com.graphene.reader.utils.TimeSeriesUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Andrei Ivanov
 */
public class ScaleToSecondsFunction extends GrapheneFunction {

    public ScaleToSecondsFunction(String text) {
        super(text, "scaleToSeconds");
    }

    @Override
    public List<TimeSeries> evaluate(TargetEvaluator evaluator) throws EvaluationException {
        List<TimeSeries> processedArguments = new ArrayList<>();
        processedArguments.addAll(evaluator.eval((Target) arguments.get(0)));

        if (processedArguments.size() == 0) return new ArrayList<>();

        if (!TimeSeriesUtils.checkAlignment(processedArguments)) {
            throw new TimeSeriesNotAlignedException();
        }

        double numberOfSeconds = (Double) arguments.get(1);

        int length = processedArguments.get(0).getValues().length;

        for (TimeSeries ts : processedArguments) {
            double scaleFactor = numberOfSeconds / ts.getStep();

            for (int i = 0; i < length; i++) {
                if (ts.getValues()[i] != null) {
                    ts.getValues()[i] *= scaleFactor;
                }
            }
            ts.setName("scaleToSeconds(" + ts.getName() + "," + numberOfSeconds + ")");
        }

        return processedArguments;
    }

    @Override
    public void checkArguments() throws InvalidArgumentException {
        if (arguments.size() > 2 || arguments.size() < 1) throw new InvalidArgumentException("scaleToSeconds: number of arguments is " + arguments.size() + ". Must be two.");
        if (!(arguments.get(0) instanceof Target)) throw new InvalidArgumentException("scaleToSeconds: argument is " + arguments.get(0).getClass().getName() + ". Must be series");
        if (!(arguments.get(1) instanceof Double)) throw new InvalidArgumentException("scaleToSeconds: argument is " + arguments.get(1).getClass().getName() + ". Must be a number");
    }
}
