#!/bin/bash

set -e

set +x
cf login -a $PCF_API -u $PCF_USER -p $PCF_PASSWORD -o $PCF_ORG -s $PCF_SPACE
set -x

cf stop sleuth-message-client
cf stop sleuth-message-service
cf stop sleuth-zipkin-query-service

cf logout

exit 0
