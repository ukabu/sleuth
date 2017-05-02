#! /bin/bash
set -e +x
pushd sleuth
  echo "Running tests"
  ./mvnw test
popd
exit 0
