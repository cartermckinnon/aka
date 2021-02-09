package mck.service.urlalias.metrics;

import io.prometheus.client.Counter;

public enum Counters {
  INSTANCE;

  /**
   * Tracks total number of HTTP requests.
   *
   * <p>Labels:
   *
   * <ul>
   *   <li>{@code method}: GET, POST, PUT, etc.
   *   <li>{@code path}: /foo, /bar, etc.
   *   <li>{@code status}: 200, 404, 500, etc.
   * </ul>
   */
  public static final Counter HTTP_REQUESTS =
      Counter.build()
          .namespace(MetricsConstants.NAMESPACE)
          .subsystem(MetricsConstants.SUBSYSTEM_HTTP_API)
          .name("requests")
          .help("Total number of requests since boot.")
          .labelNames("method", "path", "status")
          .register();

  /**
   * Tracks total number of authenticated HTTP requests.
   *
   * <p>Labels:
   *
   * <ul>
   *   <li>{@code method}: GET, POST, PUT, etc.
   *   <li>{@code path}: /foo, /bar, etc.
   *   <li>{@code status}: 200, 404, 500, etc.
   *   <li>{@code user}: admin, alan_turing, etc.
   * </ul>
   */
  public static final Counter HTTP_REQUESTS_WITH_AUTH =
      Counter.build()
          .namespace(MetricsConstants.NAMESPACE)
          .subsystem(MetricsConstants.SUBSYSTEM_HTTP_API_WITH_AUTH)
          .name("requests")
          .help("Total number of requests since boot.")
          .labelNames("method", "path", "status", "user")
          .register();
}
