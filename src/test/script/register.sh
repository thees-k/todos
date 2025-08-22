#!/usr/bin/env bash
set -euo pipefail

# Default server
server="localhost"

# Parse options
while getopts ":s:" opt; do
  case $opt in
  s)
    server="$OPTARG"
    ;;
  \?)
    echo "Invalid option: -$OPTARG" >&2
    exit 1
    ;;
  :)
    echo "Option -$OPTARG requires an argument." >&2
    exit 1
    ;;
  esac
done
shift $((OPTIND - 1))

# Check if jq is installed
if ! command -v jq >/dev/null 2>&1; then
  echo "Error: jq is not installed. Please install jq to run this script." >&2
  exit 1
fi

# Check if at least one argument is provided (username)
if [ $# -lt 1 ]; then
  echo "Usage: $0 [-s server] username [password]"
  echo "If you don't specify the password, the username will be used as password as well."
  exit 1
fi

user_name="$1"
password="${2:-$1}"

# get token (curl -s to be silent; jq -r for raw string without quotes)
token_object=$(curl -sS \
  --header "Content-Type: application/json" \
  --request POST \
  --data "{\"username\":\"$user_name\",\"password\":\"$password\"}" \
  "http://$server:8080/todos/api/auth/login")

if ! echo "$token_object" | jq -r '.token' 2>/dev/null; then
  exit 1
fi
