#!/bin/bash

PROJECT_NAME=simple_cab

BASE_DIR=$(dirname $0)

set -e

docker-compose -p $PROJECT_NAME -f ${BASE_DIR}/docker-compose.yaml down --rmi 'local' --volumes --remove-orphans

docker-compose -p $PROJECT_NAME -f ${BASE_DIR}/docker-compose.yaml up -d mysql