package mck.service.aka.metrics;

import io.prometheus.client.Histogram;

/** Singleton prometheus histograms. */
public enum Histograms {
  INSTANCE;

  /**
   * Tracks latency of storage operations.
   *
   * <p>Labels:
   *
   * <ul>
   *   <li>{@code implementation}: full class name of storage implementation.
   *   <li>{@code operation}: set, get, delete, etc.
   * </ul>
   */
  public static final Histogram STORAGE_OPERATIONS =
      Histogram.build()
          .namespace(MetricsConstants.NAMESPACE)
          .subsystem(MetricsConstants.SUBSYSTEM_STORAGE)
          .name("operations")
          .help("Operations on the storage implementation.")
          .labelNames("implementation", "operation")
          .register();
}
