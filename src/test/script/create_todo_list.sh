#!/usr/bin/env bash
set -euo pipefail

TOKEN=$(./register.sh alice)

if [ -z "$TOKEN" ]; then
  echo "TOKEN is missing"
  exit 1
fi

API_URL="http://localhost:8080/todos/api/todolists"

tmp=$(mktemp --suffix=.json)
trap 'rm -f "$tmp"' EXIT

# Put long JSON in a separate file to avoid quoting issues.
cat >"$tmp" <<'JSON'
{
  "id": 5,
  "ownerId": 2,
  "name": "Shopping List",
  "description": "List of items to buy",
  "isPublic": false,
  "isDone": false,
  "createdAt": "2024-01-01T12:00:00Z",
  "updatedAt": "2024-01-02T15:30:00Z",
  "updatedById": 1,
  "taskIds": [ 1 ]
}
JSON

# Print JSON that will be sent:
# cat "$tmp"

response_entity=$(curl -sS \
  -X POST "$API_URL" \
  -H "Authorization: Bearer $TOKEN" \
  -H "Content-Type: application/json" \
  --data-binary @"$tmp")

# If the response is not a JSON object, print it. Otherwise, use jq to pretty-print it.
if ! echo "$response_entity" | jq 2>/dev/null; then
  echo "$response_entity"
fi
