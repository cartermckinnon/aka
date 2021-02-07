#!/bin/sh

set -ex

cd $(dirname $0)
cd ..

VERSION=$(mvn help:evaluate -Dexpression=project.version -q -DforceStdout)

docker tag url-alias-service:$VERSION mckdev/url-alias-service:$VERSION
docker tag url-alias-service:$VERSION mckdev/url-alias-service:latest

docker push mckdev/url-alias-service:$VERSION
docker push mckdev/url-alias-service:latest