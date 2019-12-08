FROM gradle:5.5.1-jdk8

#################################
#         Program version       #
#################################
ENV GRAPHENE_TYPE=graphene-writer
ENV GRAPHENE_VERSION=1.4.0
ENV CONFD_VERSION=0.16.0

#################################
#         Utility tools         #
#################################
WORKDIR /bin

RUN wget https://github.com/kelseyhightower/confd/releases/download/v${CONFD_VERSION}/confd-${CONFD_VERSION}-linux-amd64 -O confd && \
    chmod +x confd

##################################
##    Graphene binary install    #
##################################
WORKDIR /tmp

RUN git clone https://github.com/graphene-monitoring/graphene

WORKDIR /tmp/graphene

RUN git fetch && \
    git checkout -b $GRAPHENE_VERSION $GRAPHENE_VERSION

##################################
##         Graphene build        #
##################################
WORKDIR /tmp/graphene/${GRAPHENE_TYPE}

RUN gradle bootjar
##################################
##  Config & Entrypoint script   #
##################################
WORKDIR /

ADD scripts /bin/
ADD confd /etc/confd

ENTRYPOINT ["/bin/entrypoint.sh"]
