package mck.service.urlalias.storage;

import static org.assertj.core.api.Assertions.assertThat;

import java.net.URI;
import org.junit.jupiter.api.Test;

public abstract class UrlAliasStorageTest {
  /**
   * @return new instance of the implementation to be tested. Each test method will call this once;
   *     ensure that the object that is returned is meant to be used by only one test method.
   */
  public abstract UrlAliasStorage createImpl();

  @Test
  public void getAndSet() {
    UrlAliasStorage storage = createImpl();
    URI url = URI.create("http://mckinnon.xyz");
    String alias = "alias";

    // set for the first time
    storage.set(url, alias);
    assertThat(storage.get(url)).isPresent().get().isEqualTo(alias);
    assertThat(storage.get(alias)).isPresent().get().isEqualTo(url);
    assertThat(storage.getUrls()).containsExactly(url);
    assertThat(storage.getAliases()).containsExactly(alias);

    // update the alias, which should remove the old one
    String aliasUpdated = "aliasUpdated";
    storage.set(url, aliasUpdated);
    assertThat(storage.get(url)).isPresent().get().isEqualTo(aliasUpdated);
    assertThat(storage.get(aliasUpdated)).isPresent().get().isEqualTo(url);
    assertThat(storage.get(alias)).isEmpty();
    assertThat(storage.getUrls()).containsExactly(url);
    assertThat(storage.getAliases()).containsExactly(aliasUpdated);
  }

  @Test
  public void delete() {
    UrlAliasStorage storage = createImpl();
    URI url = URI.create("http://mckinnon.xyz");
    String alias = "alias";

    // has not been set yet
    assertThat(storage.delete(url)).isFalse();
    assertThat(storage.delete(alias)).isFalse();

    // delete by URL
    storage.set(url, alias);
    assertThat(storage.delete(url)).isTrue();
    assertThat(storage.delete(alias)).isFalse();
    assertThat(storage.get(url)).isEmpty();
    assertThat(storage.get(alias)).isEmpty();

    // delete by alias
    storage.set(url, alias);
    assertThat(storage.delete(alias)).isTrue();
    assertThat(storage.delete(url)).isFalse();
    assertThat(storage.get(url)).isEmpty();
    assertThat(storage.get(alias)).isEmpty();
  }
}
