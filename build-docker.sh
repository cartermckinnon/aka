#!/bin/sh

set -ex

VERSION=$(mvn help:evaluate -Dexpression=project.version -q -DforceStdout)

docker build . -t url-alias-service:$VERSION