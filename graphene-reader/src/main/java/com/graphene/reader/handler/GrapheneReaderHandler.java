package com.graphene.reader.handler;

import com.graphene.reader.exceptions.EvaluationException;
import com.graphene.reader.exceptions.LogarithmicScaleNotAllowed;
import com.graphene.reader.exceptions.ParameterParsingException;
import com.graphene.reader.exceptions.TooMuchDataExpectedException;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.HttpRequest;

import java.util.concurrent.ExecutionException;

/**
 * @author Andrei Ivanov
 */
public interface GrapheneReaderHandler {

    FullHttpResponse handle(HttpRequest request) throws ParameterParsingException, ExecutionException, InterruptedException, EvaluationException, LogarithmicScaleNotAllowed, TooMuchDataExpectedException;
}
