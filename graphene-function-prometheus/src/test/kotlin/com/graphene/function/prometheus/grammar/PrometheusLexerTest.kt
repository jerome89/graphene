package com.graphene.function.prometheus.grammar

import io.kotlintest.shouldBe
import io.kotlintest.tables.forAll
import io.kotlintest.tables.headers
import io.kotlintest.tables.row
import io.kotlintest.tables.table
import io.mockk.every
import io.mockk.mockk
import org.antlr.v4.runtime.*
import org.junit.jupiter.api.Test

class PrometheusLexerTest {

  @Test
  internal fun `should tokenize common lex by prometheus rule for given input text`() {
    // given
    val commonTable = table(
      headers("input", "expectedTokens"),
      row(
        ",",
        listOf(expectedToken(PrometheusLexer.COMMA, 0, 0, ","))
      ),
      row(
        "()",
        listOf(
          expectedToken(PrometheusLexer.LEFT_PAREN, 0, 0, "("),
          expectedToken(PrometheusLexer.RIGHT_PAREN, 1, 1, ")")
        )
      ),
      row(
        "{}",
        listOf(
          expectedToken(PrometheusLexer.LEFT_BRACE, 0, 0, "{"),
          expectedToken(PrometheusLexer.RIGHT_BRACE, 1, 1, "}")
        )
      ),
      row(
        "[5m]",
        listOf(
          expectedToken(PrometheusLexer.LEFT_BRACKET, 0, 0, "["),
          expectedToken(PrometheusLexer.DURATION, 1, 2, "5m"),
          expectedToken(PrometheusLexer.RIGHT_BRACKET, 3, 3, "]")
        )
      ),
      row(
        "[ 5m]",
        listOf(
          expectedToken(PrometheusLexer.LEFT_BRACKET, 0, 0, "["),
          expectedToken(PrometheusLexer.DURATION, 2, 3, "5m"),
          expectedToken(PrometheusLexer.RIGHT_BRACKET, 4, 4, "]")
        )
      ),
      row(
        "[  5m]",
        listOf(
          expectedToken(PrometheusLexer.LEFT_BRACKET, 0, 0, "["),
          expectedToken(PrometheusLexer.DURATION, 3, 4, "5m"),
          expectedToken(PrometheusLexer.RIGHT_BRACKET, 5, 5, "]")
        )
      ),
      row(
        "[  5m ]",
        listOf(
          expectedToken(PrometheusLexer.LEFT_BRACKET, 0, 0, "["),
          expectedToken(PrometheusLexer.DURATION, 3, 4, "5m"),
          expectedToken(PrometheusLexer.RIGHT_BRACKET, 6, 6, "]")
        )
      ),
      row(
        "\r\n\r",
        listOf()
      )
    )

    // then
    commonTable.forAll { input, expectedTokens ->
      assertToken(input, expectedTokens)
    }
  }

  @Test
  internal fun `should tokenize number lex by prometheus rule for given input text`() {
    // given
    val numberTable = table(
      headers("input", "expectedTokens"),
      row(
        "1",
        listOf(expectedToken(PrometheusLexer.NUMBER, 0, 0, "1"))
      ),
      row(
        "4.23",
        listOf(
          expectedToken(PrometheusLexer.NUMBER, 0, 3, "4.23")
        )
      ),
      row(
        ".3",
        listOf(
          expectedToken(PrometheusLexer.NUMBER, 0, 1, ".3")
        )
      ),
      row(
        "5.",
        listOf(
          expectedToken(PrometheusLexer.NUMBER, 0, 1, "5.")
        )
      ),
      row(
        "NaN",
        listOf(
          expectedToken(PrometheusLexer.NUMBER, 0, 2, "NaN")
        )
      ),
      row(
        "nAN",
        listOf(
          expectedToken(PrometheusLexer.NUMBER, 0, 2, "nAN")
        )
      ),
      row(
        "NaN 123",
        listOf(
          expectedToken(PrometheusLexer.NUMBER, 0, 2, "NaN"),
          expectedToken(PrometheusLexer.NUMBER, 4, 6, "123")
        )
      ),
      row(
        "NaN123",
        listOf(
          expectedToken(PrometheusLexer.IDENTIFIER, 0, 5, "NaN123")
        )
      ),
      row(
        "iNf",
        listOf(
          expectedToken(PrometheusLexer.NUMBER, 0, 2, "iNf")
        )
      ),
      row(
        "Inf",
        listOf(
          expectedToken(PrometheusLexer.NUMBER, 0, 2, "Inf")
        )
      ),
      row(
        "+Inf",
        listOf(
          expectedToken(PrometheusLexer.ADD, 0, 0, "+"),
          expectedToken(PrometheusLexer.NUMBER, 1, 3, "Inf")
        )
      ),
      row(
        "+Inf 123",
        listOf(
          expectedToken(PrometheusLexer.ADD, 0, 0, "+"),
          expectedToken(PrometheusLexer.NUMBER, 1, 3, "Inf"),
          expectedToken(PrometheusLexer.NUMBER, 5, 7, "123")
        )
      ),
      row(
        "-Inf",
        listOf(
          expectedToken(PrometheusLexer.SUB, 0, 0, "-"),
          expectedToken(PrometheusLexer.NUMBER, 1, 3, "Inf")
        )
      ),
      row(
        "Infoo",
        listOf(
          expectedToken(PrometheusLexer.IDENTIFIER, 0, 4, "Infoo")
        )
      ),
      row(
        "-Infoo",
        listOf(
          expectedToken(PrometheusLexer.SUB, 0, 0, "-"),
          expectedToken(PrometheusLexer.IDENTIFIER, 1, 5, "Infoo")
        )
      ),
      row(
        "-Inf 123",
        listOf(
          expectedToken(PrometheusLexer.SUB, 0, 0, "-"),
          expectedToken(PrometheusLexer.NUMBER, 1, 3, "Inf"),
          expectedToken(PrometheusLexer.NUMBER, 5, 7, "123")
        )
      )
//      row(
//        "0x123",
//        listOf(
//          expectedToken(PrometheusLexer.NUMBER, 0, 5, "0x123")
//        )
//      )
    )

    // then
    numberTable.forAll { input, expectedTokens ->
      assertToken(input, expectedTokens)
    }
  }

  private fun assertToken(input: String, expectedTokens: List<Token>) {
    val prometheusLexer = PrometheusLexer(CharStreams.fromString(input))
    val actualTokens = makeActualTokens(prometheusLexer)

    for ((index, actualToken) in actualTokens.iterator().withIndex()) {
      actualToken.type shouldBe expectedTokens[index].type
      actualToken.startIndex shouldBe expectedTokens[index].startIndex
      actualToken.stopIndex shouldBe expectedTokens[index].stopIndex
      actualToken.text shouldBe expectedTokens[index].text
    }

    actualTokens.size shouldBe expectedTokens.size
  }

  private fun makeActualTokens(prometheusLexer: PrometheusLexer): MutableList<Token> {
    val actualTokens = mutableListOf<Token>()

    while (true) {
      val nextToken = prometheusLexer.nextToken()

      if (nextToken.type == Token.EOF) {
        break
      }

      actualTokens.add(nextToken)
    }
    return actualTokens
  }

  private fun expectedToken(type: Int, startIndex: Int, stopIndex: Int, text: String): Token {
    return mockk<Token>().also {
      every { it.type } answers { type }
      every { it.startIndex } answers { startIndex }
      every { it.stopIndex } answers { stopIndex }
      every { it.text } answers { text }
    }
  }

}
