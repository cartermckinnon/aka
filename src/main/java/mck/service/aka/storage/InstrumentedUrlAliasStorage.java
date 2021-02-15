package mck.service.aka.storage;

import java.net.URI;
import java.util.Collection;
import java.util.Optional;
import mck.service.aka.metrics.Histograms;
import mck.service.aka.util.Pair;

/** Implements instrumentation for all methods of {@code UrlAliasStorage}. */
public abstract class InstrumentedUrlAliasStorage implements UrlAliasStorage {

  /** @see UrlAliasStorage#get(URI) */
  protected abstract Optional<String> getImpl(URI url);

  /** @see UrlAliasStorage#get(String) */
  protected abstract Optional<Pair<URI, Long>> getImpl(String alias);

  /** @see UrlAliasStorage#set(URI, String) */
  protected abstract void setImpl(URI url, String alias);

  /** @see UrlAliasStorage#getUrls() */
  protected abstract Collection<URI> getUrlsImpl();

  /** @see UrlAliasStorage#getAliases() */
  protected abstract Collection<Pair<String, Long>> getAliasesImpl();

  /** @see UrlAliasStorage#delete(String) */
  protected abstract boolean deleteImpl(String alias);

  /** @see UrlAliasStorage#delete(URI) */
  protected abstract boolean deleteImpl(URI url);

  /** @see UrlAliasStorage#incrementUsages(String) */
  protected abstract long incrementUsagesImpl(String alias);

  private final String implClass = this.getClass().getName();

  @Override
  public final Optional<String> get(URI url) {
    return Histograms.STORAGE_OPERATION_DURATION
        .labels(implClass, "getAliasByUrl")
        .time(() -> getImpl(url));
  }

  @Override
  public final Optional<Pair<URI, Long>> get(String alias) {
    return Histograms.STORAGE_OPERATION_DURATION
        .labels(implClass, "getUrlByAlias")
        .time(() -> getImpl(alias));
  }

  @Override
  public final Collection<URI> getUrls() {
    return Histograms.STORAGE_OPERATION_DURATION
        .labels(implClass, "getUrls")
        .time(() -> getUrlsImpl());
  }

  @Override
  public final Collection<Pair<String, Long>> getAliases() {
    return Histograms.STORAGE_OPERATION_DURATION
        .labels(implClass, "getAliases")
        .time(() -> getAliasesImpl());
  }

  @Override
  public final void set(URI url, String alias) {
    Histograms.STORAGE_OPERATION_DURATION.labels(implClass, "set").time(() -> setImpl(url, alias));
  }

  @Override
  public final boolean delete(String alias) {
    return Histograms.STORAGE_OPERATION_DURATION
        .labels(implClass, "deleteByAlias")
        .time(() -> deleteImpl(alias));
  }

  @Override
  public final boolean delete(URI url) {
    return Histograms.STORAGE_OPERATION_DURATION
        .labels(implClass, "deleteByUrl")
        .time(() -> deleteImpl(url));
  }

  @Override
  public final long incrementUsages(String alias) {
    return Histograms.STORAGE_OPERATION_DURATION
        .labels(implClass, "incrementUsages")
        .time(() -> incrementUsagesImpl(alias));
  }
}
