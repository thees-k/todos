#!/usr/bin/env bash
set -euo pipefail

TOKEN=$(./register.sh alice)

if [ -z "$TOKEN" ]; then
  echo "TOKEN is missing"
  exit 1
fi

API_URL="http://localhost:8080/todos/api/roles"

response_entity=$(curl -sS \
  -X GET "$API_URL" \
  -H "Authorization: Bearer $TOKEN")

# If the response is not a JSON object, print it. Otherwise, use jq to pretty-print it.
if ! echo "$response_entity" | jq 2>/dev/null; then
  echo "$response_entity"
fi
