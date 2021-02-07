#!/bin/sh

set -ex

cd $(dirname $0)
cd ..

java -jar target/url-alias-service-*.jar server configuration.yaml
