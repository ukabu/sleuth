#!/bin/bash

. ./apps-urls/.env # load environment variables with apps endpoints

set -ex

pushd sleuth/smoketests
  ./mvnw verify
popd

exit 0
