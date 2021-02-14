#!/bin/sh

set -ex

cd $(dirname $0)
cd ..

rm -rf dist/
mkdir dist/

VERSION=$(mvn help:evaluate -Dexpression=project.version -q -DforceStdout)

cp -R target/lib dist/
cp target/aka-*.jar dist/
cp -R target/site/jacoco dist/code-coverage
cp -R target/site/apidocs dist/javadoc
cp configuration.yaml dist/
cp scripts/run-dist.sh dist/run.sh
cp -R k8s/ dist/

cd dist/
tar -czvf ../aka-$VERSION.tar.gz .
