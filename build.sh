#!/bin/sh

set -ex

mvn clean install

VERSION=$(mvn help:evaluate -Dexpression=project.version -q -DforceStdout)

docker build . -t url-alias-service:$VERSION