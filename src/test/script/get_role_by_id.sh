#!/usr/bin/env bash
set -euo pipefail

# Check if an argument was provided
if [ $# -ne 1 ]; then
  echo "Usage: $0 <role_id>"
  exit 1
fi

role_id="$1"

# Check if role_id is an integer
if ! [[ "$role_id" =~ ^[0-9]+$ ]]; then
  echo "Invalid role ID: $role_id"
  exit 1
fi

TOKEN=$(./register.sh alice)

if [ -z "$TOKEN" ]; then
  echo "TOKEN is missing"
  exit 1
fi

API_URL="http://localhost:8080/todos/api/roles/$role_id"

response_entity=$(curl -sS \
  -X GET "$API_URL" \
  -H "Authorization: Bearer $TOKEN")

# If the response is not a JSON object, print it. Otherwise, use jq to pretty-print it.
if ! echo "$response_entity" | jq 2>/dev/null; then
  echo "$response_entity"
fi
