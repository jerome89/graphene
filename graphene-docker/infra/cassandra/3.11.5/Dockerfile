FROM cassandra:3.11.5

ENV JEMALLOC_VERSION=5.2.1
ENV CONFD_VERSION=0.16.0

RUN apt update && apt install -y vim g++ autoconf make wget && \
    wget -O /tmp/jemalloc.tar.bz2 https://github.com/jemalloc/jemalloc/releases/download/${JEMALLOC_VERSION}/jemalloc-${JEMALLOC_VERSION}.tar.bz2 && \
    tar xvjf /tmp/jemalloc.tar.bz2 -C /tmp && \
    mv /tmp/jemalloc-${JEMALLOC_VERSION} /tmp/jemalloc && \
    cd /tmp/jemalloc && \
    /tmp/jemalloc/autogen.sh --prefix=/usr && \
    make && make install_lib && rm -rf /tmp/jemalloc* && apt remove -y g++ autoconf make && apt autoremove -y

RUN wget https://github.com/kelseyhightower/confd/releases/download/v${CONFD_VERSION}/confd-${CONFD_VERSION}-linux-amd64 && \
    mv confd-${CONFD_VERSION}-linux-amd64 /bin/confd && \
    chmod +x /bin/confd

RUN mkdir -p /etc/confd

ADD scripts/docker-entrypoint.sh /usr/local/bin/
ADD scripts/cassandra.in.sh /usr/share/cassandra/

ADD conf/* /etc/cassandra/
ADD confd /etc/confd

ENTRYPOINT ["/docker-entrypoint.sh"]
CMD ["cassandra", "-f"]

VOLUME /var/lib/cassandra/
VOLUME /var/log/cassandra/
