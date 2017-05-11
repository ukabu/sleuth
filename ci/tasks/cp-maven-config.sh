#!/bin/bash

if [ -z "$MAVEN_CONFIG_FILE" ]; then
  exit 0 # nothing to do
fi

mkdir -p ~/.m2
cp $MAVEN_CONFIG_FILE ~/.m2/settings.xml
