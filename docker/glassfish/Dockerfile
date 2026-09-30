FROM eclipse-temurin:21-jdk

WORKDIR /opt

ARG GLASSFISH_VERSION=7.1.1

RUN apt-get update \
    && apt-get install -y --no-install-recommends wget unzip ca-certificates \
    && wget -O glassfish.zip "https://www.eclipse.org/downloads/download.php?file=/ee4j/glassfish/glassfish-${GLASSFISH_VERSION}.zip&r=1" \
    && unzip glassfish.zip \
    && rm glassfish.zip \
    && rm -rf /var/lib/apt/lists/*

ENV GLASSFISH_HOME=/opt/glassfish7
ENV PATH="${GLASSFISH_HOME}/bin:${PATH}"

EXPOSE 8080 4848

CMD ["asadmin", "start-domain", "--verbose", "domain1"]
