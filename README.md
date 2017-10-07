```
./docker.sh
```

```
java -jar myjar.jar --spring.config.location=D:\wherever\application.properties
```

```
curl "http://localhost:8080/trips/count?medallion=D7D598CD99978BD012A87A76A7C891B7&medallion=5455D5FF2BD94D10B304A15D4B7F2735&pickupDate=2013-12-01"
```

```
curl "http://localhost:8080/trips/count?medallion=D7D598CD99978BD012A87A76A7C891B7&medallion=5455D5FF2BD94D10B304A15D4B7F2735&pickupDate=2013-12-01&ignoreCache=true"
```

```
curl -XDELETE "http://localhost:8080/cache"
```