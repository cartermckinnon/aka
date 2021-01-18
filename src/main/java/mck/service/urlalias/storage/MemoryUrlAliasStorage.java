package mck.service.urlalias.storage;

import java.net.URI;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * Naive in-memory implementation of {@link UrlAliasStorage}.
 *
 * @author Carter McKinnon {@literal <cartermckinnon@gmail.com>}
 */
public class MemoryUrlAliasStorage implements UrlAliasStorage {

  private final Map<URI, String> aliases;
  private final Map<String, URI> urls;

  public MemoryUrlAliasStorage() {
    this.aliases = new HashMap<>();
    this.urls = new HashMap<>();
  }

  @Override
  public synchronized Optional<String> getAlias(URI url) {
    return Optional.ofNullable(aliases.get(url));
  }

  @Override
  public synchronized boolean deleteAlias(URI url) {
    String alias = aliases.remove(url);
    if (alias != null) {
      urls.remove(alias);
      return true;
    }
    return false;
  }

  @Override
  public synchronized boolean setAlias(URI url, String alias) {
    aliases.put(url, alias);
    return urls.put(alias, url) != null;
  }

  @Override
  public synchronized Optional<URI> getUrl(String alias) {
    return Optional.ofNullable(urls.get(alias));
  }

  @Override
  public synchronized Map<String, URI> getUrls() {
    return Collections.unmodifiableMap(urls);
  }
}
