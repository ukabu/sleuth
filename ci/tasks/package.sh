#!/bin/bash

set -ex
echo " --------------  $MAVEN_CONFIG"
pushd sleuth
  echo "-----------------------"
  cat $MAVEN_CONFIG
  echo "-----------------------"
  echo "Packaging JAR"
  ./mvnw clean package -DskipTests

  cp --parents */target/*jar ../package-output/
  cp manifest.yml ../package-output/
popd

echo "Done packaging"
exit 0
