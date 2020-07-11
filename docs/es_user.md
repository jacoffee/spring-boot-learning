```
PUT _template/user
{
  "index_patterns": ["user"],
  "settings": {
    "number_of_shards": 1
  },
  "mappings": {
    "properties": {
      "es_id": {
        "type": "keyword"
      },
      "project": {
        "type": "keyword",
        "doc_values": false
      },
      "uid": {
        "type": "keyword",
        "doc_values": false
      },
      "name": {
        "type": "text",
        "analyzer": "ik_max_word",
        "search_analyzer": "ik_max_word",
        "doc_values": false
      },
      "last_login_time": {
        "type": "date",
        "format": "epoch_millis"
      }
    }
  }
}


PUT /user/_doc/jacoffee_1
{
    "es_id": "jacoffee_1",
    "project_id": "jacoffee",
    "uid": "1",
    "name": "allen1",
    "last_login_time": 1594046183687
}
```
