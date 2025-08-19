# Setup the Payara development server that is embedded through the payara-server-maven-plugin

## 1. Make the Maven wrapper `mvnw` executable

```bash
chmod +x mvnw
```

## 2. Find the directory of the Payara development server

Start the development server with the Maven wrapper:

```bash
./mvnw payara-server:dev
```

It doesn't matter if it crashes :-)

Look at the log outputs on console and you will see something like:

```bash
#!## LogManagerService.postConstruct : rootFolder=/tmp/payara-server-6.2025.6/payara6/glassfish
```

So we found the directory of the Payara development server, in my example it's `/tmp/payara-server-6.2025.6/payara6`.

If the server is still running, stop it now by pressing CTRL+C.

## 3. Change the `domain.xml` of the server

The full path of the `domain.xml` file is: `<Path to Payara>/glassfish/domains/domain1/config/domain.xml`

So in my example it's: `/tmp/payara-server-6.2025.6/payara6/glassfish/domains/domain1/config/domain.xml`.

In the `domain.xml` file you must add configurations to the `<resources>` part, so insert the following just before the
closing tag `</resources>`:

```xml

<jdbc-connection-pool datasource-classname="org.h2.jdbcx.JdbcDataSource" name="myH2Pool"
                      res-type="javax.sql.DataSource">
    <property name="password" value=""></property>
    <property name="user" value="sa"></property>
    <property name="URL" value="jdbc:h2:file:~/database/todos;DB_CLOSE_DELAY=-1"></property>
</jdbc-connection-pool>
<jdbc-resource pool-name="myH2Pool" jndi-name="jdbc/myJtaDataSource"></jdbc-resource>
```

By that you created the **JDBC connection pool** "myH2Pool" and the **JDBC resource** "jdbc/myJtaDataSource" inside the
server.

## 4. Start the development server and deploy the application

```bash
./mvnw clean package payara-server:dev
```

## Other things that you can do regarding the Payara development server

After changing into the Payara development server directory (e.g. `cd /tmp/payara-server-6.2025.6/payara6`) ...

### 1. Make `asadmin` executable

```bash
chmod +x bin/asadmin
```

### 2. Check if the server is running

```bash
bin/asadmin list-domains
```

### 3. Start the server

```bash
bin/asadmin start-domain
```

### 4. Check if JDBC connection pool "myH2Pool" exists

```bash
bin/asadmin list-jdbc-connection-pools
```

### 5. Execute ping test with connection pool "myH2Pool"

```bash
bin/asadmin ping-connection-pool myH2Pool
```

### 6. Check if JDBC resource "jdbc/myJtaDataSource" exists

```bash
bin/asadmin list-jdbc-resources
```

### 7. You could create a JDBC connection pool

```bash
bin/asadmin create-jdbc-connection-pool \
--datasourceclassname=org.h2.jdbcx.JdbcDataSource \
--restype=javax.sql.DataSource \
--property user=sa:URL="jdbc\:h2\:file\:~/database/todos\;DB_CLOSE_DELAY\=-1" \
myH2Pool 
```

> This is not recommended here because the property "password" with value "" (empty password) can't be set!

### 8. You could create a JDBC resource

```bash
bin/asadmin create-jdbc-resource \
--connectionpoolid=myH2Pool \
jdbc/myJtaDataSource    
```

### 9. Stop the server

```bash
bin/asadmin stop-domain
```









