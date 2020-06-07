CREATE KEYSPACE metric_offset_30000 WITH replication = {'class': 'NetworkTopologyStrategy', 'ap-northeast-2': '3'} AND durable_writes = true;

-- 'bucketSize * rollup = range of data can be stored. here bucketSize = 30000 and rollup = 60s'
CREATE TABLE metric_offset_30000.metric_60s (
  tenant text,
  path text,
  startTime bigint,
  offset smallint,
  data double,
  PRIMARY KEY ((tenant, path, startTime), offset)
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
