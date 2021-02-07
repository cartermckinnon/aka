#!/bin/sh

set -ex

cd $(dirname $0)
cd ..

mvn clean install