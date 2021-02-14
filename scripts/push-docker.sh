#!/bin/sh

set -ex

cd $(dirname $0)
cd ..

VERSION=$(mvn help:evaluate -Dexpression=project.version -q -DforceStdout)

docker tag aka:$VERSION mckdev/aka:$VERSION
docker tag aka:$VERSION mckdev/aka:latest

docker push mckdev/aka:$VERSION
docker push mckdev/aka:latest