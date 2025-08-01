#!/usr/bin/env bash

directory="$HOME/bin/payara6/glassfish/domains/domain1/logs"
log_file="server.log"

cd "$directory" || { echo "\"$directory\" not found." ; exit 1 ; }

[ ! -f "$log_file" ] && { echo "$log_file not found in $directory." ; exit 1 ; }

echo "=> Press CTRL+C to stop the output!"
echo "-----------------------------------"
echo

tail -f "$log_file"

