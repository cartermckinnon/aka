package mck.service.urlalias.storage;

import java.net.URI;
import java.util.Map;
import java.util.Optional;

/** @author Carter McKinnon {@literal <cartermckinnon@gmail.com>} */
public interface UrlAliasStorage {
  /**
   * @param url
   * @return the alias for the URL, if it exists.
   */
  public Optional<String> getAlias(URI url);

  /**
   * @param url
   * @return true if the URL's alias was deleted; false if no alias existed.
   */
  public boolean deleteAlias(URI url);

  /**
   * @param url
   * @param alias
   * @return true if an alias already existed for this URL, and it was updated; false if it did not
   *     exist. Note that the alias will be set regardless.
   */
  public boolean setAlias(URI url, String alias);

  /**
   * @param alias
   * @return the URL for an alias, if it exists.
   */
  public Optional<URI> getUrl(String alias);

  /** @return aliases and their target URLs. */
  public Map<String, URI> getUrls();
}
