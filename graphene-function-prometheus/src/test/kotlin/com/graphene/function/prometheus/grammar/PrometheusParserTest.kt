//package com.graphene.function.prometheus.grammar
//
//import arrow.core.left
//import arrow.core.right
//import com.graphene.function.prometheus.PrometheusVisitorImpl
//import com.intellij.testFramework.TestDataPath
//import org.antlr.v4.runtime.ANTLRInputStream
//import org.antlr.v4.runtime.CommonTokenStream
//import org.junit.Test
//import org.junit.runner.RunWith
//import java.io.File
//
//@TestDataPath("\$PROJECT_ROOT/testData/")
//@RunWith(com.intellij.testFramework.Parameterized::class)
//class PrometheusParserTest {
//
//  @org.junit.runners.Parameterized.Parameter
//  lateinit var testFilePath: String
//
//  companion object {
//    @org.junit.runners.Parameterized.Parameters(name = "{0}")
//    @JvmStatic
//    fun getTestFiles() = emptyList<Array<Any>>()
//
//    @com.intellij.testFramework.Parameterized.Parameters(name = "{0}")
//    @JvmStatic
//    fun getTestFiles(klass: Class<*>) = File("./testData").let { testsDir ->
//      testsDir.walkTopDown().filter { it.extension == "txt" }.map {
//        arrayOf(it.relativeTo(testsDir).path.replace("/", "$"))
//      }.toList()
//    }
//  }
//
//  @Test
//  fun `should test prometheus grammar`() {
//    val lexer = PrometheusLexer(ANTLRInputStream("answer=1+2*2+3"))
//    val tokens = CommonTokenStream(lexer)
//    val parser = PrometheusParser(tokens)
//    val parseInfo = parser.start()
//
//    if (parseInfo is PrometheusParser.AssignmentMathematicalFormulaContext) {
//
//    }
//    //TODO https//tomassetti.me/building-and-testing-a-parser-with-antlr-and-kotlin/
////
////    when(parseInfo) {
////      is PrometheusParser.AssignmentMathFormulaExprContext -> parseInfo.left()
////      is PrometheusParser.AssignmentMathematicalFormulaContext -> println("")
////      else -> println("else")
////    }
//
//
//    PrometheusVisitorImpl().visit(parseInfo)
////    PrometheusParser()
//    println(parseInfo)
//  }
//}
