#!/bin/bash

sleuth/ci/tasks/cp-maven-config.sh

. ./apps-urls/.env # load environment variables with apps endpoints

set -ex

pushd sleuth/smoketests
  ./mvnw test
popd

exit 0
