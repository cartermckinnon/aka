package mck.service.urlalias.storage;

import io.dropwizard.setup.Environment;

public interface UrlAliasStorageFactory {
  public UrlAliasStorage build(Environment e);
}
