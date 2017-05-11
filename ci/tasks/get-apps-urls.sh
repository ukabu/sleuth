#!/bin/bash

set -ex

set +x
cf login -a $PCF_API -u $PCF_USER -p $PCF_PASSWORD -o $PCF_ORG -s $PCF_SPACE
set -x

SLEUTH_MESSAGE_CLIENT_ENDPOINT=$(cf app sleuth-message-client | grep -oP "(routes|urls)\:\s+\K\S+")
SLEUTH_MESSAGE_SERVICE_ENDPOINT=$(cf app sleuth-message-service | grep -oP "(routes|urls)\:\s+\K\S+")
SLEUTH_ZIPKIN_SERVER_ENDPOINT=$(cf app sleuth-zipkin-query-service | grep -oP "(routes|urls)\:\s+\K\S+")

echo "export SLEUTH_MESSAGE_CLIENT_ENDPOINT=https://$SLEUTH_MESSAGE_CLIENT_ENDPOINT" > ./.env
echo "export SLEUTH_MESSAGE_SERVICE_ENDPOINT=https://$SLEUTH_MESSAGE_SERVICE_ENDPOINT" >> ./.env
echo "export SLEUTH_ZIPKIN_SERVER_ENDPOINT=https://$SLEUTH_ZIPKIN_SERVER_ENDPOINT" >> ./.env

exit 0
