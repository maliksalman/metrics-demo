# metrics-demo

Demo program to see capabilities of micrometer based metrics in a spring-boot app. Once running, the app will generate simulated load on its own so we can see some metrics in action. By default, two metrics are emitted - `app.foo` (a timer) and `app.bar` (a counter). All metrics will be exposed with two dimensions:

- `env` - set to `dev`
- `name` - set to `demo`

If you want to add to see some addition simulated metrics, the following can be invoked:

```
POST /metrics/timer/{metric-name}/{region}/{maxWaitMillis}
```

This will generate a timer metric under the name `app.{metric-name}` and the call will artificially take upto the `maxWaitMillis` (milliseconds) to execute. This metric will also add the `region` dimension set to value passed in the URL.

```
POST /metrics/metrics/counter/{metric-name}/{region}
```

This will generate a counter metric under the name `app.{name}`. This metric will also add the `region` dimension set to value passed in the URL.


## Running

```
./gradlew bootRun
```

This will run the app with none of the metrics exposed

### Metrics on `/actuator/metrics` endpoint

```
SPRING_PROFILES_ACTIVE=metrics ./gradlew bootRun
```

This will run the app with the metrics exposed on `/actuator/metrics` endpoint

### Metrics on `/actuator/prometheus` endpoint

```
SPRING_PROFILES_ACTIVE=prometheus ./gradlew bootRun
```

This will run the app with the metrics exposed on `/actuator/prometheus` endpoint. Prometheus integration with micrometer is pull based which means prometheus will pull the metrics using the endpoint on spring-boot app. 

### Metrics logged on STDOUT

```
SPRING_PROFILES_ACTIVE=logging ./gradlew bootRun
```

This will run the app with the metrics summarized and printed on STDOUT every minute. Only the custom metrics will be shown and none of the auto-configured metrics that spring-boot configures are exposed on STDOUT. 

### Metrics logged on Elasticsearch

```
SPRING_PROFILES_ACTIVE=elastic ./gradlew bootRun
```

This will run the app with the metrics summarized and sent to elasticsearch every 30 seconds. The Elasticsearch is assumed to be running on `http://localhost:9200`. Elasticsearch integration with micrometer is push based meaning that the spring-boot app pushes the metrics to Elasticsearch. The Elasticesearch host and other prameters are changeable via properties in `application-elastic.yml`. 
