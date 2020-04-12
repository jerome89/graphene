package com.graphene.function.prometheus.grammar

import org.antlr.v4.runtime.CharStream
import org.antlr.v4.runtime.Recognizer
import org.antlr.v4.runtime.Token

class PrometheusValidatorLexer(input: CharStream) : PrometheusLexer(input) {

  var eqlCount: Int = 0
  var eqlRegexCount: Int = 0
  var neqCount: Int = 0
  var neqRegexCount: Int = 0

  override fun nextToken(): Token {
    val nextToken = super.nextToken()

    return when (nextToken.type) {
      Recognizer.EOF -> {
        validate()
        nextToken
      }
      STRING -> {
        val la = _input.LA(1).toString()

        if (isBraceOpen && la == "}") {
          if (0 == eqlCount + eqlRegexCount + neqCount + neqRegexCount) {
            throw NotIncludeQueryOperatorInVectorMatchingException("Please check the query operator whether omit or not.")
          }

          if (_token.text.indexOf(':') != -1) {
            throw IllegalColonInBraceExpressionException("Does not allow the colon usage inside brace syntax.")
          }
        }
        nextToken
      }
      EQL -> {
        eqlCount++
        nextToken
      }
      EQL_REGEX -> {
        eqlRegexCount++
        nextToken
      }
      NEQ -> {
        neqCount++
        nextToken
      }
      NEQ_REGEX -> {
        neqRegexCount++
        nextToken
      }
      else -> nextToken
    }
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

    // https://prometheus.io/docs/prometheus/latest/querying/operators/#vector-matching
//    if (1 <= isBraceOpenCount) {
//      if (0 == eqlCount + eqlRegexCount + neqCount + neqRegexCount) {
//        throw NotIncludeQueryOperatorInVectorMatchingException("Please check the query operator whether omit or not.")
//      }
//    }
  }
}
