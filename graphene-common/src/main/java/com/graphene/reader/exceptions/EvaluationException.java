package com.graphene.reader.exceptions;

/**
 * @author Andrei Ivanov
 */
public class EvaluationException extends Exception{

    public EvaluationException() {
    }

    public EvaluationException(String message) {
      super(message);
    }

    public EvaluationException(Throwable cause) {
        super(cause);
    }
}
