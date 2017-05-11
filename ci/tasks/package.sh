#!/bin/bash

sleuth/ci/tasks/cp-maven-config.sh

set -ex

pushd sleuth
  ./mvnw clean package -DskipTests

  cp --parents */target/*jar ../package-output/
  cp manifest.yml ../package-output/
popd

exit 0
