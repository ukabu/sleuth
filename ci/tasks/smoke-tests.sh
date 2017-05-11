#!/bin/bash

set -ex

set +x
cf login -a $PCF_API -u $PCF_USER -p $PCF_PASSWORD -o $PCF_ORG -s $PCF_SPACE
set -x

MESSAGE_CLIENT_ENDPOINT=$(cf app sleuth-message-client | grep -oP "(routes|urls)\:\s+\K\S+")
ZIPKIN_SERVER_ENDPOINT=$(cf app sleuth-zipkin-query-service | grep -oP "(routes|urls)\:\s+\K\S+")

curl https://$MESSAGE_CLIENT_ENDPOINT/message/template

NOW=$(($(date +%s%N)/1000000))

exit 0
