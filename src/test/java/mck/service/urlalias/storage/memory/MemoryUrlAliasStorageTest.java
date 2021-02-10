package mck.service.urlalias.storage.memory;

import mck.service.urlalias.storage.UrlAliasStorage;
import mck.service.urlalias.storage.UrlAliasStorageTest;

public class MemoryUrlAliasStorageTest extends UrlAliasStorageTest {
  @Override
  public UrlAliasStorage createImpl() {
    return new MemoryUrlAliasStorage();
  }
}
