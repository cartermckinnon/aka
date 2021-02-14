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
  public static final Histogram STORAGE_OPERATION_DURATION =
      Histogram.build()
          .namespace(MetricsConstants.NAMESPACE)
          .subsystem(MetricsConstants.SUBSYSTEM_STORAGE)
          .name("operation_duration_seconds")
          .help("Duration of operations on the storage implementation, in seconds.")
          .labelNames("implementation", "operation")
          .buckets(0.01, 0.05, 0.1, 0.25, 0.5, 1) // 10ms, 50ms, 100ms, 250ms, 500ms, 1s
          .register();
}
