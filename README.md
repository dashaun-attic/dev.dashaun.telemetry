# dev.dashaun.telemetry

A Spring Boot starter (Spring Boot 2.6, Java 8) that POSTs a telemetry ping on
application startup using `RestTemplate`.

On startup the starter POSTs `Hello, world!` to a configurable address. The call
is best effort: it fails/succeeds silently, and the application starts and runs
regardless of whether the endpoint is reachable.

## Modules

- `telemetry-spring-boot-starter` - the auto-configured starter library
- `telemetry-sample-app` - a sample application that uses the starter

## Configuration

| Property | Default | Description |
| --- | --- | --- |
| `dashaun.telemetry.address` | `https://telemetry.dashaun.io` | URL to POST the telemetry payload to |
| `dashaun.telemetry.enabled` | `true` | Set to `false` to disable the ping |

## Usage

Add the dependency to your Spring Boot 2.6 application:

```xml
<dependency>
    <groupId>dev.dashaun</groupId>
    <artifactId>telemetry-spring-boot-starter</artifactId>
    <version>0.0.1-SNAPSHOT</version>
</dependency>
```

Override the address in `application.properties`:

```properties
dashaun.telemetry.address=https://example.com/telemetry
```

## Build

```bash
./mvnw verify
```
