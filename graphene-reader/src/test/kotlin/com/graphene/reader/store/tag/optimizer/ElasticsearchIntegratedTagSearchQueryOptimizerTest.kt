package com.graphene.reader.store.tag.optimizer

import kotlin.test.assertEquals
import org.junit.jupiter.api.Test

internal class ElasticsearchIntegratedTagSearchQueryOptimizerTest {

  @Test
  internal fun `should optimize tag value search query when tagExpressions, tagKey and tagValue are given`() {
    // given
    val optimizer = ElasticsearchIntegratedTagSearchQueryOptimizer()

    // when
    val query = optimizer.optimize(TagSearchTarget(tagKey = "server", tagValue = "a", tagExpressions = arrayListOf("az=a", "dc=x") as List<String>))

    // then
    val expectedQuery = """
    {
      "bool" : {
        "filter" : [
          {
            "exists" : {
              "field" : "tags.server",
              "boost" : 1.0
            }
          },
          {
            "prefix" : {
              "tags.server" : {
                "value" : "a",
                "boost" : 1.0
              }
            }
          },
          {
            "terms" : {
              "tags.az" : [
                "a"
              ],
              "boost" : 1.0
            }
          },
          {
            "terms" : {
              "tags.dc" : [
                "x"
              ],
              "boost" : 1.0
            }
          }
        ],
        "adjust_pure_negative" : true,
        "boost" : 1.0
      }
    }"""
    assertEquals(expectedQuery.trimIndent(), query.toString().trimIndent())
  }

  @Test
  internal fun `should optimize tag value search query when tagExpressions, tagKey are given`() {
    // given
    val optimizer = ElasticsearchIntegratedTagSearchQueryOptimizer()

    // when
    val query = optimizer.optimize(TagSearchTarget(tagKey = "server", tagExpressions = arrayListOf("az=a", "dc=x") as List<String>))

    // then
    val expectedQuery = """
    {
      "bool" : {
        "filter" : [
          {
            "exists" : {
              "field" : "tags.server",
              "boost" : 1.0
            }
          },
          {
            "terms" : {
              "tags.az" : [
                "a"
              ],
              "boost" : 1.0
            }
          },
          {
            "terms" : {
              "tags.dc" : [
                "x"
              ],
              "boost" : 1.0
            }
          }
        ],
        "adjust_pure_negative" : true,
        "boost" : 1.0
      }
    }"""
    assertEquals(expectedQuery.trimIndent(), query.toString().trimIndent())
  }

  @Test
  internal fun `should optimize tag value search query when tagExpressions is given which has negate expression`() {
    // given
    val optimizer = ElasticsearchIntegratedTagSearchQueryOptimizer()

    // when
    val query = optimizer.optimize(TagSearchTarget(tagExpressions = arrayListOf("az=a", "dc!=x") as List<String>))

    // then
    val expectedQuery = """
    {
      "bool" : {
        "filter" : [
          {
            "terms" : {
              "tags.az" : [
                "a"
              ],
              "boost" : 1.0
            }
          }
        ],
        "must_not" : [
          {
            "terms" : {
              "tags.dc" : [
                "x"
              ],
              "boost" : 1.0
            }
          }
        ],
        "adjust_pure_negative" : true,
        "boost" : 1.0
      }
    }"""
    assertEquals(expectedQuery.trimIndent(), query.toString().trimIndent())
  }

  @Test
  internal fun `should optimize tag value search query when tagExpressions is given which has terms expression`() {
    // given
    val optimizer = ElasticsearchIntegratedTagSearchQueryOptimizer()

    // when
    val query = optimizer.optimize(TagSearchTarget(tagExpressions = arrayListOf("az=a", "dc={x,y}") as List<String>))

    // then
    val expectedQuery = """
    {
      "bool" : {
        "filter" : [
          {
            "terms" : {
              "tags.az" : [
                "a"
              ],
              "boost" : 1.0
            }
          },
          {
            "terms" : {
              "tags.dc" : [
                "x",
                "y"
              ],
              "boost" : 1.0
            }
          }
        ],
        "adjust_pure_negative" : true,
        "boost" : 1.0
      }
    }"""
    assertEquals(expectedQuery.trimIndent(), query.toString().trimIndent())
  }

  @Test
  internal fun `should optimize tag value search query when tagExpressions is given which has regex expression`() {
    // given
    val optimizer = ElasticsearchIntegratedTagSearchQueryOptimizer()

    // when
    val query = optimizer.optimize(TagSearchTarget(tagExpressions = arrayListOf("az=a", "dc=~{a,b}") as List<String>))

    // then
    val expectedQuery = """
    {
      "bool" : {
        "filter" : [
          {
            "terms" : {
              "tags.az" : [
                "a"
              ],
              "boost" : 1.0
            }
          },
          {
            "regexp" : {
              "tags.dc" : {
                "value" : "(a|b)",
                "flags_value" : 65535,
                "max_determinized_states" : 10000,
                "boost" : 1.0
              }
            }
          }
        ],
        "adjust_pure_negative" : true,
        "boost" : 1.0
      }
    }"""
    assertEquals(expectedQuery.trimIndent(), query.toString().trimIndent())
  }
}
