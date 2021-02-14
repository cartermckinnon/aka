package mck.service.aka.storage;

import java.net.URI;
import java.util.Collection;
import java.util.Optional;
import mck.service.aka.util.Pair;

/**
 * The data structure which contains URLs and their aliases.
 *
 * <p>URLs should only be mapped to a single alias, and vice versa.
 */
public interface UrlAliasStorage {
  /**
   * @param url
   * @return the alias for the URL, if it exists.
   */
  public Optional<String> get(URI url);

  /**
   * @param alias
   * @return the URL for an alias and the number of usages of the alias; if it exists.
   */
  public Optional<Pair<URI, Long>> get(String alias);

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

  /**
   * Increment the usage counter for an alias.
   *
   * @param alias
   * @return number of times this alias has been used, including this occurrance.
   */
  public long incrementUsages(String alias);
}
