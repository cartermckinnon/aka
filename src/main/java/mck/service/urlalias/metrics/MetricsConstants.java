package mck.service.urlalias.metrics;

public enum MetricsConstants {
  INSTANCE;

  protected static final String NAMESPACE = "url_alias_service";

  protected static final String SUBSYSTEM_STORAGE = "storage";

  protected static final String SUBSYSTEM_HTTP_API = "http_api";

  protected static final String SUBSYSTEM_HTTP_API_WITH_AUTH = "http_api_with_auth";
}
