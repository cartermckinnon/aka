package mck.service.urlalias.storage.memory;

import io.dropwizard.setup.Environment;
import mck.service.urlalias.storage.UrlAliasStorage;
import mck.service.urlalias.storage.UrlAliasStorageFactory;

public class MemoryUrlAliasStorageFactory implements UrlAliasStorageFactory {
  /** Each call will return a new instance of the in-memory storage implementation. */
  @Override
  public UrlAliasStorage build(Environment e) {
    return new MemoryUrlAliasStorage();
  }
}
