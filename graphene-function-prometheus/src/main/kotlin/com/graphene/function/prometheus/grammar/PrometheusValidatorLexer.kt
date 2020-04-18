package com.graphene.function.prometheus.grammar

import org.antlr.v4.runtime.BaseErrorListener
import org.antlr.v4.runtime.CharStream
import org.antlr.v4.runtime.RecognitionException
import org.antlr.v4.runtime.Recognizer
import org.antlr.v4.runtime.Token
import org.slf4j.LoggerFactory

class PrometheusValidatorLexer(input: CharStream) : PrometheusLexer(input) {

  var eqlCount: Int = 0
  var eqlRegexCount: Int = 0
  var neqCount: Int = 0
  var neqRegexCount: Int = 0

  var tmpTokens: MutableList<Token> = mutableListOf()

  private val antlrErrorListener = ErrorCountableANTLRErrorListener()

  init {
    removeErrorListeners()
    addErrorListener(antlrErrorListener)
  }

  override fun nextToken(): Token {
    val nextToken = super.nextToken()

    tmpTokens.add(nextToken)

    return when (nextToken.type) {
      Recognizer.EOF -> {
        validate()
        nextToken
      }
      STRING -> {
        nextToken
      }
      RIGHT_BRACE -> {
        var startBrace = false
        var tokensInsideBrace = mutableListOf<Token>()
        for (tmpToken in tmpTokens) {
          if (startBrace) {
            if (tmpToken.type == METRIC_IDENTIFIER && !useAssignmentStatement) {
              throw NotAllowedMetricIdentifierInsideBraceException("Not allow ${tmpToken.text} METRIC_IDENTIFIER token inside brace")
            }
            tokensInsideBrace.add(tmpToken)
          }

          if (tmpToken.type == LEFT_BRACE) {
            startBrace = true
          }
        }

        var meetQueryOperator = false
        var vectorCount = 0
        for (tokenInsideBrace in tokensInsideBrace) {
          when(tokenInsideBrace.type) {
            IDENTIFIER -> {
              vectorCount++
            }
            STRING -> {
              vectorCount++
            }
            EQL_REGEX -> meetQueryOperator = true
            EQL -> meetQueryOperator = true
            NEQ -> meetQueryOperator = true
            NEQ_REGEX -> meetQueryOperator = true
          }
        }

        if (vectorCount % 2 != 0) {
          throw IllegalVectorPairException("")
        }

        if (vectorCount != 0 && !meetQueryOperator) {
          throw NotIncludeQueryOperatorInVectorMatchingException("Please check the query operator whether omit or not.")
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

    // It cannot be used alone unless it is a metric identifier.
    if (2 == tmpTokens.size) {
      when (tmpTokens[0].type) {
        EQL_REGEX, NEQ_REGEX -> throw NotAllowedSingleNonMetricIdentifierException("")
      }
    }

    if (0 < antlrErrorListener.errorCount) {
      throw UnknownTokenException("Detect antlr error during parsing $_input")
    }
  }

  class ErrorCountableANTLRErrorListener : BaseErrorListener() {

    private val log = LoggerFactory.getLogger(javaClass)

    var errorCount = 0

    override fun syntaxError(recognizer: Recognizer<*, *>?, offendingSymbol: Any?, line: Int, charPositionInLine: Int, msg: String?, e: RecognitionException?) {
      log.error("line $line:$charPositionInLine $msg")
      errorCount++
    }
  }
}
