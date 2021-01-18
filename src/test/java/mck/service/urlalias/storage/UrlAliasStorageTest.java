package mck.service.urlalias.storage;

import static org.assertj.core.api.Assertions.assertThat;

import java.net.URI;
import org.junit.jupiter.api.Test;

/** @author Carter McKinnon {@literal <cartermckinnon@gmail.com>} */
public abstract class UrlAliasStorageTest {
  /**
   * @return new instance of the implementation to be tested. Each test method will call this once;
   *     ensure that the object that is returned is meant to be used by only one test method.
   */
  public abstract UrlAliasStorage createImpl();

  @Test
  public void setAlias() {
    UrlAliasStorage storage = createImpl();
    URI url = URI.create("http://mckinnon.xyz");
    assertThat(storage.setAlias(url, "alias")).isFalse();
    assertThat(storage.setAlias(url, "alias")).isTrue();
  }

  @Test
  public void getAlias() {
    UrlAliasStorage storage = createImpl();
    URI url = URI.create("http://mckinnon.xyz");
    assertThat(storage.getAlias(url)).isEmpty();
    storage.setAlias(url, "alias");
    assertThat(storage.getAlias(url)).isPresent().get().isEqualTo(("alias"));
    storage.deleteAlias(url);
    assertThat(storage.getAlias(url)).isEmpty();
  }

  @Test
  public void getUrl() {
    UrlAliasStorage storage = createImpl();
    URI url = URI.create("http://mckinnon.xyz");
    String alias = "alias";
    assertThat(storage.getUrl(alias)).isEmpty();
    storage.setAlias(url, alias);
    assertThat(storage.getUrl(alias)).isPresent().get().isEqualTo(url);
    storage.deleteAlias(url);
    assertThat(storage.getUrl(alias)).isEmpty();
  }
}
