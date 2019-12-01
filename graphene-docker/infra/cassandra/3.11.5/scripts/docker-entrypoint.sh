#!/bin/bash
set -e

# first arg is `-f` or `--some-option`
# or there are no args
if [ "$#" -eq 0 ] || [ "${1#-}" != "$1" ]; then
	set -- cassandra -f "$@"
fi

# allow the container to be started with `--user`
if [ "$1" = 'cassandra' -a "$(id -u)" = '0' ]; then
	find /var/lib/cassandra /var/log/cassandra "$CASSANDRA_CONFIG" \
		\! -user cassandra -exec chown cassandra '{}' +
	exec gosu cassandra "$BASH_SOURCE" "$@"
fi

_ip_address() {
	# scrape the first non-localhost IP address of the container
	# in Swarm Mode, we often get two IPs -- the container IP, and the (shared) VIP, and the container IP should always be first
	ip address | awk '
		$1 == "inet" && $NF != "lo" {
			gsub(/\/.+$/, "", $2)
			print $2
			exit
		}
	'
}

# "sed -i", but without "mv" (which doesn't work on a bind-mounted file, for example)
_sed-in-place() {
	local filename="$1"; shift
	local tempFile
	tempFile="$(mktemp)"
	sed "$@" "$filename" > "$tempFile"
	cat "$tempFile" > "$filename"
	rm "$tempFile"
}

CONFD_BACKEND=${CONFD_BACKEND:-env}

export CLUSTER_NAME=${CLUSTER_NAME:-metric}

if [ -z ${SEEDS+x} ]; then
  echo "SEEDS variable must be provided";
  exit 1;
fi

if [ -z ${NETWORK_INTERFACE+x} ]; then
  echo "NETWORK_INTERFACE variable must be provided";
  exit 1;
fi

export ENDPOINT_SNITCH=${ENDPOINT_SNITCH:-GossipingPropertyFileSnitch}

if [ "$1" = 'cassandra' ]; then
	mkdir -p /var/log/cassandra/
	mkdir -p /var/lib/cassandra/commitlog
	mkdir -p /var/lib/cassandra/saved_caches
	mkdir -p /var/lib/cassandra/hints
	confd --onetime --backend $CONFD_BACKEND

	for rackdc in dc rack; do
		var="CASSANDRA_${rackdc^^}"
		val="${!var}"
		if [ "$val" ]; then
			_sed-in-place "$CASSANDRA_CONFIG/cassandra-rackdc.properties" \
				-r 's/^('"$rackdc"'=).*/\1 '"$val"'/'
		fi
	done
fi

exec "$@"
