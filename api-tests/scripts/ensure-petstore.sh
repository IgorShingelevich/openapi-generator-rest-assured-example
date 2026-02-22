#!/usr/bin/env sh
# Ensure Docker is running, image is present, and Petstore container is up.
# Variables must match pom.xml: petstore.port (80), petstore.basePath (/v2), petstore.image, petstore.container.name

set -e

if ! docker info >/dev/null 2>&1; then
  echo "[ERROR] Docker is not running or not in PATH. Start Docker and retry."
  exit 1
fi

docker pull swaggerapi/petstore

if ! docker start petstore 2>/dev/null; then
  docker run -d -e SWAGGER_BASE_PATH=/v2 -p 80:8080 --name petstore swaggerapi/petstore
  echo "[INFO] Petstore container started."
else
  echo "[INFO] Petstore container already running."
fi
