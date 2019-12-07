package com.graphene.reader.graphite.functions;

import com.graphene.reader.exceptions.EvaluationException;
import com.graphene.reader.exceptions.InvalidArgumentException;
import com.graphene.reader.graphite.evaluation.TargetEvaluator;
import com.graphene.reader.utils.CollectionUtils;
import com.graphene.reader.beans.TimeSeries;

import java.util.Collections;
import java.util.List;

/**
 * @author Andrei Ivanov
 */
public class ConstantLineFunction extends GrapheneFunction {

    public ConstantLineFunction(String text) {
        super(text, "constantLine");
    }

    @Override
    public List<TimeSeries> evaluate(TargetEvaluator evaluator) throws EvaluationException {
        Double constant = (Double) arguments.get(0);

        TimeSeries ts = evaluator.getEmptyTimeSeries(from, to);
        CollectionUtils.constant(ts.getValues(), constant);

        ts.setName(String.valueOf(constant));

        return Collections.singletonList(ts);
    }

    @Override
    public void checkArguments() throws InvalidArgumentException {
        if (arguments.size() != 1) throw new InvalidArgumentException("constantLine: number of arguments is " + arguments.size() + ". Must be one.");
        if (!(arguments.get(0) instanceof Double)) throw new InvalidArgumentException("constantLine: argument is " + arguments.get(0).getClass().getName() + ". Must be a number");
    }
}
