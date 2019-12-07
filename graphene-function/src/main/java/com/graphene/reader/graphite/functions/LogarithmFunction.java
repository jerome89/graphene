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
public class LogarithmFunction extends GrapheneFunction {

    public LogarithmFunction(String text) {
        super(text, "logarithm");
    }

    @Override
    public List<TimeSeries> evaluate(TargetEvaluator evaluator) throws EvaluationException {
        List<TimeSeries> processedArguments = new ArrayList<>();
        processedArguments.addAll(evaluator.eval((Target) arguments.get(0)));

        if (processedArguments.size() == 0) return new ArrayList<>();

        if (!TimeSeriesUtils.checkAlignment(processedArguments)) {
            throw new TimeSeriesNotAlignedException();
        }

        double base = arguments.size() > 1 ? (Double) arguments.get(1) : 10.;
        double logBase = Math.log(base);


        int length = processedArguments.get(0).getValues().length;

        for (TimeSeries ts : processedArguments) {
            for (int i = 0; i < length; i++) {
                if (ts.getValues()[i] != null && ts.getValues()[i] > 0 && logBase != 0) {
                    ts.getValues()[i] = Math.log(ts.getValues()[i]) / logBase;
                } else {
                    ts.getValues()[i] = null;
                }
            }

            ts.setName("logarithm(" + ts.getName() + "," + base + ")");
        }

        return processedArguments;
    }

    @Override
    public void checkArguments() throws InvalidArgumentException {
        if (arguments.size() > 2 || arguments.size() == 0) throw new InvalidArgumentException("logarithm: number of arguments is " + arguments.size() + ". Must be one or two.");
        if (!(arguments.get(0) instanceof Target)) throw new InvalidArgumentException("logarithm: argument is " + arguments.get(0).getClass().getName() + ". Must be series");
        if (arguments.size() > 1 && !(arguments.get(1) instanceof Double)) throw new InvalidArgumentException("logarithm: argument is " + arguments.get(1).getClass().getName() + ". Must be a number");
        if (arguments.size() > 1 && ((Double) arguments.get(1) <= 0)) throw new InvalidArgumentException("logarithm: base is " + arguments.get(1).getClass().getName() + ". Must be > 0.");
    }
}
