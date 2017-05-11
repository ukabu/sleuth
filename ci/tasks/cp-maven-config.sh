#!/bin/bash

if [ -z "$MAVEN_CONFIG_FILE" ]; then
  exit 0 # nothing to do
fi

cp $MAVEN_CONFIG_FILE ~/.m2/settings.xml
