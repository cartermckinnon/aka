package mck.service.urlalias.storage;

import java.net.URI;
import java.util.Collection;
import java.util.Optional;
import mck.service.urlalias.metrics.Histograms;

/**
 * Implements instrumentation for all methods of {@code UrlAliasStorage}. Child classes should not
 * override these implementations, and should instead implement only the (equivalent) abstract
 * methods defined in this class (i.e. the ones suffixed with {@code -Impl}).
 */
public abstract class InstrumentedUrlAliasStorage implements UrlAliasStorage {

  /** @see UrlAliasStorage#get(URI) */
  protected abstract Optional<String> getImpl(URI url);

  /** @see UrlAliasStorage#get(String) */
  protected abstract Optional<URI> getImpl(String alias);

  /** @see UrlAliasStorage#set(URI, String) */
  protected abstract void setImpl(URI url, String alias);

  /** @see UrlAliasStorage#getUrls() */
  protected abstract Collection<URI> getUrlsImpl();

  /** @see UrlAliasStorage#getAliases() */
  protected abstract Collection<String> getAliasesImpl();

  /** @see UrlAliasStorage#delete(String) */
  protected abstract boolean deleteImpl(String alias);

  /** @see UrlAliasStorage#delete(URI) */
  protected abstract boolean deleteImpl(URI url);

  private final String implClass = this.getClass().getName();

  @Override
  public final Optional<String> get(URI url) {
    return Histograms.STORAGE_OPERATIONS
        .labels(implClass, "getAliasByUrl")
        .time(() -> getImpl(url));
  }

  @Override
  public final Optional<URI> get(String alias) {
    return Histograms.STORAGE_OPERATIONS
        .labels(implClass, "getUrlByAlias")
        .time(() -> getImpl(alias));
  }

  @Override
  public final Collection<URI> getUrls() {
    return Histograms.STORAGE_OPERATIONS.labels(implClass, "getUrls").time(() -> getUrlsImpl());
  }

  @Override
  public final Collection<String> getAliases() {
    return Histograms.STORAGE_OPERATIONS
        .labels(implClass, "getAliases")
        .time(() -> getAliasesImpl());
  }

  @Override
  public final void set(URI url, String alias) {
    Histograms.STORAGE_OPERATIONS.labels(implClass, "set").time(() -> setImpl(url, alias));
  }

  @Override
  public final boolean delete(String alias) {
    return Histograms.STORAGE_OPERATIONS
        .labels(implClass, "deleteByAlias")
        .time(() -> deleteImpl(alias));
  }

  @Override
  public final boolean delete(URI url) {
    return Histograms.STORAGE_OPERATIONS
        .labels(implClass, "deleteByUrl")
        .time(() -> deleteImpl(url));
  }
}
