package com.graphene.reader.utils;

import com.graphene.reader.exceptions.InvalidParameterValueException;
import com.mdimension.jchronic.Chronic;
import com.mdimension.jchronic.utils.Span;
import com.graphene.reader.exceptions.InvalidParameterValueException;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Andrei Ivanov
 */
public class DateUtils {

    private static final Pattern EXTENDED_TIME_PATTERN = Pattern.compile("-*([0-9]+)([a-zA-Z]+)");

    public static long parseDateTime(String in, DateTimeZone tz) throws InvalidParameterValueException {
        Span span = Chronic.parse(in);
        if (span == null) {
            throw new InvalidParameterValueException("Unsupported date format: " + in);
        }

        return new DateTime(span.getBeginCalendar().getTimeInMillis(), tz).getMillis() / 1000L;
    }

    public static long parseExtendedTime(String timeString, DateTimeZone tz) {
        Matcher matcher = EXTENDED_TIME_PATTERN.matcher(timeString);

        if (matcher.matches()) {
            String value = matcher.group(1);
            String unit = matcher.group(2);
            Long unitValue;

            // calc unit value
            if (unit.startsWith("s")) {
                unitValue = 1L;
            } else if (unit.startsWith("min")) {
                unitValue = 60L;
            } else if (unit.startsWith("h")) {
                unitValue = 3600L;
            } else if (unit.startsWith("d")) {
                unitValue = 86400L;
            } else if (unit.startsWith("w")) {
                unitValue = 604800L;
            } else if (unit.startsWith("mon")) {
                unitValue = 2678400L;
            } else if (unit.startsWith("y")) {
                unitValue = 31536000L;
            } else {
                unitValue = 60L;
            }
            // calc offset as (now) - (number * unit value)
            return (System.currentTimeMillis() / 1000L) - (Long.parseLong(value) * unitValue);
        } else if ("now".equals(timeString.toLowerCase())) {
            return System.currentTimeMillis() / 1000L;
        } else {
            return new DateTime(Long.parseLong(timeString) * 1000, tz).getMillis() / 1000L;
        }
    }
}
