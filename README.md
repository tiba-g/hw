## This is a Spring Boot API application which represents a simple banking backend.

The authentication is deliberately turned off in Spring Boot Security.

### Migration
The models are being generated with Liquibase (resources/db/changelog)
Liquibase properties file contains how to run the LB commands.

### Run the application
To run the application you need a
- PostgreSQL Database on port 5436 (configurable in application-dev.properties)
- Run the application with the 'dev' spring profile
- Sample requests can be found in request.http file

### Run the application with docker compose

You can run the application using 'docker-compose up'
- this will start up the SB app and the DB instance in two separate containers