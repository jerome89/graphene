# Graphene

## What is Graphene?
Graphene is a java version of [Graphite](https://graphiteapp.org). To
store metrics, it uses [Cassandra](https://github.com/apache/cassandra)
and [Elasticsearch](https://github.com/elastic/elasticsearch) instead of
[Whisper](https://github.com/graphite-project/whisper) to handle data in
a scalable way. It is designed to be compatible with Graphite's API and
easily integrates with Grafana's Graphite datasource.   
In a cloud environment, the metric key increases exponentially. To cope
with this, each metric needs to be managed by time and removed or
archived according to the policy. Graphene is a system that supports
these methods later to help you manage your metrics cost-effectively. It
also supports multi-tenancy for metrics later, so that large numbers of
metrics can be maintained and managed in isolated clusters.

graphene-writer: cassandra backed metric storage *writer*
==================================================

This project is inspired by [cyanite](https://github.com/pyr/cyanite) and is intended to replace it in write mode only (thus transiently replacing **Graphite**).
It must be fully compatible with **cyanite** and cyanite can still be used for reading. I've started another project 
which will do the reads: [disthene-reader](https://github.com/EinsamHauer/disthene-reader). And, yes, I'm convinced that in a large scale setup (and that's why I started **disthene** project) it's best to separate these two roles. 

## Motivation

There are a couple of things which seem to be an absolute must and which were missing in **cyanite**:

* aggregation: ability to sum similar metrics from several servers
* blacklisting: ability to omit storing metrics which match a certain pattern. Makes not much sense by itself but is quite handy when you have previous item
* caching incoming paths: this may really reduce the load on Elasticsearch cluster
* some minimal set of own metrics (received count, write count, etc)
* true multitenancy

The other thing is performance. **Disthene** is being developed with one major requirement in mind - performance. 
Essentially, in most cases the only bottleneck will be C\*. 
There is no ultimate benchmark here (mostly because of the C\* bottleneck in test lab) 
but it looks like something like 1M data points/core/minute should not be a problem     

(Here is a quick performance [comparison](https://gist.github.com/EinsamHauer/2aa552a63add5415bfe5))

## Compiling 

This is a standard Java Maven project. 

```
mvn package
```

will most probably do the trick.

## Running

First of all, it's strongly recommended to run it with Java 8. Even though this software is fully compatible with Java 7. 
The main reason for that is a bug in Java ([JDK-7032154](http://bugs.java.com/view_bug.do?bug_id=7032154)) prior to version 8
Moreover, it's strongly recommended to have some GC tuning. For a start here is a set of some universal options:
* -XX:+UseConcMarkSweepGC
* -XX:+CMSParallelRemarkEnabled
* -XX:+UseCMSInitiatingOccupancyOnly
* -XX:CMSInitiatingOccupancyFraction=75
* -XX:+ScavengeBeforeFullGC
* -XX:+CMSScavengeBeforeRemark
 
There are a couple of things you will need in runtime, just the same set as for **cyanite**

* Cassandra
* Elasticsearch

Cassandra schema is identical to that of **cyanite**:

```
CREATE TABLE metric (
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
  read_repair_chance=0.100000 AND
  replicate_on_write='true' AND
  populate_io_cache_on_flush='false' AND
  default_time_to_live=0 AND
  speculative_retry='NONE' AND
  memtable_flush_period_in_ms=0 AND
  compaction={'class': 'SizeTieredCompactionStrategy'} AND
  compression={'sstable_compression': 'LZ4Compressor'};

```

Your mileage may vary but generally (as graphite like systems are closer to write only/read never type) one would benefit from changing
```
  compression={'sstable_compression': 'LZ4Compressor'};
```
to
```
  compression={'sstable_compression': 'DeflateCompressor'};
```
This will probably save ~25% on disk storage and quite some IO on reads at the cost of slightly increased CPU.


## Configuration
There several configuration files involved
* /etc/disthene/disthene.yaml (location can be changed with -c command line option if needed)
* /etc/disthene/disthene-log4j.xml (location can be changed with -l command line option if needed)
* /etc/disthene/blacklist.yaml (location can be changed with -b command line option if needed)
* /etc/disthene/aggregator.yaml (location can be changed with -a command line option if needed)

##### Main configuration in disthene.yaml
```
carbon:
# bind address and port
  bind: "127.0.0.0"
  port: 2003
# rollups - currently only "s" units supported  
  rollups:
    - 60s:5356800s
    - 900s:62208000s
# seconds to wait before flushing aggregated metrics   
  aggregatorDelay: 90
# also aggregate the first rollup
  aggregateBaseRollup: false
store:
# C* contact points, port, keyspace and table
  cluster:
    - "cassandra-1"
    - "cassandra-2"
  port: 9042
  keyspace: 'metric'
  columnFamily: 'metric'
# maximum connections per host , timeouts in seconds, max requests per host - these are literally used in C* java driver settings
  maxConnections: 2048
  readTimeout: 10
  connectTimeout: 10
  maxRequests: 128
# use C* batch statetments - the trade off is: using batch puts load on C*, not using it may cause congestion on disthene side
  batch: true
# batch size if above is true
  batchSize: 500
# number of threads submitting requests to C*  
  pool: 2
index:
# ES cluster name, contact points, native port, index name & type
  name: "disthene"
  cluster:
    - "es-1"
    - "es-2"
  port: 9300
  index: "disthene"
  type: "path"
# cache paths on disthene side?
  cache: true
# if cached is used, expire it after seconds below. That is, if we haven't seen metric name on 'expire' seconds - remove it from cache
  expire: 3600
# when to flush bulk - either when incoming queue reaches 'actions' size or every 'interval' seconds
  bulk:
    actions: 10000
    interval: 5
stats:
# flush self metrics every 'interval' seconds
  interval: 60
# tenant to use for stats
  tenant: "test"
# hostname to use
  hostname: "disthene-1a"
# output stats to log as well
  log: true
```

##### Logging configuration in disthene-log4j.xml
Configuration is straight forward as per log4j

##### Blacklist configuration in blacklist.yaml
This is a list of regular expressions per tenant. Matching metrics will NOT be store but they still WILL be aggregated (see below)

##### Aggregation configuration in aggregator.yaml
List of aggregation rules per tenant. By exmaple:
```
"xxx_test_server*.<data>": "xxx_sum.<data>"
```
means that disthene will sum all the values matching 'xxx_test_server*.<data>' (where <data> is a placeholder for deeper path) and put the value into 'xxx_sum.<data>'

## Configuration
There several configuration files involved
* /etc/disthene/disthene.yaml (location can be changed with -c command line option if needed)
* /etc/disthene/disthene-log4j.xml (location can be changed with -l command line option if needed)
* /etc/disthene/blacklist.yaml (location can be changed with -b command line option if needed)
* /etc/disthene/aggregator.yaml (location can be changed with -a command line option if needed)

##### Main configuration in disthene.yaml
```
carbon:
# bind address and port
  bind: "127.0.0.0"
  port: 2003
# rollups - currently only "s" units supported  
  rollups:
    - 60s:5356800s
    - 900s:62208000s
# seconds to wait before flushing aggregated metrics   
  aggregatorDelay: 90
# also aggregate the first rollup
  aggregateBaseRollup: false
store:
# C* contact points, port, keyspace and table
  cluster:
    - "cassandra-1"
    - "cassandra-2"
  port: 9042
  keyspace: 'metric'
  columnFamily: 'metric'
# maximum connections per host , timeouts in seconds, max requests per host - these are literally used in C* java driver settings
  maxConnections: 2048
  readTimeout: 10
  connectTimeout: 10
  maxRequests: 128
# use C* batch statetments - the trade off is: using batch puts load on C*, not using it may cause congestion on disthene side
  batch: true
# batch size if above is true
  batchSize: 500
# number of threads submitting requests to C*  
  pool: 2
index:
# ES cluster name, contact points, native port, index name & type
  name: "disthene"
  cluster:
    - "es-1"
    - "es-2"
  port: 9300
  index: "disthene"
  type: "path"
# cache paths on disthene side?
  cache: true
# if cached is used, expire it after seconds below. That is, if we haven't seen metric name on 'expire' seconds - remove it from cache
  expire: 3600
# when to flush bulk - either when incoming queue reaches 'actions' size or every 'interval' seconds
  bulk:
    actions: 10000
    interval: 5
stats:
# flush self metrics every 'interval' seconds
  interval: 60
# tenant to use for stats
  tenant: "test"
# hostname to use
  hostname: "disthene-1a"
# output stats to log as well
  log: true
```

##### Logging configuration in disthene-log4j.xml
Configuration is straight forward as per log4j

##### Blacklist configuration in blacklist.yaml
This is a list of regular expressions per tenant. Matching metrics will NOT be store but they still WILL be aggregated (see below)

##### Aggregation configuration in aggregator.yaml
List of aggregation rules per tenant. By exmaple:
```
"xxx_test_server*.<data>": "xxx_sum.<data>"
```
means that disthene will sum all the values matching 'xxx_test_server*.<data>' (where <data> is a placeholder for deeper path) and put the value into 'xxx_sum.<data>'

graphene-reader: cassandra backed metric storage *reader*
=========================================================

## Motivation
This is a "dual" project to [disthene](https://github.com/EinsamHauer/disthene). Though the data written by **disthene** can still be read and plotted by combination of **cyanite** & **graphite-api**, this schema introduces quite some overhead at least caused by serializing/deserializing to/from json. In cases when rendering a graph involves 10s of millions of data points this overhead is quite noticable.
Besides that **graphite-api** as well as original **graphite** rendering could really be a bit faster.
All in all, this project is about read and rendering performance exactly like **disthene** is about write performance.

## What's in
The following APIs are supported:
* /paths API for backward compatibility with **graphite-api** and **cyanite**
* /metrics/find for Grafana
* /metrics
* /render mostly as per Graphite specification version 0.10.0

## Support Graphite Function
Please check [this document](/docs/FUNCTION.md) for the graphene-reader configuration

## Compiling 

This is a standard Java Gradle project. 

```
gradle jar
```

will most probably do the trick.

## Running
There are a couple of things you will need in runtime

* Cassandra 2.x
* Elasticsearch 2.x
* Grafana using graphite as the datasource

## Quick start for development

You can run the disthene-reader app by using the default setup value as shown below.

```
$ export GRAPHENE_HOME=$GRAPHENE_INSTALLED_DIR
$ cd $GRAPHENE_HOME
$ docker-compose up -d
$ docker exec -it cassandra cqlsh
# create the keyspace and table using $GRAPHENE_HOME/infra/cassandra/2.x/metric.cql
$ -c $GRAPHENE_HOME/graphene-reader/graphene-reader-sample.yaml -l $GRAPHENE_HOME/config/graphene-reader-log4j-sample.xml -t $GRAPHENE_HOME/config/throttling-sample.yaml   
```

## Commit convention
This project uses the git conventional commit rule provided by [conventional commits](https://www.conventionalcommits.org/en/v1.0.0-beta.4/)

## Configuration
Please check [this document](/docs/CONFIGURATION.md) for the graphene-reader configuration

## Thanks

Thanks, this project is useless without their work on **cyanite**, **graphite-api**, **graphite-cyanite**, **disthene-reader**, **disthene**

- Pierre-Yves Ritschard ([https://github.com/pyr](https://github.com/pyr))
- Bruno Renié ([https://github.com/brutasse](https://github.com/brutasse))
- EinsamHauer ([https://github.com/EinsamHauer/disthene-reader](https://github.com/EinsamHauer/disthene-reader))

Many people helped with the Graphene project.

- jerome89 ([https://github.com/jerome89]) : He found the grammar of Graphite functions in the disthene project. This helped us to create a better metric system. Additionally, he helped develop the faulty function of the graphene itself and the overall system development.
- drunkencoding : He developed and maintained the entire metric core system like Elasticsearch indexing logic and time life cycle in the metric key (path). Without him, the metric system would only be able to handle simple traffic.

## License

The MIT License (MIT)

Copyright (C) 2015 Andrei Ivanov

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
