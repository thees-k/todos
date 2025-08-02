#!/usr/bin/env bash

asadmin="$HOME/bin/payara6/bin/asadmin"
h2_library="$HOME/bin/payara6/h2db/bin/h2.jar"

if ! [[ -x "$asadmin" ]]; then
  echo "Error: asadmin executable not found or not executable: $asadmin"
  exit 1
fi

if ! [[ -f "$h2_library" ]]; then
  echo "Error: h2.jar not found: $h2_library"
  exit 1
fi

echo "=> Press CTRL+C to stop!"
echo "------------------------"
echo

# Stop the server
"$asadmin" stop-domain &> /dev/null

# Start the web console
java -jar "$h2_library"
