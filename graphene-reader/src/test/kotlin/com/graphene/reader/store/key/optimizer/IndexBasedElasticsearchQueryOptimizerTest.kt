package com.graphene.reader.store.key.optimizer

import kotlin.test.assertEquals
import org.junit.jupiter.api.Test

internal class IndexBasedElasticsearchQueryOptimizerTest {

  @Test
  internal fun `should optimize leaf query with depth`() {
    // given
    val optimizer = IndexBasedElasticsearchQueryOptimizer()

    // when
    val query = optimizer.optimizeLeafQuery("servers.server1.cpu.usage")

    // then
    val expectedQuery = """
    {
      "bool" : {
        "filter" : [
          {
            "term" : {
              "0" : {
                "value" : "servers",
                "boost" : 1.0
              }
            }
          },
          {
            "term" : {
              "1" : {
                "value" : "server1",
                "boost" : 1.0
              }
            }
          },
          {
            "term" : {
              "2" : {
                "value" : "cpu",
                "boost" : 1.0
              }
            }
          },
          {
            "term" : {
              "3" : {
                "value" : "usage",
                "boost" : 1.0
              }
            }
          },
          {
            "term" : {
              "depth" : {
                "value" : 4,
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

  @Test
  internal fun `should optimize branch term query each index`() {
    // given
    val optimizer = IndexBasedElasticsearchQueryOptimizer()

    // when
    val query = optimizer.optimizeBranchQuery("servers.server1.cpu.usage")

    // then
    val expectedQuery = """
    {
      "bool" : {
        "filter" : [
          {
            "term" : {
              "0" : {
                "value" : "servers",
                "boost" : 1.0
              }
            }
          },
          {
            "term" : {
              "1" : {
                "value" : "server1",
                "boost" : 1.0
              }
            }
          },
          {
            "term" : {
              "2" : {
                "value" : "cpu",
                "boost" : 1.0
              }
            }
          },
          {
            "term" : {
              "3" : {
                "value" : "usage",
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

  @Test
  internal fun `should optimize excluded specific index which is wildcard path index`() {
    // given
    val optimizer = IndexBasedElasticsearchQueryOptimizer()

    // when
    val query = optimizer.optimizeBranchQuery("servers.*")

    // then
    val expectedQuery = """
    {
      "bool" : {
        "filter" : [
          {
            "term" : {
              "0" : {
                "value" : "servers",
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

  @Test
  internal fun `should optimize prefix term query if have suffix wildcard`() {
    // given
    val optimizer = IndexBasedElasticsearchQueryOptimizer()

    // when
    val query = optimizer.optimizeBranchQuery("servers.server*")

    // then
    val expectedQuery = """
    {
      "bool" : {
        "filter" : [
          {
            "term" : {
              "0" : {
                "value" : "servers",
                "boost" : 1.0
              }
            }
          },
          {
            "prefix" : {
              "1" : {
                "value" : "server*",
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

  @Test
  internal fun `should optimize terms query if have or condition`() {
    // given
    val optimizer = IndexBasedElasticsearchQueryOptimizer()

    // when
    val query = optimizer.optimizeBranchQuery("servers.{server1, server2}.cpu.usage")

    // then
    val expectedQuery = """
    {
      "bool" : {
        "filter" : [
          {
            "term" : {
              "0" : {
                "value" : "servers",
                "boost" : 1.0
              }
            }
          },
          {
            "terms" : {
              "1" : [
                "server1",
                "server2"
              ],
              "boost" : 1.0
            }
          },
          {
            "term" : {
              "2" : {
                "value" : "cpu",
                "boost" : 1.0
              }
            }
          },
          {
            "term" : {
              "3" : {
                "value" : "usage",
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

  @Test
  internal fun `should optimize regex query if have a regex pattern`() {
    // given
    val optimizer = IndexBasedElasticsearchQueryOptimizer()

    // when
    val query = optimizer.optimizeBranchQuery("servers.{server*}.cpu.usage")

    // then
    val expectedQuery = """
    {
      "bool" : {
        "filter" : [
          {
            "term" : {
              "0" : {
                "value" : "servers",
                "boost" : 1.0
              }
            }
          },
          {
            "regexp" : {
              "1" : {
                "value" : "(server[^\\.]*)",
                "flags_value" : 65535,
                "max_determinized_states" : 10000,
                "boost" : 1.0
              }
            }
          },
          {
            "term" : {
              "2" : {
                "value" : "cpu",
                "boost" : 1.0
              }
            }
          },
          {
            "term" : {
              "3" : {
                "value" : "usage",
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
