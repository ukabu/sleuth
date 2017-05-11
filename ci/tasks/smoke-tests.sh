#!/bin/bash

. ./apps-urls/.env # load environment variables with apps endpoints

set -ex

pushd sleuth/smoke-tests
  ./mvnw verify
popd

exit 0
