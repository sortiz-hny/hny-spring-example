# hny-spring-example
A dummy application to demo the spring-boot/honeycomb integration

# Setup
```
mvn clean install
```

# Run it
```
java -jar target/hny-example-0.0.1-SNAPSHOT.jar --honeycomb.beeline.write-key=<my write key> --honeycomb.beeline.api-host=http://0:8081
```

# Test it
```
curl http://localhost:9090/quotes
```