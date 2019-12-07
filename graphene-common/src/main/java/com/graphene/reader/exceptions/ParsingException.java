package com.graphene.reader.exceptions;

public class ParsingException extends RuntimeException {

    public ParsingException(Throwable cause) {
        super(cause);
    }

    public ParsingException(String message) {
        super(message);
    }
}
