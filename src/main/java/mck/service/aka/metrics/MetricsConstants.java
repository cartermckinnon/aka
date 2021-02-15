package mck.service.aka.metrics;

/** Prometheus constant values. */
public enum MetricsConstants {
  INSTANCE;

  protected static final String NAMESPACE = "aka";

  protected static final String SUBSYSTEM_STORAGE = "storage";

  protected static final String SUBSYSTEM_HTTP = "http";
}
