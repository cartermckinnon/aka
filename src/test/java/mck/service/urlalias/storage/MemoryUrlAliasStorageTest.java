package mck.service.urlalias.storage;

/** @author Carter McKinnon {@literal <cartermckinnon@gmail.com>} */
public class MemoryUrlAliasStorageTest extends UrlAliasStorageTest {
  @Override
  public UrlAliasStorage createImpl() {
    return new MemoryUrlAliasStorage();
  }
}
