package com.graphene.function.prometheus.grammar

import org.antlr.v4.runtime.ANTLRErrorListener
import org.antlr.v4.runtime.CharStream
import org.antlr.v4.runtime.Parser
import org.antlr.v4.runtime.RecognitionException
import org.antlr.v4.runtime.Recognizer
import org.antlr.v4.runtime.Token
import org.antlr.v4.runtime.atn.ATNConfigSet
import org.antlr.v4.runtime.dfa.DFA
import java.util.BitSet

class PrometheusValidatorLexer(input: CharStream) : PrometheusLexer(input) {

  var eqlCount: Int = 0
  var eqlRegexCount: Int = 0
  var neqCount: Int = 0
  var neqRegexCount: Int = 0

  var tmpTokens: MutableList<Token> = mutableListOf()

  var errorCount: Int = 0

  override fun addErrorListener(listener: ANTLRErrorListener?) {
    super.addErrorListener(object: ANTLRErrorListener {
      override fun reportAttemptingFullContext(recognizer: Parser?, dfa: DFA?, startIndex: Int, stopIndex: Int, conflictingAlts: BitSet?, configs: ATNConfigSet?) {
        println("reportAttemptingFullContext")
      }

      override fun syntaxError(recognizer: Recognizer<*, *>?, offendingSymbol: Any?, line: Int, charPositionInLine: Int, msg: String?, e: RecognitionException?) {
        println("syntaxError")
        errorCount++
      }

      override fun reportAmbiguity(recognizer: Parser?, dfa: DFA?, startIndex: Int, stopIndex: Int, exact: Boolean, ambigAlts: BitSet?, configs: ATNConfigSet?) {
        println("reportAmbiguity")
      }

      override fun reportContextSensitivity(recognizer: Parser?, dfa: DFA?, startIndex: Int, stopIndex: Int, prediction: Int, configs: ATNConfigSet?) {
        println("reportContextSensitivity")
      }
    })
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
            if (tmpToken.type == METRIC_IDENTIFIER) {
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

    if (0 < errorCount) {
      throw UnknownTokenException("")
    }
  }
}
