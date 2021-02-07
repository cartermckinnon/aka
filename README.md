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

Modify [`configuration.yaml`](configuration.yaml) as necessary, then:

```sh
scripts/run.sh
```

With Docker:
```sh
docker run \
  -p 8080:8080 \
  -v $PWD/configuration.yaml:/app/configuration/configuration.yaml \
  mckdev/url-alias-service
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
See the [management HTTP API implementation](src/main/java/mck/service/urlalias/resources/UrlAliasServiceApiResource.java) for more.

Use an alias:
```sh
curl -L http://localhost:8080/google
```
See the ["redirect" HTTP API implementation](src/main/java/mck/service/urlalias/resources/UrlAliasServiceRedirectResource.java) for more.

### Storage

By default, all data is kept in-memory. This means a "highly available" deployment of the service is not possible.
You can enable storage in Redis, if you want to be able to restart the service without losing your data, or if you want to deploy multiple instances of the service for redundancy.

See [`configuration.yaml`](configuration.yaml) for an example of a Redis storage configuration block.
