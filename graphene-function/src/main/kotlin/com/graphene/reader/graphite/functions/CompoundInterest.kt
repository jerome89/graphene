package com.graphene.reader.graphite.functions

import com.graphene.reader.beans.TimeSeries
import com.graphene.reader.exceptions.EvaluationException
import com.graphene.reader.exceptions.InvalidArgumentException
import com.graphene.reader.graphite.Target
import com.graphene.reader.graphite.evaluation.TargetEvaluator
import java.util.*

class CompoundInterest(text: String?): GrapheneFunction(text, "compoundInterest") {
  @Throws(InvalidArgumentException::class)
  override fun checkArguments() {
  }

  @Throws(EvaluationException::class)
  override fun evaluate(evaluator: TargetEvaluator): List<TimeSeries> {
    val processedArguments: MutableList<TimeSeries> = ArrayList()
    for (target in arguments) {
      processedArguments.addAll(evaluator.eval(target as Target))
    }
    for (ts in processedArguments) {
      var currentValue = 1.0
      for (i in ts.values.indices) {
        if (Objects.nonNull(ts.values[i])) {
          val rate = 1.0 + ts.values[i] / 100.0
          ts.values[i] = (currentValue * rate - 1.0) * 100.0
          currentValue *= rate
        }
      }
    }
    return processedArguments
  }
}
