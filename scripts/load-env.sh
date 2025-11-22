#!/usr/bin/env bash
# Load .env into the current shell (bash)
ENV_FILE="$(dirname "$0")/../.env"
[ -f "$ENV_FILE" ] || ENV_FILE=".env"
if [ -f "$ENV_FILE" ]; then
  set -a
  # shellcheck disable=SC1090
  . "$ENV_FILE"
  set +a
  echo "Loaded $ENV_FILE"
else
  echo "No .env found"
  exit 1
fi
