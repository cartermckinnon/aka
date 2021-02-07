#!/bin/sh

set -ex

cd $(dirname $0)
cd ..

VERSION=$(mvn help:evaluate -Dexpression=project.version -q -DforceStdout)

docker build . -t url-alias-service:$VERSION