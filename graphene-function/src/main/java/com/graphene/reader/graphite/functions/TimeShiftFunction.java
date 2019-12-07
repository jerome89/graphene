package com.graphene.reader.graphite.functions;

import com.graphene.reader.beans.TimeSeries;
import com.graphene.reader.exceptions.EvaluationException;
import com.graphene.reader.exceptions.InvalidArgumentException;
import com.graphene.reader.exceptions.TimeSeriesNotAlignedException;
import com.graphene.reader.graphite.Target;
import com.graphene.reader.graphite.evaluation.TargetEvaluator;
import com.graphene.common.utils.DateTimeUtils;
import com.graphene.reader.utils.TimeSeriesUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * @author Andrei Ivanov
 */
public class TimeShiftFunction extends GrapheneFunction {

    public TimeShiftFunction(String text) {
        super(text, "timeShift");
    }

    @Override
    public List<TimeSeries> evaluate(TargetEvaluator evaluator) throws EvaluationException {
        // parse offset
        long offset = DateTimeUtils.INSTANCE.parseTimeOffset((String) arguments.get(1));

        // apply shift to pathTarget
        // todo: we will experience some problems if this resolution doesn't exist anymore in the past... take care of this corner case!
        List<TimeSeries> processedArguments = new ArrayList<>(evaluator.eval(((Target) arguments.get(0)).shiftBy(offset)));

        if (processedArguments.size() == 0) return new ArrayList<>();

        if (!TimeSeriesUtils.checkAlignment(processedArguments)) {
            throw new TimeSeriesNotAlignedException();
        }

        for (TimeSeries ts : processedArguments) {
            ts.setFrom(ts.getFrom() - offset);
            ts.setTo(ts.getTo() - offset);
            ts.setName("timeShift(" + ts.getName() + "," + arguments.get(1) + ")");
        }

        return processedArguments;
    }

    @Override
    public void checkArguments() throws InvalidArgumentException {
        check(arguments.size() == 2,
            "timeShift: number of arguments is " + arguments.size() + ". Must be two.");

        // argument cannot be a result of another function - it's not clear how to evaluate it in that case
        // todo: I have a solution(jerome89), will work it later.
        Optional<Object> argSeries = Optional.ofNullable(arguments.get(0));
        check(argSeries.orElse(null) instanceof Target,
            "timeShift: argument is " + getClassName(argSeries.orElse(null)) + ". Must be series");
        Optional<Object> argStringOffset = Optional.ofNullable(arguments.get(1));
        check(argStringOffset.orElse(null) instanceof String,
            "timeShift: Offset argument is " + getClassName(argStringOffset.orElse(null)) + ". Must be String.");
        check(DateTimeUtils.INSTANCE.testTimeOffset((String) argStringOffset.get()),
            "timeShift: shift cannot be parsed (" + argStringOffset.get() + ")");
    }
}
