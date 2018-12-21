# metrics-demo

Demo program to see capabilities of micrometer based metrics in a spring-boot app. Once running, the app will generate simulated load on its own so we can see the some metrics in action. Only the custom metrics are shown and none of the auto-configured metrics that spring-boot configures are exposed.

If you want to add to see some addition simulated metrics, the following can be invoked:

```
POST /metrics/timer/{name}/{maxWaitMillis}
```

This will generate a timer metric under the name `app.{name}` and the call will artificially take upto the `maxWaitMillis` (milliseconds) to execute

```
POST /metrics/metrics/counter/{name}
```

This will generate a counter metric under the name `app.{name}`


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

This will run the app with the metrics exposed on `/actuator/prometheus` endpoint

### Metrics logged on STDOUT

```
SPRING_PROFILES_ACTIVE=logging ./gradlew bootRun
```

This will run the app with the metrics summarized and printed on STDOUT every minute

