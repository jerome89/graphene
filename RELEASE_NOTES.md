# 1.1.2

## Bug fix
- Fix metrics/find API duplicated path issue [#3](https://github.com/graphene-monitoring/graphene/pull/3)

## Feature
- aliasByNode function now supports minus index [#2](https://github.com/graphene-monitoring/graphene/pull/2)

# 1.1.1

## Feature
- Remove local aggregation and rollup feature

## Bug fix

# 1.1.0

## Feature
- Development environment to use docker-compose.yml
- Support the /metrics/find API to use at Grafana [#1](https://github.com/Dark0096/disthene-reader/issues/1) 
- Implement StatsService interface to be disable graphite stats service
- Change project build tool from maven to gradle
- Use spring framework for graphene

## Bug fix
- Fix /render api not supported about asterisk query [#3](https://github.com/Dark0096/disthene-reader/issues/3)
- Fix the duplication metric path to use /metrics/find API [#5](https://github.com/Dark0096/disthene-reader/issues/5)
- Add the elasticsearch client ping config to keep safely connection between elasticsearch cluster [#11](https://github.com/Dark0096/disthene-reader/issues/11) 
