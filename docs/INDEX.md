# Elasticsearch Index Info

You can set index mapping information using by below command.

```
curl -XPUT 'http://localhost:9200/metric' -d '{
  "mappings": {
    "path": {
      "_source": {
        "enabled": false
      },
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
    "number_of_replicas": 1,
    "number_of_shards": 5
  }
}'
```