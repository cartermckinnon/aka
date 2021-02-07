#!/bin/sh

set -ex

cd $(dirname $0)

java -jar url-alias-service-*.jar server configuration.yaml