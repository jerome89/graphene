# Cassandra 3.11.5
## What's different
- Based on Cassandra 3.11.5 official docker image.
- Jemalloc installed and cassandra uses offheap.
- ConfD to template configurations.
- Configurable data directories.
- Configurable listen interface.
- Configurable Snitch method.
- Added variable -Dcassandra.max_queued_native_transport_requests with value 8192.
- G1GC is default in use.

## Directories
- /var/lib/cassandra/ : commit_logs, hints, caches are saved in here.
- /var/log/cassandra/ : logs are saved in here.

## Important note
- You should provide environmental variable NETWORK_INTERFACE for listen interface.
- You should provide environmental variable SEEDS to gossip with.
- You should provide environmental variable CASSANDRA_DATA_DIRECTORY_* to run.
