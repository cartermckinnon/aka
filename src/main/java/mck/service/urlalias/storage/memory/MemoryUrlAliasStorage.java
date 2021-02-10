package mck.service.urlalias.storage.memory;

import java.net.URI;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import mck.service.urlalias.storage.InstrumentedUrlAliasStorage;
import mck.service.urlalias.storage.UrlAliasStorage;

/** Naive in-memory implementation of {@link UrlAliasStorage}. Thread-safe. */
public class MemoryUrlAliasStorage extends InstrumentedUrlAliasStorage {

  private final Map<URI, String> urls;
  private final Map<String, URI> aliases;

  public MemoryUrlAliasStorage() {
    this.aliases = new HashMap<>();
    this.urls = new HashMap<>();
  }

  @Override
  public synchronized Optional<String> getImpl(URI url) {
    return Optional.ofNullable(urls.get(url));
  }

  @Override
  public synchronized Optional<URI> getImpl(String alias) {
    return Optional.ofNullable(aliases.get(alias));
  }

  @Override
  public synchronized Collection<URI> getUrlsImpl() {
    return Collections.unmodifiableCollection(urls.keySet());
  }

  @Override
  public synchronized Collection<String> getAliasesImpl() {
    return Collections.unmodifiableCollection(aliases.keySet());
  }

  @Override
  public synchronized void setImpl(URI url, String alias) {
    String previousAlias = urls.put(url, alias);
    if (previousAlias != null) {
      aliases.remove(previousAlias);
    }
    URI previousUrl = aliases.put(alias, url);
    if (previousUrl != null) {
      urls.remove(previousUrl);
    }
  }

  @Override
  public synchronized boolean deleteImpl(URI url) {
    String alias = urls.remove(url);
    if (alias != null) {
      aliases.remove(alias);
      return true;
    }
    return false;
  }

  @Override
  public synchronized boolean deleteImpl(String alias) {
    URI url = aliases.remove(alias);
    if (url != null) {
      urls.remove(url);
      return true;
    }
    return false;
  }
}
