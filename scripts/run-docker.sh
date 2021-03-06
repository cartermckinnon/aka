#!/bin/sh

set -ex

cd $(dirname $0)
cd ..

VERSION=$(mvn help:evaluate -Dexpression=project.version -q -DforceStdout)

docker run \
  --rm \
  -p 8080:8080 \
  -v $PWD/configuration.yaml:/app/configuration/configuration.yaml \
  aka:$VERSION