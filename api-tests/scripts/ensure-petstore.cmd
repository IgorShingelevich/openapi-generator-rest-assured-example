@echo off
REM Ensure Docker is running, image is present, and Petstore container is up.
REM Variables must match pom.xml: petstore.port (80), petstore.basePath (/v2), petstore.image, petstore.container.name

docker info >nul 2>&1
if errorlevel 1 (
  echo [ERROR] Docker is not running or not in PATH. Start Docker Desktop and retry.
  exit /b 1
)

docker pull swaggerapi/petstore
if errorlevel 1 (
  echo [ERROR] Failed to pull swaggerapi/petstore
  exit /b 1
)

docker start petstore >nul 2>&1
if errorlevel 1 (
  docker run -d -e SWAGGER_BASE_PATH=/v2 -p 80:8080 --name petstore swaggerapi/petstore
  if errorlevel 1 (
    echo [ERROR] Failed to start or create Petstore container
    exit /b 1
  )
  echo [INFO] Petstore container started.
) else (
  echo [INFO] Petstore container already running.
)
