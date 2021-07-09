#!/bin/sh

# localhost
HONEYCOMB_API_KEY=351f2a5d698936448b0fc95c418cca69
HONEYCOMB_API_ENDPOINT=http://localhost:8081

# Kibble
#HONEYCOMB_API_KEY=<fill me in>
#HONEYCOMB_API_ENDPOINT=https://api-kibble.honeycomb.io:443

# Common
SAMPLE_RATE=1
SERVICE_NAME=tapper
HONEYCOMB_DATASET=tapper

java -jar target/hny-example-0.0.1-SNAPSHOT.jar --honeycomb.beeline.write-key=$HONEYCOMB_API_KEY --honeycomb.beeline.api-host=$HONEYCOMB_API_ENDPOINT

