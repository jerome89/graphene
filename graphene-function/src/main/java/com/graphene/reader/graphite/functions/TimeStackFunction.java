package com.graphene.reader.graphite.functions;

import com.graphene.reader.beans.TimeSeries;
import com.graphene.reader.exceptions.EvaluationException;
import com.graphene.reader.exceptions.InvalidArgumentException;
import com.graphene.reader.graphite.Target;
import com.graphene.reader.graphite.evaluation.TargetEvaluator;
import com.graphene.common.utils.DateTimeUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * @author Andrei Ivanov
 */
public class TimeStackFunction extends GrapheneFunction {

    public TimeStackFunction(String text) {
        super(text, "timeStack");
    }

    @Override
    public List<TimeSeries> evaluate(TargetEvaluator evaluator) throws EvaluationException {
        // parse offset
        long offset = DateTimeUtils.INSTANCE.parseTimeOffset((String) arguments.get(1));
        int startIndex = ((Double) arguments.get(2)).intValue();
        int endIndex = ((Double) arguments.get(3)).intValue();


        List<TimeSeries> resultList = new ArrayList<>();
        // todo: we will experience some problems if this resolution doesn't exist anymore in the past... take care of this corner case!
        while (startIndex <= endIndex) {
            long shiftAmount = offset * startIndex;
            List<TimeSeries> processedArguments = evaluator.eval(((Target) arguments.get(0)).shiftBy(shiftAmount));
            for (TimeSeries ts : processedArguments) {
                ts.setFrom(ts.getFrom() - shiftAmount);
                ts.setTo(ts.getTo() - shiftAmount);
                ts.setName("timeShift(" + ts.getName() + "," + arguments.get(1) + "," + startIndex + ")");
                resultList.add(ts);
            }

            startIndex++;
        }

        return resultList;
    }

    @Override
    public void checkArguments() throws InvalidArgumentException {
        check(arguments.size() == 4,
            "timeStack: number of arguments is " + arguments.size() + ". Must be four.");

        // argument cannot be a result of another function - it's not clear how to evaluate it in that case
        // todo: I have a solution(jerome89), will work it later.
        Optional<Object> argSeries = Optional.ofNullable(arguments.get(0));
        check(argSeries.orElse(null) instanceof Target,
            "timeStack: argument is " + getClassName(argSeries.orElse(null)) + ". Must be series wildcard");

        Optional<Object> argStringOffset = Optional.ofNullable(arguments.get(1));
        check(argStringOffset.orElse(null) instanceof String,
            "timeStack: Offset argument is " + getClassName(argStringOffset.orElse(null)) + ". Must be String.");
        check(DateTimeUtils.INSTANCE.testTimeOffset((String) argStringOffset.get()),
            "timeStack: shift cannot be parsed (" + argStringOffset.get() + ")");

        for (int i = 2; i < 4; i++) {
            Optional<Object> argOffsetIndex = Optional.ofNullable(arguments.get(i));
            check(argOffsetIndex.orElse(null) instanceof Double,
                "timeStack: argument is " + getClassName(argOffsetIndex.orElse(null)) + ". Must be a number");
        }
    }
}
