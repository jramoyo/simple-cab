# simple-cab

### Prerequisites

- Java 8
- Maven 3.5.0

### Building the application

```sh
mvn clean package
```

### Running the end-to-end application

#### 1. Starting the MySQL database

A dockerised version of the MySQL database is included (http://localhost:3307). 

To start the database, run (be patient): 

```sh
./bin/database.sh
```

#### 2. Running the server

Start the Spring Boot application:

```
./bin/server.sh
```

By default, the server is configured to run against the dockerised database. When using an existing database, it needs to be reconfigured. 

Create an `application.properties` file with the following content:

```properties
spring.datasource.url=jdbc:mysql://localhost/ny_cab_data?useSSL=false
spring.datasource.username=root
spring.datasource.password=
```

Then start the Spring Boot application:

```
./bin/server.sh --spring.config.location=application.properties
```

#### 3. Running the client

Display help:

```sh
./bin/client.sh help
```

To get the number of trips of the specified medallions for the given pickup date:

```sh
./bin/client.sh trip-count "http://localhost:8080" D7D598CD99978BD012A87A76A7C891B7,5455D5FF2BD94D10B304A15D4B7F2735 2013-12-01 --ignore-cache
```

To delete the server cache:

```sh
./bin/client.sh delete-cache "http://localhost:8080"
```

### REST API

#### `GET /trips/count`

Get the number of trips for the specified medallions and the given pickup date.

Query Parameters:
- `medallion` - string (array)
- `pickupDate` - date (`yyyy-MM-dd`)
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