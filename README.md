# Sample project to highlight issue after startup

Metrics are working as expected, however after starting the application a warning is logged:
```
This Gauge has been already registered 
```

## Issue

* After starting/deploying a kafka-app, metrics seem to be registered multiple times
```
2025-02-13T16:40:19.925+01:00  WARN 173203 --- [kafka-demo] [r-kafka-metrics] i.m.core.instrument.MeterRegistry        : This Gauge has been already registered (MeterId{name='kafka.consumer.coordinator.heartbeat.response.time.max', tags=[tag(application=kafka-demo),tag(client.id=consumer-kafka-demo-kafka-demo.retry-0-1),tag(kafka.version=3.8.1),tag(spring.id=kafkaConsumerFactory.consumer-kafka-demo-kafka-demo.retry-0-1)]}), the Gauge registration will be ignored. Note that subsequent logs will be logged at debug level.
```

## Expected behavior

* Registration happens only once
* No warnings are logged

## Reproducing the Issue

* Run `docker compose up -d` to start Zookeeper, Kafka and Prometheus
* Compile the demo application: `./mvnw clean install`
* Run the app: `java -jar target/kakfa-demo-0.0.1-SNAPSHOT.jar`
* Wait for about 1 minute
```
2025-02-13T16:40:19.925+01:00  WARN 173203 --- [kafka-demo] [r-kafka-metrics] i.m.core.instrument.MeterRegistry        : This Gauge has been already registered (MeterId{name='kafka.consumer.coordinator.heartbeat.response.time.max', tags=[tag(application=kafka-demo),tag(client.id=consumer-kafka-demo-kafka-demo.retry-0-1),tag(kafka.version=3.8.1),tag(spring.id=kafkaConsumerFactory.consumer-kafka-demo-kafka-demo.retry-0-1)]}), the Gauge registration will be ignored. Note that subsequent logs will be logged at debug level.
```
* Issue is only observed after startup
* Message varies (different metrics are reported)

