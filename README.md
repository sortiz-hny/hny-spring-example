# hny-spring-example
A dummy application to demo the spring-boot/honeycomb integration

# Prerequisites
Download the honeycomb-opentelemetry-java jar and install it in the root directory of this project.
https://github.com/honeycombio/honeycomb-opentelemetry-java#agent-usage

# Setup
```
mvn clean install
```

# Run it
Modify start_service.sh with the correct API Keys, then use: 
```
./start_service.sh
```

# Test it
```
curl http://localhost:9091/beer
```
