package com.graphene.reader.graphite.functions;

import com.google.common.base.Joiner;
import com.graphene.reader.beans.TimeSeries;
import com.graphene.reader.exceptions.EvaluationException;
import com.graphene.reader.exceptions.InvalidArgumentException;
import com.graphene.reader.exceptions.TimeSeriesNotAlignedException;
import com.graphene.reader.graphite.Target;
import com.graphene.reader.graphite.evaluation.TargetEvaluator;
import com.graphene.reader.utils.Grouper;
import com.graphene.reader.utils.TimeSeriesUtils;

import java.util.ArrayList;
import java.util.List;


public class GroupByNodesFunction extends GrapheneFunction {

    public GroupByNodesFunction(String text) {
        super(text, "groupByNodes");
    }

    @Override
    public List<TimeSeries> evaluate(TargetEvaluator evaluator) throws EvaluationException {
        List<TimeSeries> processedArguments = new ArrayList<>();
        processedArguments.addAll(evaluator.eval((Target) arguments.get(0)));

        if (processedArguments.size() == 0) return new ArrayList<>();

        if (!TimeSeriesUtils.checkAlignment(processedArguments)) {
            throw new TimeSeriesNotAlignedException();
        }

        String callbackName = (String) arguments.get(1);

        int[] positions = new int[arguments.size() - 2];
        for (int i = 2; i < arguments.size(); i++) {
            positions[i - 2] = ((Double) arguments.get(i)).intValue();
        }

        return new Grouper(processedArguments, callbackName, from, to).byNodesIndex(positions);
    }

    @Override
    public void checkArguments() throws InvalidArgumentException {
        if (arguments.size() < 3)
            throw new InvalidArgumentException("groupByNodes: number of arguments is " + arguments.size() + ". Must be a least two.");

        if (!(arguments.get(0) instanceof Target))
            throw new InvalidArgumentException("groupByNodes: argument is " + arguments.get(0).getClass().getName() + ". Must be series");

        if (!(arguments.get(1) instanceof String))
            throw new InvalidArgumentException("groupByNodes: argument is " + arguments.get(1).getClass().getName() + ". Must be a string");

        if (!(Grouper.hasAggregationMethod((String) arguments.get(1)))) {
            throw new InvalidArgumentException("groupByNodes: argument is " + arguments.get(1) + ". Must be one of " +
                                               Joiner.on(", ").join(Grouper.getAvailableAggregationMethods()) + ".");
        }

        for (int i = 2; i < arguments.size(); i++) {
            if (!(arguments.get(i) instanceof Double))
                throw new InvalidArgumentException("groupByNodes: argument " + i + " is " + arguments.get(i).getClass().getName() + ". Must be a number");
        }
    }
}
