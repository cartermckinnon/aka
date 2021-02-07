#!/bin/sh

cd $(dirname $0)

java -jar target/url-alias-service-*.jar server configuration.yaml
