#!/usr/bin/env bash

asadmin="$1"
war="$2"

if [[ -z "$asadmin" || -z "$war" ]]; then
  script_name=$(basename "$0")
  echo "Usage: $script_name /path/to/asadmin /path/to/app.war"
  exit 1
fi

if ! [[ -x "$asadmin" ]]; then
  echo "Error: asadmin executable not found or not executable: $asadmin"
  exit 1
fi

if ! [[ -f "$war" ]]; then
  echo "Error: WAR file not found: $war"
  exit 1
fi

# Stop the server
"$asadmin" stop-domain &> /dev/null

# Start the server (domain1)
"$asadmin" start-domain domain1 || { echo "Failed to start domain"; exit 1; }

# Undeploy all apps except internal system apps
apps=$("$asadmin" list-applications | awk '{print $1}' | grep -v -E '^(__admingui|Command)$')
for app in $apps; do
    "$asadmin" undeploy "$app" &> /dev/null  || echo "Warning: Failed to undeploy $app"
done

# Deploy specified WAR with force option
"$asadmin" deploy --force "$war" || { echo "Failed to deploy application"; exit 1; }
