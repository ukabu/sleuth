#! /bin/bash
set -ex
pushd sleuth
  ./mvnw test
popd
exit 0
