#!/bin/bash

eval $(minikube docker-env)
./gradlew clean build jibDockerBuild --image metrics-demo:local