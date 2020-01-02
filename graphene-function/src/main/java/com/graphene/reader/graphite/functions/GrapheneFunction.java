package com.graphene.reader.graphite.functions;

import com.graphene.reader.beans.TimeSeries;
import com.graphene.reader.exceptions.InvalidArgumentException;
import com.graphene.reader.exceptions.InvalidFunctionException;
import com.graphene.reader.exceptions.UnsupportedMethodException;
import com.graphene.reader.graphite.Target;
import com.graphene.reader.graphite.functions.registry.FunctionRegistry;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Andrei Ivanov
 */
public abstract class GrapheneFunction extends Target {

    protected List<Object> arguments = new ArrayList<>();
    protected String name;
    protected Long from;
    protected Long to;
    protected String tenant;

    public GrapheneFunction(String text, String name) {
        super(text);
        this.name = name;
    }

    public void addArg(Object argument) {
        arguments.add(argument);
    }

    public List<TimeSeries> computeDirectly(List<TimeSeries> timeSeriesList) throws UnsupportedMethodException {
        throw new UnsupportedMethodException("Cannot call this method on " + this.name);
    }

    public abstract void checkArguments() throws InvalidArgumentException;

    @Override
    public String toString() {
        return this.getClass().getSimpleName() + "{" +
                ", arguments=" + arguments +
                '}';
    }

    public String getName() {
        return name;
    }

    protected void setResultingName(TimeSeries timeSeries) {
        timeSeries.setName(name + "(" + timeSeries.getName() + ")");
    }

    public Long getFrom() {
        return from;
    }

    public void setFrom(Long from) {
        this.from = from;
    }

    public Long getTo() {
        return to;
    }

    public void setTo(Long to) {
        this.to = to;
    }

    public String getTenant() {
      return tenant;
    }

    public void setTenant(String tenant) {
      this.tenant = tenant;
    }

    protected String getResultingName(TimeSeries timeSeries) {
        return name + "(" + timeSeries.getName() + ")";
    }

    @Override
    public Target shiftBy(long shift) {
        try {
            GrapheneFunction function = FunctionRegistry.getFunction(getContext(), name, tenant, from + shift, to + shift);

            for (Object argument : arguments) {
                if (argument instanceof Target) {
                    function.addArg(((Target) argument).shiftBy(shift));
                } else {
                    function.addArg(argument);
                }
            }

            // we'd like to keep the text intact vs replacing it with function name here
            function.setText(getText());
            return function;
        } catch (InvalidFunctionException ignored) {
        }

        return null;
    }

    @Override
    public Target previous(long period) {
        try {
            GrapheneFunction function = FunctionRegistry.getFunction(getContext(), name, tenant,from - period , from - 1);

            for (Object argument : arguments) {
                if (argument instanceof Target) {
                    function.addArg(((Target) argument).previous(period));
                } else {
                    function.addArg(argument);
                }
            }

            return function;
        } catch (InvalidFunctionException ignored) {
        }

        return null;
    }

    public void check(boolean condition, String message) throws InvalidArgumentException {
        if (! condition) {
            throw new InvalidArgumentException(message);
        }
    }

    public String getClassName(Object object) {
        if (null == object) {
            return "null";
        }
        return object.getClass().getName();
    }
}
