package com.graphene.function.prometheus.grammar

import org.antlr.v4.runtime.CharStream
import org.antlr.v4.runtime.Recognizer
import org.antlr.v4.runtime.Token

class PrometheusValidatorLexer(input: CharStream) : PrometheusLexer(input) {

  override fun nextToken(): Token {
    val nextToken = super.nextToken()

    if (nextToken.type == Recognizer.EOF) {
      validate()
    }

    return nextToken
  }

  private fun validate() {
    if (isBraceOpen) {
      throw IllegalBraceException("left-brace does not close. This case is an illegal query.")
    }

    if (1 < isBraceOpenCount) {
      throw IllegalBraceException("left-brace open only once.")
    }

    if (isParenOpen) {
      throw IllegalParenException("left-paren does not close. This case is an illegal query.")
    }

    if (1 < isParenOpenCount) {
      throw IllegalBraceException("left-paren open only once.")
    }

    if (isBracketOpen) {
      throw IllegalBracketException("left-bracket does not close. This case is an illegal query.")
    }

    if (1 < isBracketOpenCount) {
      throw IllegalBracketException("left-bracket open only once.")
    }
  }
}
