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

### Route

|       Environment         |Default Value|                        Description                               |
|---------------------------|-------------|---------------------------------------------------------------------------------------|
| GRAPHENE_INPUT_ROUTE_HOST | 127.0.0.1   | If you want to route your metric data for dual write, you can set this host to a graphene's load balancer DNS. |
| GRAPHENE_INPUT_ROUTE_PORT | 2003        | If you want to route your metric data for dual write, you can set this host to a graphene's load balancer port.|

## Store ( Key )

### Key Store Handler

|                 Environment                  | Default Value |           Description           |
|----------------------------------------------|---------------|---------------------------------|
| GRAPHENE_INDEX_ELASTICSEARCH_CLUSTER_NAME    | metric        | Elasticsearch cluster name      |
| GRAPHENE_INDEX_ELASTICSEARCH_CLUSTER         | elasticsearch | Elasticsearch cluster endpoint  |

## Store ( Data )

|                 Environment                  | Default Value |           Description           |
|----------------------------------------------|---------------|---------------------------------|
| GRAPHENE_DATA_CASSANDRA_CLUSTER              | 127.0.0.1     | Cassandra cluster endpoint      |
| GRAPHENE_DATA_CASSANDRA_USERNAME             | cassandra     | Cassandra username              |
| GRAPHENE_DATA_CASSANDRA_USERPASSWORD         | cassandra     | Cassandra user password         |
| GRAPHENE_DATA_CASSANDRA_KEYSPACE             | metric        | Cassandra keyspace              |
| GRAPHENE_DATA_CASSANDRA_COLUMNFAMILY         | metric        | Cassandra columnfamily          |

## Stats
