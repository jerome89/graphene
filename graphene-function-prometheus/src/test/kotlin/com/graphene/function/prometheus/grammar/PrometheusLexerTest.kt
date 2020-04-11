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
import java.util.Objects

class PrometheusLexerTest {

  @Test
  internal fun `should tokenize common lex by prometheus rule for given input text`() {
    // given
    val table = table(
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
    table.forAll { input, expectedTokens ->
      assertToken(input, expectedTokens)
    }
  }

  @Test
  internal fun `should tokenize number lex by prometheus rule for given input text`() {
    // given
    val table = table(
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
    table.forAll { input, expectedTokens ->
      assertToken(input, expectedTokens)
    }
  }

  @Test
  internal fun `should tokenize strings lex by prometheus rule for given input text`() {
    // given
    val table = table(
      headers("input", "expectedTokens"),
      row(
        "\"test\\tsequence\"",
        listOf(expectedToken(PrometheusLexer.STRING, 0, 15, """"test\tsequence""""))
      ),
      row(
        "\"test\\\\.expression\"",
        listOf(expectedToken(PrometheusLexer.STRING, 0, 18, """"test\\.expression""""))
      ),
      row(
        "\"test\\.expression\"",
        listOf(expectedToken(PrometheusLexer.STRING, 0, 17, """"test\.expression""""))
      ),
      row(
        "`test\\.expression`",
        listOf(expectedToken(PrometheusLexer.STRING, 0, 17, """`test\.expression`"""))
      )
//      ,
//      row(
//        ".٩",
//        listOf(expectedToken(PrometheusLexer.STRING, 0, 17, """`test\.expression`"""))
//      )
    )

    // then
    table.forAll { input, expectedTokens ->
      assertToken(input, expectedTokens)
    }
  }

  @Test
  internal fun `should tokenize durations lex by prometheus rule for given input text`() {
    // given
    val table = table(
      headers("input", "expectedTokens"),
      row(
        "5s",
        listOf(expectedToken(PrometheusLexer.DURATION, 0, 1, "5s"))
      ),
      row(
        "123m",
        listOf(expectedToken(PrometheusLexer.DURATION, 0, 3, "123m"))
      ),
      row(
        "1h",
        listOf(expectedToken(PrometheusLexer.DURATION, 0, 1, "1h"))
      ),
      row(
        "3w",
        listOf(expectedToken(PrometheusLexer.DURATION, 0, 1, "3w"))
      ),
      row(
        "1y",
        listOf(expectedToken(PrometheusLexer.DURATION, 0, 1, "1y"))
      )
    )

    // then
    table.forAll { input, expectedTokens ->
      assertToken(input, expectedTokens)
    }
  }

  @Test
  internal fun `should tokenize identifiers lex by prometheus rule for given input text`() {
    // given
    val table = table(
      headers("input", "expectedTokens"),
      row(
        "abc",
        listOf(expectedToken(PrometheusLexer.IDENTIFIER, 0, 2, "abc"))
      ),
      row(
        "a:bc",
        listOf(expectedToken(PrometheusLexer.METRIC_IDENTIFIER, 0, 3, "a:bc"))
      ),
      row(
        "abc d",
        listOf(
          expectedToken(PrometheusLexer.IDENTIFIER, 0, 2, "abc"),
          expectedToken(PrometheusLexer.IDENTIFIER, 4, 4, "d")
        )
      ),
      row(
        ":bc",
        listOf(expectedToken(PrometheusLexer.METRIC_IDENTIFIER, 0, 2, ":bc"))
      )
//      ,
//      row(
//        "0a:bc",
//        listOf(expectedToken(PrometheusLexer.DURATION, 0, 1, "1y"))
//      )
    )

    // then
    table.forAll { input, expectedTokens ->
      assertToken(input, expectedTokens)
    }
  }

  @Test
  internal fun `should tokenize comments lex by prometheus rule for given input text`() {
    // given
    val table = table(
      headers("input", "expectedTokens"),
      row(
        "# some comment",
        listOf(expectedToken(PrometheusLexer.COMMENT, 0, 13, "# some comment"))
      ),
      row(
        "5 # 1+1\\n5",
        listOf(
          expectedToken(PrometheusLexer.NUMBER, 0, 0, "5"),
          expectedToken(PrometheusLexer.COMMENT, 2, 6, "# 1+1"),
          // Please fix me below string type
          expectedToken(PrometheusLexer.STRING, 7, 8, "\\n"),
          expectedToken(PrometheusLexer.NUMBER, 9, 9, "5")
        )
      )
    )

    // then
    table.forAll { input, expectedTokens ->
      assertToken(input, expectedTokens)
    }
  }

  @Test
  internal fun `should tokenize operators lex by prometheus rule for given input text`() {
    // given
    val table = table(
      headers("input", "expectedTokens"),
      row(
        "=",
        listOf(expectedToken(PrometheusLexer.ASSIGN, 0, 0, "="))
      ),
      // Inside braces equality is a single '=' character.
      row(
        "{=}",
        listOf(
          expectedToken(PrometheusLexer.LEFT_BRACE, 0, 0, "{"),
          expectedToken(PrometheusLexer.EQL, 1, 1, "="),
          expectedToken(PrometheusLexer.RIGHT_BRACE, 2, 2, "}")
        )
      ),
      row(
        "==",
        listOf(expectedToken(PrometheusLexer.EQL, 0, 1, "=="))
      ),
      row(
        "!=",
        listOf(expectedToken(PrometheusLexer.NEQ, 0, 1, "!="))
      ),
      row(
        "<",
        listOf(expectedToken(PrometheusLexer.LSS, 0, 0, "<"))
      ),
      row(
        ">",
        listOf(expectedToken(PrometheusLexer.GTR, 0, 0, ">"))
      ),
      row(
        ">=",
        listOf(expectedToken(PrometheusLexer.GTE, 0, 1, ">="))
      ),
      row(
        "<=",
        listOf(expectedToken(PrometheusLexer.LTE, 0, 1, "<="))
      ),
      row(
        "+",
        listOf(expectedToken(PrometheusLexer.ADD, 0, 0, "+"))
      ),
      row(
        "-",
        listOf(expectedToken(PrometheusLexer.SUB, 0, 0, "-"))
      ),
      row(
        "*",
        listOf(expectedToken(PrometheusLexer.MUL, 0, 0, "*"))
      ),
      row(
        "/",
        listOf(expectedToken(PrometheusLexer.DIV, 0, 0, "/"))
      ),
      row(
        "^",
        listOf(expectedToken(PrometheusLexer.POW, 0, 0, "^"))
      ),
      row(
        "%",
        listOf(expectedToken(PrometheusLexer.MOD, 0, 0, "%"))
      ),
      row(
        "AND",
        listOf(expectedToken(PrometheusLexer.LAND, 0, 2, "AND"))
      ),
      row(
        "or",
        listOf(expectedToken(PrometheusLexer.LOR, 0, 1, "or"))
      ),
      row(
        "unless",
        listOf(expectedToken(PrometheusLexer.LUNLESS, 0, 5, "unless"))
      )

    )

    // then
    table.forAll { input, expectedTokens ->
      assertToken(input, expectedTokens)
    }
  }

  @Test
  internal fun `should tokenize aggregators lex by prometheus rule for given input text`() {
    // given
    val table = table(
      headers("input", "expectedTokens"),
      row(
        "sum",
        listOf(expectedToken(PrometheusLexer.SUM, 0, 2, "sum"))
      ),
      row(
        "AVG",
        listOf(expectedToken(PrometheusLexer.AVG, 0, 2, "AVG"))
      ),
      row(
        "MAX",
        listOf(expectedToken(PrometheusLexer.MAX, 0, 2, "MAX"))
      ),
      row(
        "min",
        listOf(expectedToken(PrometheusLexer.MIN, 0, 2, "min"))
      ),
      row(
        "count",
        listOf(expectedToken(PrometheusLexer.COUNT, 0, 4, "count"))
      ),
      row(
        "stdvar",
        listOf(expectedToken(PrometheusLexer.STDVAR, 0, 5, "stdvar"))
      ),
      row(
        "stddev",
        listOf(expectedToken(PrometheusLexer.STDDEV, 0, 5, "stddev"))
      )
    )

    // then
    table.forAll { input, expectedTokens ->
      assertToken(input, expectedTokens)
    }
  }

  @Test
  internal fun `should tokenize keywords lex by prometheus rule for given input text`() {
    // given
    val table = table(
      headers("input", "expectedTokens"),
      row(
        "offset",
        listOf(expectedToken(PrometheusLexer.OFFSET, 0, 5, "offset"))
      ),
      row(
        "by",
        listOf(expectedToken(PrometheusLexer.BY, 0, 1, "by"))
      ),
      row(
        "without",
        listOf(expectedToken(PrometheusLexer.WITHOUT, 0, 6, "without"))
      ),
      row(
        "on",
        listOf(expectedToken(PrometheusLexer.ON, 0, 1, "on"))
      ),
      row(
        "ignoring",
        listOf(expectedToken(PrometheusLexer.IGNORING, 0, 7, "ignoring"))
      ),
      row(
        "group_left",
        listOf(expectedToken(PrometheusLexer.GROUP_LEFT, 0, 9, "group_left"))
      ),
      row(
        "group_right",
        listOf(expectedToken(PrometheusLexer.GROUP_RIGHT, 0, 10, "group_right"))
      ),
      row(
        "bool",
        listOf(expectedToken(PrometheusLexer.BOOL, 0, 3, "bool"))
      )
    )

    // then
    table.forAll { input, expectedTokens ->
      assertToken(input, expectedTokens)
    }
  }

  @Test
  internal fun `should tokenize selectors lex by prometheus rule for given input text`() {
    // given
    val table = table(
      headers("input", "expectedTokens"),
      row(
        "台北",
        fail()
      ),
//      row(
//        "{台北='a'}",
//        fail()
//      ),
//      row(
//        "{0a='a'}",
//        fail()
//      ),
      row(
        "{foo='bar'}",
        listOf(
          expectedToken(PrometheusLexer.LEFT_BRACE, 0, 0, "{"),
          expectedToken(PrometheusLexer.IDENTIFIER, 1, 3, "foo"),
          expectedToken(PrometheusLexer.EQL, 4, 4, "="),
          expectedToken(PrometheusLexer.STRING, 5, 9, "'bar'"),
          expectedToken(PrometheusLexer.RIGHT_BRACE, 10, 10, "}")
        )
      ),
      row(
        """{foo="bar\"bar"}""",
        listOf(
          expectedToken(PrometheusLexer.LEFT_BRACE, 0, 0, "{"),
          expectedToken(PrometheusLexer.IDENTIFIER, 1, 3, "foo"),
          expectedToken(PrometheusLexer.EQL, 4, 4, "="),
          expectedToken(PrometheusLexer.STRING, 5, 14, """"bar\"bar""""),
          expectedToken(PrometheusLexer.RIGHT_BRACE, 15, 15, "}")
        )
      ),
      row(
        """{NaN	!= "bar" }""",
        listOf(
          expectedToken(PrometheusLexer.LEFT_BRACE, 0, 0, "{"),
          expectedToken(PrometheusLexer.IDENTIFIER, 1, 3, "NaN"),
          expectedToken(PrometheusLexer.NEQ, 5, 6, "!="),
          expectedToken(PrometheusLexer.STRING, 8, 12, """"bar""""),
          expectedToken(PrometheusLexer.RIGHT_BRACE, 14, 14, "}")
        )
      ),
      row(
        """{NaN	!= "bar" }""",
        listOf(
          expectedToken(PrometheusLexer.LEFT_BRACE, 0, 0, "{"),
          expectedToken(PrometheusLexer.IDENTIFIER, 1, 3, "NaN"),
          expectedToken(PrometheusLexer.NEQ, 5, 6, "!="),
          expectedToken(PrometheusLexer.STRING, 8, 12, """"bar""""),
          expectedToken(PrometheusLexer.RIGHT_BRACE, 14, 14, "}")
        )
      ),
      row(
        """{alert=~"bar" }""",
        listOf(
          expectedToken(PrometheusLexer.LEFT_BRACE, 0, 0, "{"),
          expectedToken(PrometheusLexer.IDENTIFIER, 1, 5, "alert"),
          expectedToken(PrometheusLexer.EQL_REGEX, 6, 7, "=~"),
          expectedToken(PrometheusLexer.STRING, 8, 12, """"bar""""),
          expectedToken(PrometheusLexer.RIGHT_BRACE, 14, 14, "}")
        )
      ),
      row(
        """{on!~"bar"}""",
        listOf(
          expectedToken(PrometheusLexer.LEFT_BRACE, 0, 0, "{"),
          expectedToken(PrometheusLexer.IDENTIFIER, 1, 2, "on"),
          expectedToken(PrometheusLexer.NEQ_REGEX, 3, 4, "!~"),
          expectedToken(PrometheusLexer.STRING, 5, 9, """"bar""""),
          expectedToken(PrometheusLexer.RIGHT_BRACE, 10, 10, "}")
        )
      )
//      ,
//      row(
//        """{alert!#"bar"}""",
//        fail()
//      ),
//      row(
//        """{foo:a="bar"}""",
//        fail()
//      )
    )

    // then
    table.forAll { input, expectedTokens ->
      assertToken(input, expectedTokens)
    }
  }

//  @Test
//  internal fun `should fail if mismatched syntax in the input`() {
//    // given
//    val table = table(
//      headers("input", "expectedTokens"),
//      row(
//        "=~",
//        fail()
//      ),
//      row(
//        "!~",
//        fail()
//      ),
//      row(
//        "!(",
//        fail()
//      ),
//      row(
//        "1a",
//        fail()
//      )
//    )
//
//    // then
//    table.forAll { input, expectedTokens ->
//      assertToken(input, expectedTokens)
//    }
//  }

//  @Test
//  internal fun `should fail if mismatched parentheses in the input`() {
//    // given
//    val table = table(
//      headers("input", "expectedTokens"),
//      row(
//        "(",
//        fail()
//      ),
//      row(
//        "())",
//        fail()
//      ),
//      row(
//        "(()",
//        fail()
//      ),
//      row(
//        "{",
//        fail()
//      ),
//      row(
//        "}",
//        fail()
//      ),
//      row(
//        "{{",
//        fail()
//      ),
//      row(
//        "{{}}",
//        fail()
//      ),
//      row(
//        "[",
//        fail()
//      ),
//      row(
//        "[[",
//        fail()
//      ),
//      row(
//        "[]]",
//        fail()
//      ),
//      row(
//        "[[]]",
//        fail()
//      ),
//      row(
//        "]",
//        fail()
//      )
//    )
//
//    // then
//    table.forAll { input, expectedTokens ->
//      assertToken(input, expectedTokens)
//    }
//  }

//  @Test
//  internal fun `should fail if encoding issue in the input`() {
//    // given
//    val table = table(
//      headers("input", "expectedTokens"),
//      row(
//        """\"\xff\"""",
//        fail()
//      ),
//      row(
//        """\xff""",
//        fail()
//      )
//    )
//
//    // then
//    table.forAll { input, expectedTokens ->
//      assertToken(input, expectedTokens)
//    }
//  }

  private fun assertToken(input: String, expectedTokens: List<Token>?) {
    val prometheusLexer = PrometheusLexer(CharStreams.fromString(input))
    val actualTokens = makeActualTokens(prometheusLexer)

    return if (Objects.isNull(expectedTokens)) { actualTokens.size shouldBe 0 }
    else {
      for ((index, actualToken) in actualTokens.iterator().withIndex()) {
        actualToken.type shouldBe expectedTokens!![index].type
        actualToken.startIndex shouldBe expectedTokens[index].startIndex
        actualToken.stopIndex shouldBe expectedTokens[index].stopIndex
        actualToken.text shouldBe expectedTokens[index].text
      }

      actualTokens.size shouldBe expectedTokens!!.size
    }
  }

  private fun fail(): List<Token>? = null

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
