# AKA

Naive HTTP service for URL aliasing/redirecting.

TinyURL-like services are generally annoying to use by requiring registration, consumption of ads, or over-complexity. This is a comparable service you can run yourself. It provides a bare-bones feature set, basic authentication, and optional, external storage in Redis.

### :warning: **Warning** :warning:

You should always run this service over TLS because of the use of HTTP basic authentication.

### Pre-requisites

- JDK 15
- Maven
- Docker

---

### Build

```sh
scripts/build.sh
```

---

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
  mckdev/aka
```

---

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
See the [management HTTP API implementation](src/main/java/mck/service/aka/resources/ApiResource.java) for more.

Use an alias:
```sh
curl -L http://localhost:8080/google
```
See the ["redirect" HTTP API implementation](src/main/java/mck/service/aka/resources/RedirectResource.java) for more.

---

### Storage

By default, all data is kept in-memory. This means a "highly available" deployment of the service is not possible.
You can enable storage in Redis, if you want to be able to restart the service without losing your data, or if you want to deploy multiple instances of the service for redundancy.

See [`configuration.yaml`](configuration.yaml) for an example of a Redis storage configuration block.

---

### Kubernetes

An example set of Kubernetes resources is provided in [k8s](k8s/). A single-node Redis service is included.

To use the included LetsEncrypt-based automatic TLS, your cluster needs to have [cert-manager](https://cert-manager.io/docs/installation/kubernetes/) installed.
You probably want to use the LetsEncrypt staging environment while you're getting things going, to avoid hitting any rate-limits. See the comment in [letsencrypt-issuer.yaml](k8s/letsencrypt-issuer.yaml).

1. Modify [configmap.yaml](k8s/configmap.yaml) to set a proper username and password.

1. Set your host in [ingress.yaml](k8s/ingress.yaml).
**Note** that these manifests assume that your cluster's ingress controller is [Traefik](https://github.com/traefik/traefik/).
Traefik is the ingress controller used by [k3s](https://k3s.io/), my Kubernetes distribution of choice for projects like these.
If you're using a different ingress controller, modify the annotations in [ingress.yaml](k8s/ingress.yaml) and `ingress.class` in [letsencrypt-issuer.yaml](k8s/letsencrypt-issuer.yaml) as necessary.

1. Set your email address in [letsencrypt-issuer.yaml](k8s/letsencrypt-issuer.yaml).

1. Deploy the resources: `kubectl create -f k8s/`
