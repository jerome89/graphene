package com.graphene.reader.handler.prometheus

import com.google.common.base.Joiner
import com.graphene.common.rule.GrapheneRules.SpecialChar
import com.graphene.reader.beans.TimeSeries
import com.graphene.reader.controller.prometheus.request.QueryRangeRequest
import com.graphene.reader.controller.prometheus.response.MetricResponse
import com.graphene.reader.controller.prometheus.response.QueryRangeResponse
import com.graphene.reader.controller.prometheus.response.QueryRangeResponseData
import com.graphene.reader.graphite.Target
import com.graphene.reader.graphite.evaluation.EvaluationContext
import com.graphene.reader.graphite.evaluation.TargetEvaluator
import com.graphene.reader.graphite.evaluation.TargetVisitor
import com.graphene.reader.graphite.grammar.GraphiteLexer
import com.graphene.reader.graphite.grammar.GraphiteParser
import com.graphene.reader.service.index.KeySearchHandler
import com.graphene.reader.service.metric.DataFetchHandler
import com.graphene.reader.utils.HumanValueFormatter
import java.util.Objects
import java.util.TreeMap
import org.antlr.v4.runtime.ANTLRInputStream
import org.antlr.v4.runtime.CommonTokenStream
import org.antlr.v4.runtime.misc.ParseCancellationException
import org.apache.commons.lang3.StringUtils
import org.springframework.stereotype.Component

@Component
class PrometheusRenderHandler(
  dataFetchHandler: DataFetchHandler,
  keySearchHandler: KeySearchHandler
) {

  private var evaluator: TargetEvaluator = TargetEvaluator(dataFetchHandler, keySearchHandler)

  fun handle(queryRangeRequest: QueryRangeRequest): QueryRangeResponse {
    val query = queryRangeRequest.query
    val targetString = convertPlainQuery(query)
    val grapheneTargets = getGrapheneTargets(queryRangeRequest, targetString)
    return createResponse(getTimeSeriesList(grapheneTargets))
  }

  fun getTimeSeriesList(targets: List<Target>): List<TimeSeries> {
    val results = mutableListOf<TimeSeries>()
    for (target in targets) {
      results.addAll(evaluator.eval(target))
    }
    return results
  }

  fun createResponse(timeSeriesList: List<TimeSeries>): QueryRangeResponse {
    val queryRangeResponseData = QueryRangeResponseData("matrix", mutableListOf())
    for (ts in timeSeriesList) {
      var timestamp = ts.from
      val metricResponse = MetricResponse(mutableMapOf(), mutableListOf())
      for ((key, value) in ts.tags) {
        if (key == "@name") {
          metricResponse.metric["__name__"] = value
        } else {
          metricResponse.metric[key] = value
        }
      }
      for (value in ts.values) {
        if (Objects.nonNull(value)) {
          metricResponse.values.add(mutableListOf(timestamp, value.toString()))
        }
        timestamp += ts.step
      }
      queryRangeResponseData.result.add(metricResponse)
    }
    return QueryRangeResponse("success", queryRangeResponseData)
  }

  fun getGrapheneTargets(queryRangeRequest: QueryRangeRequest, targetString: String): List<Target> {
    val ctx = EvaluationContext(HumanValueFormatter())
    val targets = mutableListOf<Target>()
    val parser = GraphiteParser(CommonTokenStream(GraphiteLexer(ANTLRInputStream(targetString))))
    val tree = parser.expression()
    try {
      targets.add(TargetVisitor("NONE", queryRangeRequest.start, queryRangeRequest.end, ctx).visit(tree))
    } catch (e: ParseCancellationException) {
      print(e.message)
    }
    return targets
  }

  fun convertPlainQuery(query: String): String {
    val tags = TreeMap<String, String>()
    val tmp = StringBuilder()
    val tagExpList = mutableListOf<String>()
    var tmpTagKey = ""
    val result = StringBuilder("seriesByTag(")
    for (char in query) {
      if (char == SpecialChar.BRACE_OPEN) {
        tags["@name"] = tmp.toString()
        tmp.clear()
      } else if (char == SpecialChar.EQUAL) {
        tmpTagKey = tmp.toString()
        tmp.clear()
      } else if (char == SpecialChar.COMMA || char == SpecialChar.BRACE_CLOSE) {
        tags[tmpTagKey] = tmp.toString()
        tmp.clear()
      } else if (char == SpecialChar.WHITESPACE) {
        continue
      } else if (char != SpecialChar.DOUBLE_QUOTE) {
        tmp.append(char)
      }
    }
    if (tmp.isNotEmpty() && !tags.containsKey("@name")) {
      tags["@name"] = tmp.toString()
    }

    for ((key, value) in tags) {
      if (StringUtils.isNotBlank(key) && StringUtils.isNotBlank(value)) {
        tagExpList.add("\'$key=$value\'")
      }
    }

    result.append(Joiner.on(",").join(tagExpList)).append(")")
    return result.toString()
  }
}
