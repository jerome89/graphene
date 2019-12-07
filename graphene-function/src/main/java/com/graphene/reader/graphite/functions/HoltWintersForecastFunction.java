package com.graphene.reader.graphite.functions;

import com.graphene.reader.beans.TimeSeries;
import com.graphene.reader.exceptions.EvaluationException;
import com.graphene.reader.exceptions.InvalidArgumentException;
import com.graphene.reader.graphite.Target;
import com.graphene.reader.graphite.evaluation.TargetEvaluator;
import com.graphene.reader.graphite.utils.HoltWinters;

import java.util.List;

/**
 * @author Andrei Ivanov
 */
public class HoltWintersForecastFunction extends GrapheneFunction {

    public HoltWintersForecastFunction(String text) {
        super(text, "holtWintersForecast");
    }

    @Override
    public List<TimeSeries> evaluate(TargetEvaluator evaluator) throws EvaluationException {
        List<TimeSeries> forecasts = HoltWinters.analyze((Target) arguments.get(0), evaluator).getForecasts();

        for (TimeSeries ts : forecasts) {
            setResultingName(ts);
        }

        return forecasts;
    }

    @Override
    public void checkArguments() throws InvalidArgumentException {
        if (arguments.size() > 1 || arguments.size() == 0) throw new InvalidArgumentException("holtWintersForecast: number of arguments is " + arguments.size() + ". Must be 1.");
        if (!(arguments.get(0) instanceof Target)) throw new InvalidArgumentException("holtWintersForecast: argument is " + arguments.get(0).getClass().getName() + ". Must be series");
    }
}
