package mck.service.aka.storage.memory;

import mck.service.aka.storage.UrlAliasStorage;
import mck.service.aka.storage.UrlAliasStorageTest;

public class MemoryUrlAliasStorageTest extends UrlAliasStorageTest {
  @Override
  public UrlAliasStorage createImpl() {
    return new MemoryUrlAliasStorage();
  }
}
