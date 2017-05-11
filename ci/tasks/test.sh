#! /bin/bash

sleuth/ci/tasks/cp-maven-config.sh

set -ex
pushd sleuth
  ./mvnw test
popd
exit 0
