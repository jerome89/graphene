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
  internal fun `should tokenize by prometheus lex rule given input text`() {
    // given
    val table = table(
      headers("input", "expectedTokens"),
      row(
        ",",
        listOf(expectedToken(PrometheusLexer.COMMA, 0, 0, ","))
      ),
      row(
        "{}",
        listOf(
          expectedToken(PrometheusLexer.LEFT_BRACE, 0, 0, "{"),
          expectedToken(PrometheusLexer.RIGHT_BRACE, 1, 1, "}")
        )
      )
    )

    // then
    table.forAll { input, expectedTokens ->
      val prometheusLexer = PrometheusLexer(CharStreams.fromString(input))
      val actualTokens = makeActualTokens(prometheusLexer)

      for ((index, actualToken) in actualTokens.iterator().withIndex()) {
        actualToken.type shouldBe expectedTokens[index].type
        actualToken.startIndex shouldBe expectedTokens[index].startIndex
        actualToken.stopIndex shouldBe expectedTokens[index].stopIndex
        actualToken.text shouldBe expectedTokens[index].text
      }
    }
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
