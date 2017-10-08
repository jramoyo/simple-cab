# simple-cab

### Prerequisites

- Java 8
- Maven 3.5.0

### Running the server

A dockerised version of the MySQL database is included (http://localhost:3307). To start the database, run (be patient): 

```sh
./docker.sh
```

Then package and run the Spring Boot application:

```
mvn package
java -jar server/target/server-1.0-SNAPSHOT.jar
```

When using an existing database, the Spring Boot application needs to be configured to point to the database. Create an `application.properties` file with the following content:

```properties
spring.datasource.url=jdbc:mysql://localhost/ny_cab_data?useSSL=false
spring.datasource.username=root
spring.datasource.password=
```

Then package and run the Spring Boot application:

```
mvn package
java -jar server/target/server-1.0-SNAPSHOT.jar --spring.config.location=<application.properties>
```

### REST API

#### `GET /trips/count`

Get the number of trips of the specified medallions for the given pickup date.

Query Parameters:
- `medallion` - string|array
- `pickupDate` - date (`YYYY-MM-dd`)
- `ignoreCache` - boolean (optional)

Returns:

```json
{
  "trips": {
    "5455D5FF2BD94D10B304A15D4B7F2735": 2,
    "D7D598CD99978BD012A87A76A7C891B7": 3
  }
}
```

Example:

```sh
curl "http://localhost:8080/trips/count?medallion=D7D598CD99978BD012A87A76A7C891B7&medallion=5455D5FF2BD94D10B304A15D4B7F2735&pickupDate=2013-12-01"
```

```sh
curl "http://localhost:8080/trips/count?medallion=D7D598CD99978BD012A87A76A7C891B7&medallion=5455D5FF2BD94D10B304A15D4B7F2735&pickupDate=2013-12-01&ignoreCache=true"
```

#### `DELETE /trips/count/cache`

Delete the cache.

Example:

```sh
curl -XDELETE "http://localhost:8080/trips/count/cache"
```