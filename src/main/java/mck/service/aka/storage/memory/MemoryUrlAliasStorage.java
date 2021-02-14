package mck.service.aka.storage.memory;

import java.net.URI;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import mck.service.aka.storage.InstrumentedUrlAliasStorage;
import mck.service.aka.storage.UrlAliasStorage;
import mck.service.aka.util.LongCounter;
import mck.service.aka.util.Pair;

/** Naive in-memory implementation of {@link UrlAliasStorage}. Thread-safe. */
public class MemoryUrlAliasStorage extends InstrumentedUrlAliasStorage {

  private final Map<URI, String> urls;
  private final Map<String, Pair<URI, LongCounter>> aliases;

  public MemoryUrlAliasStorage() {
    this.aliases = new HashMap<>();
    this.urls = new HashMap<>();
  }

  @Override
  public synchronized Optional<String> getImpl(URI url) {
    return Optional.ofNullable(urls.get(url));
  }

  @Override
  public synchronized Optional<Pair<URI, Long>> getImpl(String alias) {
    return Optional.ofNullable(aliases.get(alias))
        .map(pair -> new Pair<>(pair.getLeft(), pair.getRight().value()));
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
    String previousAlias = urls.get(url);
    if (previousAlias == null || !previousAlias.equals(alias)) {
      // url wasn't mapped to an alias, or it was mapped to a different alias
      LongCounter usages = new LongCounter();
      urls.put(url, alias);
      var previousUrl = aliases.put(alias, new Pair<>(url, usages));
      if (previousUrl != null) {
        urls.remove(previousUrl.getLeft());
      }
      if (previousAlias != null) {
        aliases.remove(previousAlias);
      }
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
    var url = aliases.remove(alias);
    if (url != null) {
      urls.remove(url.getLeft());
      return true;
    }
    return false;
  }

  @Override
  protected synchronized long incrementUsagesImpl(String alias) {
    var url = aliases.get(alias);
    if (url != null) {
      return url.getRight().increment();
    }
    return -1;
  }
}
