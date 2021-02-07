#!/bin/sh

set -ex

cd $(dirname $0)
cd ..

rm -rf dist/
mkdir dist/

cp -R target/lib dist/
cp target/url-alias-service-*.jar dist/
cp -R target/site/jacoco dist/code-coverage
cp -R target/site/apidocs dist/javadoc