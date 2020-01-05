package com.graphene.writer.input

import java.lang.RuntimeException

class UnexpectedConverterException(message: String, cause: Throwable) : RuntimeException(message, cause)
