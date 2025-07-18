# Setup the Payara development server that is embedded through the payara-server-maven-plugin

## 1. Find the directory of the Payara development server

Start the development server with the Maven wrapper:

```bash
./mvnw payara-server:dev
```
In the log outputs on console you will see something like:
```bash
#!## LogManagerService.postConstruct : rootFolder=/tmp/payara-server-6.2025.6/payara6/glassfish
```

Then stop the development server by pressing CTRL+C!

## 2. Navigate to server's root directory

```bash
cd /tmp/payara-server-6.2025.6/payara6
```

## 3. Make `asadmin` executable

```bash
chmod +x bin/asadmin
```

## 4. Check if server is running

```bash
bin/asadmin list-domains
```

## 5. Start the server

```bash
bin/asadmin start-domain
```

## 6. Check if JDBC connection pool "myH2Pool" exists

```bash
bin/asadmin list-jdbc-connection-pools
```

## 7. Check if JDBC resource "jdbc/myJtaDataSource" exists

```bash
bin/asadmin list-jdbc-connection-resources
```

## 8. Create JDBC connection pool "myH2Pool"

```bash
bin/asadmin create-jdbc-connection-pool \
--datasourceclassname=org.h2.jdbcx.JdbcDataSource \
--restype=javax.sql.DataSource \
--property user=sa:URL="jdbc\:h2\:mem\:todos\;DB_CLOSE_DELAY\=-1" \
myH2Pool 
```

## 9. Create JDBC resource "jdbc/myJtaDataSource"

```bash
bin/asadmin create-jdbc-resource \
--connectionpoolid=myH2Pool \
jdbc/myJtaDataSource    
```

## 10. Stop the server

```bash
bin/asadmin stop-domain
```

## 11. Edit domain.xml

E.g., with `nano`:

```bash
nano glassfish/domains/domain1/config/domain.xml
```

Change connection pool "myH2Pool" to:
```xml
<jdbc-connection-pool datasource-classname="org.h2.jdbcx.JdbcDataSource" name="myH2Pool" res-type="javax.sql.DataSource">
  <property name="user" value="sa"></property>
  <property name="password" value=""></property>
  <property name="URL" value="jdbc:h2:mem:todos;DB_CLOSE_DELAY=-1"></property>
</jdbc-connection-pool>
```

So just add the line `<property name="password" value=""></property>`

## 12. Star the server

```bash
bin/asadmin start-domain
```

## 13. Execute ping test with connection pool "myH2Pool"

```bash
bin/asadmin ping-connection-pool myH2Pool
```

## 14. Stop the server

```bash
bin/asadmin stop-domain
```

## 15. Use the development server
 
After that you should be able to start the development server with the Maven wrapper:

```bash
./mvnw payara-server:dev
```










