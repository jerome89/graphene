package com.graphene.reader.graphite.evaluation;

import com.graphene.reader.graphite.utils.ValueFormatter;

/**
 * @author Andrei Ivanov
 */
public class EvaluationContext {

    private ValueFormatter formatter;

    public EvaluationContext(ValueFormatter formatter) {
        this.formatter = formatter;
    }

    public ValueFormatter getFormatter() {
        return formatter;
    }
}
