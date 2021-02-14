#!/bin/sh

set -ex

cd $(dirname $0)
cd ..

java -jar target/aka-*.jar server configuration.yaml
