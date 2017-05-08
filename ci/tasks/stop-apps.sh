#!/bin/bash

set -e

set +x
cf login -a {{pcf-api}} -u {{pcf-user}} -p {{pcf-password}} -o {{pcf-org}} -s {{pcf-space}}
set -x

cf stop sleuth-message-client
cf stop sleuth-message-service
cf stop sleuth-zipkin-query-service

cf logout

exit 0
