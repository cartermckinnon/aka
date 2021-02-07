# URL Alias Service

Naive HTTP service for URL aliasing/redirecting.

TinyURL-like services are generally annoying to use by requiring registration, consumption of ads, or over-complexity. This is a comparable service you can run yourself. It provides a bare-bones feature set, basic authentication, and optional, external storage in Redis.

### :warning: **Warning** :warning:

You should always run this service over TLS because of the use of HTTP basic authentication.

### Pre-requisites

- JDK 15
- Maven
- Docker

### Build

```sh
scripts/build.sh
```

### Run

Modify `configuration.yaml` as necessary, then:

```sh
scripts/run.sh
```

### Usage

Create an alias:
```sh
curl \
  -X POST \
  -H "Content-Type: application/json" \
  -d '{"url":"https://google.com", "alias":"google"}' \
  -u admin:password \
  http://localhost:8080/api/set
```

Use an alias:
```sh
curl -L http://localhost:8080/google
```
