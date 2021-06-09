#!/bin/sh

# localhost
HONEYCOMB_API_KEY=<fill me in>
HONEYCOMB_API_ENDPOINT=http://localhost:8081

# Kibble
#HONEYCOMB_API_KEY=<fill me in>
#HONEYCOMB_API_ENDPOINT=https://api-kibble.honeycomb.io:443

# Common
SAMPLE_RATE=2
SERVICE_NAME=tapper
HONEYCOMB_DATASET=stephanie-delete-me

java -Dsample.rate=$SAMPLE_RATE -Dservice.name=$SERVICE_NAME -Dhoneycomb.api.key=$HONEYCOMB_API_KEY -Dhoneycomb.dataset=$HONEYCOMB_DATASET -Dhoneycomb.api.endpoint=$HONEYCOMB_API_ENDPOINT -javaagent:honeycomb-opentelemetry-javaagent-0.3.0-all.jar -jar target/hny-example-0.0.1-SNAPSHOT.jar
