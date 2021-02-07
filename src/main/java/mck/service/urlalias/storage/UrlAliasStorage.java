package mck.service.urlalias.storage;

import java.net.URI;
import java.util.Collection;
import java.util.Optional;

/** @author Carter McKinnon {@literal <cartermckinnon@gmail.com>} */
public interface UrlAliasStorage {
  /**
   * @param url
   * @return the alias for the URL, if it exists.
   */
  public Optional<String> get(URI url);

  /**
   * @param alias
   * @return the URL for an alias, if it exists.
   */
  public Optional<URI> get(String alias);

  /** @return all URLs. */
  public Collection<URI> getUrls();

  /** @return all aliases. */
  public Collection<String> getAliases();

  /**
   * @param url
   * @param alias
   */
  public void set(URI url, String alias);

  /**
   * @param alias
   * @return true if the alias was deleted; false if no alias existed.
   */
  public boolean delete(String alias);

  /**
   * @param url
   * @return true if the URL was deleted; false if it did not exist.
   */
  public boolean delete(URI url);
}
