# Overview

This page explains a developer to set up a graphene project for contributing.
Firstly, if you want to understand graphene [go to the graphene concepts page](https://github.com/Dark0096/graphene/wiki/Concepts).

We will set up the environment for developing Graphene application in the following order.

1. Infrastructure settings
2. Run application at Intellij
3. Test

# Prerequisites

- JDK 1.8

# 1. Infrastructure settings

First, Set up the infrastructure for developing and testing:

```console
$ git clone https://github.com/Dark0096/graphene
$ cd graphene
$ docker-compose up -d
```

Second, Create Elasticsearch index for the metric key.

```json
$ curl -XPUT 'http://localhost:9200/metric' -d '{
  "mappings": {
    "path": {
      "properties": {
        "path": {
          "index": "not_analyzed",
          "type": "string"
        },
        "depth": {
          "type": "long"
        },
        "leaf": {
          "type": "boolean"
        },
        "tenant": {
          "type": "string"
        }
      }
    }
  },
  "settings": {
    "number_of_replicas": 0,
    "number_of_shards": 5
  }
}'
```

Third, Create Cassandra keyspace and table for metric data.

```console
$ docker exec -it cassandra /bin/bash
$ cqlsh
$ CREATE KEYSPACE metric WITH REPLICATION = { 'class' : 'SimpleStrategy', 'replication_factor' : 1 };
$ CREATE TABLE metric.metric (
  period int,
  rollup int,
  tenant text,
  path text,
  time bigint,
  data list<double>,
  PRIMARY KEY ((tenant, period, rollup, path), time)
) WITH
  bloom_filter_fp_chance=0.010000 AND
  caching='KEYS_ONLY' AND
  comment='' AND
  dclocal_read_repair_chance=0.000000 AND
  gc_grace_seconds=864000 AND
  index_interval=128 AND
  read_repair_chance=0.000000 AND
  replicate_on_write='true' AND
  populate_io_cache_on_flush='false' AND
  default_time_to_live=0 AND
  speculative_retry='NONE' AND
  memtable_flush_period_in_ms=0 AND
  compaction = {'sstable_size_in_mb': '640', 'class': 'org.apache.cassandra.db.compaction.LeveledCompactionStrategy'} AND
  compression={'sstable_compression': 'LZ4Compressor'};
```

_Note that it is recommended that you allocate enough memory for the Docker engine._

# 2. Run application at Intellij

Graphene project was composed by Spring framework and Gradle build tools.
The main entry point is two parts which are graphene reader and writer.
Project settings are easy using by [Intellij](https://www.jetbrains.com/idea/).
Please refer to below links:
[Intellij with spring framework](https://spring.io/guides/gs/intellij-idea)

# 3. Test

If you ran Graphene reader and writer at IDE tools, you are ready to test.

1. Send metric to Graphene writer.

```console
$ echo "foo.bar 1 `date +%s`" | nc localhost 2003
```

2. Go to the Grafana Datasource setting screen ( Please use browser access mode )
<img src="https://github.com/Dark0096/graphene/blob/master/docs/image/Graphite%20Datasource%20Settings.png" width="400" height="400" />

3. You can check the metric you sent.
<img src="https://github.com/Dark0096/graphene/blob/master/docs/image/Graphene%20Test%20Dashboard.png" width="800" height="400" />
