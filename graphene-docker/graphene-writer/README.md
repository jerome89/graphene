# Graphene Writer Config

## Common Environment

|     Environment      |                            Description                           |
|----------------------|------------------------------------------------------------------|
| GRAPHENE_CONF_DIR    | This environment sets Graphene configuration directory location. |
| GRAPHENE_DATA_DIR    | This environment sets Graphene data directory location.          |
| GRAPHENE_LOG_DIR     | This environment sets Graphene logging directory location.       |
| GRAPHENE_PROGRAM_DIR | This environment sets Graphene program directory location.       |

## Blacklist

## Input

### Carbon

#### Route

|       Environment         |Default Value|                                                       Description                                              |
|---------------------------|-------------|----------------------------------------------------------------------------------------------------------------|
| GRAPHENE_INPUT_ROUTE_HOST | 127.0.0.1   | If you want to route your metric data for dual write, you can set this host to a graphene's load balancer DNS. |
| GRAPHENE_INPUT_ROUTE_PORT | 2003        | If you want to route your metric data for dual write, you can set this host to a graphene's load balancer port.|

### Kafka

#### Custom

This environment means that you can implement the custom deserializer and then apply it.

|          Environment                                                                                               |                            Default Value                             |                                                    Description                                                 |
|--------------------------------------------------------------------------------------------------------------------|----------------------------------------------------------------------|----------------------------------------------------------------------------------------------------------------|
| GRAPHENE_INPUT_KAFKA_CUSTOM_ENABLED                                                                                | false                                                                |                                                                                                                |
| GRAPHENE_INPUT_KAFKA_CUSTOM_BOOTSTRAP_SERVER                                                                       | localhost:9092                                                       |                                                                                                                |
| GRAPHENE_INPUT_KAFKA_CUSTOM_CONSUMER_GROUP_ID                                                                      | graphene-writer                                                      |                                                                                                                |
| GRAPHENE_INPUT_KAFKA_CUSTOM_POLL_INTERVAL_MS                                                                       | 500                                                                  |                                                                                                                |
| GRAPHENE_INPUT_KAFKA_CUSTOM_MAX_POLL_RECORDS                                                                       | 1000                                                                 |                                                                                                                |
| GRAPHENE_INPUT_KAFKA_CUSTOM_TOPICS                                                                                 | graphene                                                             |                                                                                                                |
| GRAPHENE_INPUT_KAFKA_CUSTOM_KEY_DESERIALIZER_CLASS                                                                 | org.apache.kafka.common.serialization.StringDeserializer             |                                                                                                                |
| GRAPHENE_INPUT_KAFKA_CUSTOM_VALUE_DESERIALIZER_CLASS                                                               | com.graphene.writer.input.kafka.deserializer.PrometheusDeserializer  |                                                                                                                |

#### Prometheus

|          Environment                                                                                               |                            Default Value                             |                                                    Description                                                 |
|--------------------------------------------------------------------------------------------------------------------|----------------------------------------------------------------------|----------------------------------------------------------------------------------------------------------------|
| GRAPHENE_INPUT_KAFKA_PROMETHEUS_ENABLED                                                                            | false                                                                |                                                                                                                |
| GRAPHENE_INPUT_KAFKA_PROMETHEUS_BOOTSTRAP_SERVER                                                                   | localhost:9092                                                       |                                                                                                                |
| GRAPHENE_INPUT_KAFKA_PROMETHEUS_CONSUMER_GROUP_ID                                                                  | graphene-writer                                                      |                                                                                                                |
| GRAPHENE_INPUT_KAFKA_PROMETHEUS_POLL_INTERVAL_MS                                                                   | 500                                                                  |                                                                                                                |
| GRAPHENE_INPUT_KAFKA_PROMETHEUS_MAX_POLL_RECORDS                                                                   | 1000                                                                 |                                                                                                                |
| GRAPHENE_INPUT_KAFKA_PROMETHEUS_TOPICS                                                                             | graphene                                                             |                                                                                                                |
| GRAPHENE_INPUT_KAFKA_PROMETHEUS_KEY_DESERIALIZER_CLASS                                                             | org.apache.kafka.common.serialization.StringDeserializer             |                                                                                                                |
| GRAPHENE_INPUT_KAFKA_PROMETHEUS_VALUE_DESERIALIZER_CLASS                                                           | com.graphene.writer.input.kafka.deserializer.PrometheusDeserializer  |                                                                                                                |

#### InfluxDB

|          Environment                                                                                               |                            Default Value                             |                                                    Description                                                 |
|--------------------------------------------------------------------------------------------------------------------|----------------------------------------------------------------------|----------------------------------------------------------------------------------------------------------------|
| GRAPHENE_INPUT_KAFKA_INFLUXDB_ENABLED                                                                              | false                                                                |                                                                                                                |
| GRAPHENE_INPUT_KAFKA_INFLUXDB_BOOTSTRAP_SERVER                                                                     | localhost:9092                                                       |                                                                                                                |
| GRAPHENE_INPUT_KAFKA_INFLUXDB_CONSUMER_GROUP_ID                                                                    | graphene-writer                                                      |                                                                                                                |
| GRAPHENE_INPUT_KAFKA_INFLUXDB_POLL_INTERVAL_MS                                                                     | 500                                                                  |                                                                                                                |
| GRAPHENE_INPUT_KAFKA_INFLUXDB_MAX_POLL_RECORDS                                                                     | 1000                                                                 |                                                                                                                |
| GRAPHENE_INPUT_KAFKA_INFLUXDB_TOPICS                                                                               | graphene                                                             |                                                                                                                |
| GRAPHENE_INPUT_KAFKA_INFLUXDB_KEY_DESERIALIZER_CLASS                                                               | org.apache.kafka.common.serialization.StringDeserializer             |                                                                                                                |
| GRAPHENE_INPUT_KAFKA_INFLUXDB_VALUE_DESERIALIZER_CLASS                                                             | com.graphene.writer.input.kafka.deserializer.InfluxDbDeserializer    |                                                                                                                |

## Store ( Key )

### Key Store Handler

|                 Environment                  | Default Value |           Description           |
|----------------------------------------------|---------------|---------------------------------|
| GRAPHENE_INDEX_ELASTICSEARCH_CLUSTER_NAME    | metric        | Elasticsearch cluster name      |
| GRAPHENE_INDEX_ELASTICSEARCH_CLUSTER         | elasticsearch | Elasticsearch cluster endpoint  |

## Store ( Data )

### Simple Data Store Handler

|                 Environment                  | Default Value |           Description           |
|----------------------------------------------|---------------|---------------------------------|
| GRAPHENE_DATA_HANDLER_SIMPLE_ENABLED         | false         |                                 |
| GRAPHENE_DATA_HANDLER_SIMPLE_TTL             | 604800        |                                 |
| GRAPHENE_DATA_HANDLER_SIMPLE_KEYSPACE        | metric_offset |                                 |
| GRAPHENE_DATA_HANDLER_SIMPLE_COLUMNFAMILY    | metric        |                                 |
| GRAPHENE_DATA_HANDLER_SIMPLE_CLUSTER         | 127.0.0.1     |                                 |
| GRAPHENE_DATA_HANDLER_SIMPLE_USERNAME        | cassandra     |                                 |
| GRAPHENE_DATA_HANDLER_SIMPLE_USERPASSWORD    | cassandra     |                                 |
| GRAPHENE_DATA_HANDLER_SIMPLE_MAX_CONNECTIONS | 2048          |                                 |
| GRAPHENE_DATA_HANDLER_SIMPLE_MAX_REQUESTS    | 128           |                                 |
| GRAPHENE_DATA_HANDLER_SIMPLE_CONSISTENCYLEVEL| ONE           |                                 |
| GRAPHENE_DATA_HANDLER_SIMPLE_PROTOCOLVERSION | V4            |                                 |

### Offset Based Data Store Handler

|                 Environment                  | Default Value |           Description           |
|----------------------------------------------|---------------|---------------------------------|
| GRAPHENE_DATA_HANDLER_OFFSET_ENABLED         | false         |                                 |
| GRAPHENE_DATA_HANDLER_OFFSET_TTL             | 604800        |                                 |
| GRAPHENE_DATA_HANDLER_OFFSET_KEYSPACE        | metric_offset |                                 |
| GRAPHENE_DATA_HANDLER_OFFSET_COLUMNFAMILY    | metric        |                                 |
| GRAPHENE_DATA_HANDLER_OFFSET_BUCKETSIZE      | 30000         |                                 |
| GRAPHENE_DATA_HANDLER_OFFSET_CLUSTER         | 127.0.0.1     |                                 |
| GRAPHENE_DATA_HANDLER_OFFSET_USERNAME        | cassandra     |                                 |
| GRAPHENE_DATA_HANDLER_OFFSET_USERPASSWORD    | cassandra     |                                 |
| GRAPHENE_DATA_HANDLER_OFFSET_MAX_CONNECTIONS | 2048          |                                 |
| GRAPHENE_DATA_HANDLER_OFFSET_MAX_REQUESTS    | 128           |                                 |
| GRAPHENE_DATA_HANDLER_OFFSET_CONSISTENCYLEVEL| ONE           |                                 |
| GRAPHENE_DATA_HANDLER_OFFSET_PROTOCOLVERSION | V4            |                                 |

## Stats
