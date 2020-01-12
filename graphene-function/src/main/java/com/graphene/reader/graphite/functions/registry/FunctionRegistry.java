package com.graphene.reader.graphite.functions.registry;

import com.graphene.reader.exceptions.InvalidFunctionException;
import com.graphene.reader.graphite.evaluation.EvaluationContext;
import com.graphene.reader.graphite.functions.*;

import java.lang.reflect.Constructor;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Andrei Ivanov
 */
public class FunctionRegistry {

    private static final Map<String, Class<? extends GrapheneFunction>> registry = new HashMap<>();

    static {
        registry.put("absolute", AbsoluteFunction.class);
        registry.put("aggregateLine", AggregateLineFunction.class);
        registry.put("alias", AliasFunction.class);
        registry.put("aliasByNode", AliasByNodeFunction.class);
        registry.put("aliasByMetric", AliasByMetricFunction.class);
        registry.put("aliasSub", AliasSubFunction.class);
        registry.put("alpha", AlphaFunction.class);
        registry.put("areaBetween", AreaBetweenFunction.class);
        registry.put("asPercent", AsPercentFunction.class);
        registry.put("averageOutsidePercentile", AverageOutsidePercentileFunction.class);
        registry.put("pct", AsPercentFunction.class);
        registry.put("averageAbove", AverageAboveFunction.class);
        registry.put("averageBelow", AverageBelowFunction.class);
        registry.put("averageSeries", AverageSeriesFunction.class);
        registry.put("averageSeriesWithWildcards", AverageSeriesWithWildcardsFunction.class);
        registry.put("avg", AverageSeriesFunction.class);
        registry.put("cactiStyle", CactiStyleFunction.class);
        registry.put("changed", ChangedFunction.class);
        registry.put("color", ColorFunction.class);
        registry.put("constantLine", ConstantLineFunction.class);
        registry.put("countSeries", CountSeriesFunction.class);
        registry.put("currentAbove", CurrentAboveFunction.class);
        registry.put("currentBelow", CurrentBelowFunction.class);
        registry.put("dashed", DashedFunction.class);
        registry.put("derivative", DerivativeFunction.class);
        registry.put("diffSeries", DiffSeriesFunction.class);
        registry.put("divideSeries", DivideSeriesFunction.class);
        registry.put("drawAsInfinite", DrawAsInfiniteFunction.class);
        registry.put("grep", GrepFunction.class);
        registry.put("group", GroupFunction.class);
        registry.put("groupByNode", GroupByNodeFunction.class);
        registry.put("groupByNodes", GroupByNodesFunction.class);
        registry.put("exclude", ExcludeFunction.class);
        registry.put("highestAverage", HighestAverageFunction.class);
        registry.put("highestCurrent", HighestCurrentFunction.class);
        registry.put("highestMax", HighestMaxFunction.class);
        registry.put("hitcount", HitcountFunction.class);
        registry.put("holtWintersAberration", HoltWintersAberrationFunction.class);
        registry.put("holtWintersConfidenceArea", HoltWintersConfidenceAreaFunction.class);
        registry.put("holtWintersConfidenceBands", HoltWintersConfidenceBandsFunction.class);
        registry.put("holtWintersForecast", HoltWintersForecastFunction.class);
        registry.put("integral", IntegralFunction.class);
        registry.put("invert", InvertFunction.class);
        registry.put("isNonNull", IsNonNullFunction.class);
        registry.put("keepLastValue", KeepLastValueFunction.class);
        registry.put("legendValue", LegendValueFunction.class);
        registry.put("limit", LimitFunction.class);
        registry.put("lineWidth", LineWidthFunction.class);
        registry.put("logarithm", LogarithmFunction.class);
        registry.put("log", LogarithmFunction.class);
        registry.put("lowestAverage", LowestAverageFunction.class);
        registry.put("lowestCurrent", LowestCurrentFunction.class);
        registry.put("mapSeries", MapSeriesFunction.class);
        registry.put("map", MapSeriesFunction.class);
        registry.put("maximumAbove", MaximumAboveFunction.class);
        registry.put("maximumBelow", MaximumBelowFunction.class);
        registry.put("minimumAbove", MinimumAboveFunction.class);
        registry.put("minimumBelow", MinimumBelowFunction.class);
        registry.put("maxSeries", MaxSeriesFunction.class);
        registry.put("minSeries", MinSeriesFunction.class);
        registry.put("mostDeviant", MostDeviantFunction.class);
        registry.put("movingAverage", MovingAverageFunction.class);
        registry.put("movingMax", MovingMaxFunction.class);
        registry.put("movingMedian", MovingMedianFunction.class);
        registry.put("movingMin", MovingMinFunction.class);
        registry.put("multiplySeries", MultiplySeriesFunction.class);
        registry.put("multiplySeriesWithWildcards", MultiplySeriesWithWildcardsFunction.class);
        registry.put("nonNegativeDerivative", NonNegativeDerivativeFunction.class);
        registry.put("nPercentile", NPercentileFunction.class);
        registry.put("offset", OffsetFunction.class);
        registry.put("offsetToZero", OffsetToZeroFunction.class);
        registry.put("percentileOfSeries", PercentileOfSeriesFunction.class);
        registry.put("perSecond", PerSecondFunction.class);
        registry.put("pow", PowFunction.class);
        registry.put("rangeOfSeries", RangeOfSeriesFunction.class);
        registry.put("reduceSeries", ReduceSeriesFunction.class);
        registry.put("reduce", ReduceSeriesFunction.class);
        registry.put("removeAbovePercentile", RemoveAbovePercentileFunction.class);
        registry.put("removeAboveValue", RemoveAboveValueFunction.class);
        registry.put("removeBelowPercentile", RemoveBelowPercentileFunction.class);
        registry.put("removeBelowValue", RemoveBelowValueFunction.class);
        registry.put("scale", ScaleFunction.class);
        registry.put("scaleToSeconds", ScaleToSecondsFunction.class);
        registry.put("secondYAxis", SecondYAxisFunction.class);
        registry.put("sortByMaxima", SortByMaximaFunction.class);
        registry.put("sortByMinima", SortByMinimaFunction.class);
        registry.put("sortByName", SortByNameFunction.class);
        registry.put("sortByTotal", SortByTotalFunction.class);
        registry.put("squareRoot", SquareRootFunction.class);
        registry.put("stacked", StackedFunction.class);
        registry.put("stdev", StdevFunction.class);
        registry.put("stddevSeries", StddevSeriesFunction.class);
        registry.put("sumSeries", SumSeriesFunction.class);
        registry.put("sumSeriesWithWildcards", SumSeriesWithWildcardsFunction.class);
        registry.put("sum", SumSeriesFunction.class);
        registry.put("summarize", SummarizeFunction.class);
        registry.put("threshold", ThresholdFunction.class);
        registry.put("timeShift", TimeShiftFunction.class);
        registry.put("timeStack", TimeStackFunction.class);
        registry.put("transformNull", TransformNullFunction.class);
        registry.put("useSeriesAbove", UseSeriesAboveFunction.class);
        registry.put("seriesByTag", SeriesByTagFunction.class);
        registry.put("groupByTags", GroupByTagsFunction.class);
        registry.put("aliasByTags", AliasByTagsFunction.class);
    }

    //todo: from & to parameters are only because of constantLine function. sort this out?
    public static GrapheneFunction getFunction(EvaluationContext context, String name, String tenant, long from, long to) throws InvalidFunctionException {
        if (registry.get(name) == null) {
            throw new InvalidFunctionException("Unknown function: " + name);
        }

        try {
            @SuppressWarnings("unchecked")
            Constructor<GrapheneFunction> constructor = (Constructor<GrapheneFunction>) registry.get(name).getConstructor(String.class);
            GrapheneFunction function = constructor.newInstance(name);
            function.setTenant(tenant);
            function.setFrom(from);
            function.setTo(to);
            function.setContext(context);
            return function;
        } catch (Exception e) {
            throw new InvalidFunctionException("Something went wrong constructing " + name + " function: " + e.getMessage());
        }
    }
}
