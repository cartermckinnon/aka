#!/bin/sh

set -ex

cd $(dirname $0)

java -jar aka-*.jar server configuration.yaml