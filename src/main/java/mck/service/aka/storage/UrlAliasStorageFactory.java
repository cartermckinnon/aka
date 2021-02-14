package mck.service.aka.storage;

import io.dropwizard.setup.Environment;

/**
 * Defines the ability to instantiate a {@link UrlAliasStorage} implementation. A factory should be
 * available for each storage implementation.
 */
public interface UrlAliasStorageFactory {
  public UrlAliasStorage build(Environment e);
}
