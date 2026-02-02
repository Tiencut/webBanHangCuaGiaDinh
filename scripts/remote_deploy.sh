#!/usr/bin/env bash
set -euo pipefail
# Remote deploy helper: place this script in the compose folder on the VPS
# Usage: ./remote_deploy.sh

echo "Deploy folder: $(pwd)"
echo "Pulling images..."
docker compose pull || true
echo "Starting containers..."
docker compose up -d --remove-orphans
echo "Pruning images..."
docker image prune -f || true

HEALTH_URL=${HEALTH_URL:-http://localhost:8080/actuator/health}
MAX_WAIT=${MAX_WAIT:-120}
SLEEP_INTERVAL=${SLEEP_INTERVAL:-5}
elapsed=0
echo "Waiting for health endpoint at $HEALTH_URL"
while [ $elapsed -lt $MAX_WAIT ]; do
  if curl -fsS "$HEALTH_URL" >/dev/null 2>&1; then
    echo "Health check passed"
    exit 0
  fi
  sleep $SLEEP_INTERVAL
  elapsed=$((elapsed + SLEEP_INTERVAL))
done
echo "Health check failed after ${MAX_WAIT}s" >&2
exit 2
