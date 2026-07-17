#!/bin/bash

set -e

VERSION=$1

if [ -z "$VERSION" ]; then
    echo "Usage: ./deploy.sh <version>"
    exit 1
fi

./mvnw clean package -DskipTests
docker build -t austinrippee/run4spring:$VERSION .
docker tag austinrippee/run4spring:$VERSION austinrippee/run4spring:latest

read -p "Push version $VERSION? (y/n): " answer

if [[ "$answer" == "y" ]]; then
    docker push austinrippee/run4spring:$VERSION
    docker push austinrippee/run4spring:latest
fi

docker run --rm -p 8081:8081 austinrippee/run4spring:$VERSION