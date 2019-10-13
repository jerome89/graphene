package net.iponweb.disthene.reader.format;

import com.google.common.base.Joiner;
import com.google.gson.Gson;
import io.netty.buffer.Unpooled;
import io.netty.handler.codec.http.*;
import net.iponweb.disthene.reader.beans.TimeSeries;
import net.iponweb.disthene.reader.exceptions.LogarithmicScaleNotAllowed;
import net.iponweb.disthene.reader.graph.DecoratedTimeSeries;
import net.iponweb.disthene.reader.graphite.utils.GraphiteUtils;
import com.graphene.reader.handler.RenderParameter;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.NotImplementedException;
import org.joda.time.DateTime;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Andrei Ivanov
 */
public class ResponseFormatter {

    private static Gson GSON = new Gson();

    public static FullHttpResponse formatResponse(List<TimeSeries> timeSeriesList, RenderParameter parameters) throws NotImplementedException, LogarithmicScaleNotAllowed {
        // Let's remove empty series
        List<TimeSeries> filtered = filterAllNulls(timeSeriesList);

        // TODO 원복시키기
//        switch (parameters.getFormat()) {
//            case JSON:
//            case RAW: return formatResponseAsRaw(filtered);
//            case CSV: return formatResponseAsCSV(filtered, parameters);
//            case PNG: return formatResponseAsPng(filtered, parameters);
//            case GRAPHPLOT_JSON: return formatResponseAsGraphplotJson(filtered, parameters);
//            default:throw new NotImplementedException();
//        }
        return formatResponseAsJson(filtered, parameters);
    }

    private static FullHttpResponse formatResponseAsCSV(List<TimeSeries> timeSeriesList, RenderParameter renderParameter) {
        List<String> results = new ArrayList<>();

        for(TimeSeries timeSeries : timeSeriesList) {
            Double[] values = timeSeries.getValues();
            for(int i = 0; i < values.length; i++) {
                DateTime dt = new DateTime((timeSeries.getFrom() + i * timeSeries.getStep()) * 1000, renderParameter.getTz());
                String stringValue;
                if (values[i] == null) {
                    stringValue = "";
                } else {
                    BigDecimal bigDecimal = BigDecimal.valueOf(values[i]);
                    if (bigDecimal.precision() > 10) {
                        bigDecimal = bigDecimal.setScale(bigDecimal.precision() - 1, BigDecimal.ROUND_HALF_UP);
                    }

                    stringValue = bigDecimal.stripTrailingZeros().toPlainString();
                }
                results.add(timeSeries.getName() + "," + dt.toString("YYYY-MM-dd HH:mm:ss") + "," + stringValue);
            }
        }

        String responseString = Joiner.on("\n").join(results);

        FullHttpResponse response = new DefaultFullHttpResponse(
                HttpVersion.HTTP_1_1,
                HttpResponseStatus.OK,
                Unpooled.wrappedBuffer(responseString.getBytes()));
        response.headers().set(HttpHeaders.Names.CONTENT_TYPE, "text/csv");
        response.headers().set(HttpHeaders.Names.CONTENT_LENGTH, response.content().readableBytes());
        return response;
    }

    private static FullHttpResponse formatResponseAsRaw(List<TimeSeries> timeSeriesList) {
        List<String> results = new ArrayList<>();

        for(TimeSeries timeSeries : timeSeriesList) {
            List<String> formattedValues = new ArrayList<>();
            for (Double value : timeSeries.getValues()) {
                if (value == null) {
                    formattedValues.add("null");
                } else {
                    formattedValues.add(GraphiteUtils.formatDoubleSpecialPlain(value));
                }
            }
            results.add(timeSeries.getName() + "," + timeSeries.getFrom() + "," + timeSeries.getTo() + "," + timeSeries.getStep() + "|" + Joiner.on(",").useForNull("null").join(formattedValues));
        }

        String responseString = Joiner.on("\n").join(results);


        FullHttpResponse response = new DefaultFullHttpResponse(
                HttpVersion.HTTP_1_1,
                HttpResponseStatus.OK,
                Unpooled.wrappedBuffer(responseString.getBytes()));
        response.headers().set(HttpHeaders.Names.CONTENT_TYPE, "text/plain");
        response.headers().set(HttpHeaders.Names.CONTENT_LENGTH, response.content().readableBytes());
        return response;
    }

    private static FullHttpResponse formatResponseAsJson(List<TimeSeries> timeSeriesList, RenderParameter renderParameter) {
        // consolidate data points
        consolidate(timeSeriesList, renderParameter.getMaxDataPoints());
        StringBuilder result = new StringBuilder(computeStringBuilderCapacity(timeSeriesList));
        appendResults(result, timeSeriesList);

        FullHttpResponse response = new DefaultFullHttpResponse(
                HttpVersion.HTTP_1_1,
                HttpResponseStatus.OK,
                Unpooled.wrappedBuffer(result.toString().getBytes()));
        response.headers().set(HttpHeaders.Names.CONTENT_TYPE, "application/json");
        response.headers().set(HttpHeaders.Names.CONTENT_LENGTH, response.content().readableBytes());
        return response;
    }

    private static FullHttpResponse formatResponseAsGraphplotJson(List<TimeSeries> timeSeriesList, RenderParameter renderParameter) {
        List<String> results = new ArrayList<>();

        Gson gson = new Gson();

        // consolidate data points
        consolidate(timeSeriesList, renderParameter.getMaxDataPoints());

        for(TimeSeries timeSeries : timeSeriesList) {
            List<String> datapoints = new ArrayList<>();
            for(int i = 0; i < timeSeries.getValues().length; i++) {
                String stringValue;
                if (timeSeries.getValues()[i] == null) {
                    stringValue = "null";
                } else {
                    stringValue = GraphiteUtils.formatDoubleSpecialPlain(timeSeries.getValues()[i]);
                }

                datapoints.add(stringValue);
            }
            results.add("{\"start\": " + timeSeries.getFrom() + ", \"step\": " + timeSeries.getStep() + ", \"end\": " + (timeSeries.getFrom() + timeSeries.getStep() * timeSeries.getValues().length) + ", \"name\": \"" + timeSeries.getName() + "\", \"data\": [" + Joiner.on(", ").join(datapoints) + "]}");
        }
        String responseString = "[" + Joiner.on(", ").join(results) + "]";


        FullHttpResponse response = new DefaultFullHttpResponse(
                HttpVersion.HTTP_1_1,
                HttpResponseStatus.OK,
                Unpooled.wrappedBuffer(responseString.getBytes()));
        response.headers().set(HttpHeaders.Names.CONTENT_TYPE, "application/json");
        response.headers().set(HttpHeaders.Names.CONTENT_LENGTH, response.content().readableBytes());
        return response;
    }


    private static void consolidate(List<TimeSeries> timeSeriesList, int maxDataPoints) {
        for (TimeSeries ts : timeSeriesList) {
            DecoratedTimeSeries dts = new DecoratedTimeSeries(ts);
            if (maxDataPoints < ts.getValues().length) {
                dts.setValuesPerPoint((int) Math.ceil(ts.getValues().length / (double) maxDataPoints));
            } else {
                dts.setValuesPerPoint(1);
            }

            ts.setStep(dts.getValuesPerPoint() * dts.getStep());
            ts.setTo(ts.getFrom() + ts.getStep() * dts.getConsolidatedValues().length - 1);
            ts.setValues(dts.getConsolidatedValues());
        }
    }

    private static List<TimeSeries> filterAllNulls(List<TimeSeries> timeSeriesList) {
        List<TimeSeries> result = new ArrayList<>();

        for (TimeSeries ts : timeSeriesList) {
            for (Double value : ts.getValues()) {
                if (value != null) {
                    result.add(ts);
                    break;
                }
            }
        }

        return result;
    }

    private static int computeStringBuilderCapacity(List<TimeSeries> timeSeriesList) {
        // For each TimeSeries, 28 = {"target":,"datapoints":[]},
        // Subtract 1 because the last TimeSeries does not have comma.
        // Plus 2 = [] --> 28 * timeSeriesList.size() - 1 + 2 = 28 * timeSeriesList.size() + 1
        int stringBuilderCapacity = 28 * timeSeriesList.size() + 1;
        for (TimeSeries ts : timeSeriesList) {
            ts.setName(GSON.toJson(ts.getName()));
            // For each timeSeries, there are
            // 1. TimeSeries name length
            // 2. Length of timestamp = 10
            // 3. Necessary symbols = [,], = 4
            // So, 1 + (2 + 3) * (Count of values), but subtract 1 because the last datapoint does not have comma.
            stringBuilderCapacity += ts.getName().length() + (14 * ts.getValues().length) - 1;
            // Count for each datapoint's length.
            for (Double val : ts.getValues()) {
                stringBuilderCapacity += String.valueOf(val).length();
            }
        }
        return stringBuilderCapacity;
    }

    private static void appendResults(StringBuilder sb, List<TimeSeries> timeSeriesList) {
        sb.append("[");
        for (TimeSeries ts : timeSeriesList) {
            if (isSecondTime(sb)) {
                sb.append(",");
            }
            appendTimeSeries(sb, ts);
        }
        sb.append("]");
    }

    private static boolean isSecondTime(StringBuilder sb) {
        return 2 <= sb.length();
    }

    private static void appendTimeSeries(StringBuilder sb, TimeSeries ts) {
        sb.append("{").append("\"target\"").append(":").append(ts.getName()).append(",");
        sb.append("\"datapoints\"").append(":[");
        if (ArrayUtils.isNotEmpty(ts.getValues())) {
            for (int i = 0; i < ts.getValues().length; i++) {
                sb.append("[").append(ts.getValues()[i]).append(",").append(ts.getFrom() + i * ts.getStep()).append("],");
            }
            sb.deleteCharAt(sb.length() - 1);
        }
        sb.append("]}");
    }
}
