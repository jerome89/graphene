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
import java.util.regex.Pattern;

/**
 * @author Andrei Ivanov
 */
public class AliasSubFunction extends GrapheneFunction {

    public AliasSubFunction(String text) {
        super(text, "aliasSub");
    }

    @Override
    public List<TimeSeries> evaluate(TargetEvaluator evaluator) throws EvaluationException {
        List<TimeSeries> processedArguments = new ArrayList<>();
        processedArguments.addAll(evaluator.eval((Target) arguments.get(0)));

        if (processedArguments.size() == 0) return new ArrayList<>();

        if (!TimeSeriesUtils.checkAlignment(processedArguments)) {
            throw new TimeSeriesNotAlignedException();
        }

        String regex = (String) arguments.get(1);
        String replacement = ((String) arguments.get(2)).replaceAll("\\\\", "\\$");

        Pattern pattern = Pattern.compile(regex);

        for (TimeSeries ts : processedArguments) {
            ts.setName(ts.getName().replaceAll(regex, replacement));
        }

        return processedArguments;
    }

    @Override
    public void checkArguments() throws InvalidArgumentException {
        if (arguments.size() != 3) throw new InvalidArgumentException("aliasSub: number of arguments is " + arguments.size() + ". Must be three.");
        if (!(arguments.get(0) instanceof Target)) throw new InvalidArgumentException("aliasSub: argument is " + arguments.get(0).getClass().getName() + ". Must be series");
        if (!(arguments.get(1) instanceof String)) throw new InvalidArgumentException("aliasSub: argument is " + arguments.get(1).getClass().getName() + ". Must be a string");
        if (!(arguments.get(2) instanceof String)) throw new InvalidArgumentException("aliasSub: argument is " + arguments.get(1).getClass().getName() + ". Must be a string");
    }
}
