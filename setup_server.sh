#!/usr/bin/env bash

POOL="myH2Pool"
RESOURCE="jdbc/myJtaDataSource"
DOMAIN_XML="glassfish/domains/domain1/config/domain.xml"

command -v "sed" &> /dev/null || { echo "Command \"sed\" not found." ; exit 1 ; }

path_to_server="$1"
script_name=$(basename "$0")

if [[ -z "$path_to_server" ]]; then
  echo "Usage: $script_name /path/to/server"
  exit 1
fi

asadmin="$path_to_server/bin/asadmin"

if ! [[ -x "$asadmin" ]]; then
  echo "Error: asadmin executable not found or not executable: $asadmin"
  echo "Usage: $script_name /path/to/server"
  exit 1
fi

"$asadmin" stop-domain &> /dev/null

"$asadmin" start-domain domain1 || { echo "Failed to start the server"; exit 1; }

"$asadmin" create-jdbc-connection-pool \
--datasourceclassname=org.h2.jdbcx.JdbcDataSource \
--restype=javax.sql.DataSource \
--property user=sa:URL="jdbc\:h2\:file\:~/database/todos\;DB_CLOSE_DELAY\=-1" \
"$POOL"

if [ $? -ne 0 ]; then
  echo "Creating pool \"$POOL\" failed. Does it already exist?"
  exit 1
fi

"$asadmin" create-jdbc-resource --connectionpoolid=myH2Pool "$RESOURCE"

if [ $? -ne 0 ]; then
  echo "Creating resource \"$RESOURCE\" failed. Does it already exist?"
  exit 1
fi

"$asadmin" stop-domain &> /dev/null  || { echo "WARNING: Failed to stop the server!" ; exit 1; }

line_to_add='      <property name="password" value=""></property>'
target_file="$path_to_server/$DOMAIN_XML"

sed -i "/jdbc-connection-pool.*$POOL/ a\\
$line_to_add
" "$target_file"

if [ $? -ne 0 ]; then
  echo "Updating of \"$target_file\" failed."
  exit 1
fi

echo -e "\nServer setup successfully executed!"
