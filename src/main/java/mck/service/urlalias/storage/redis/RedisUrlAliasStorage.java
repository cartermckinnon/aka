package mck.service.urlalias.storage.redis;

import static java.nio.charset.StandardCharsets.UTF_8;

import java.net.URI;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import mck.service.urlalias.storage.InstrumentedUrlAliasStorage;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.ScanParams;
import redis.clients.jedis.ScanResult;

public class RedisUrlAliasStorage extends InstrumentedUrlAliasStorage {

  private final JedisPool pool;

  public RedisUrlAliasStorage(JedisPool pool) {
    this.pool = pool;
  }

  @Override
  public Optional<String> getImpl(URI url) {
    byte[] alias;
    try (Jedis jedis = pool.getResource()) {
      alias = jedis.get(RedisKeys.url(url));
    }
    return Optional.ofNullable(alias).map(bytes -> new String(bytes, UTF_8));
  }

  @Override
  public Optional<URI> getImpl(String alias) {
    byte[] url;
    try (Jedis jedis = pool.getResource()) {
      url = jedis.get(RedisKeys.alias(alias));
    }
    return Optional.ofNullable(url).map(bytes -> new String(bytes, UTF_8)).map(URI::create);
  }

  @Override
  public Collection<URI> getUrlsImpl() {
    List<URI> urls = new ArrayList<>();
    ScanParams params = new ScanParams();
    params.match(RedisKeys.URL_PATTERN);
    String cursor = ScanParams.SCAN_POINTER_START;
    try (Jedis jedis = pool.getResource()) {
      do {
        ScanResult<String> res = jedis.scan(cursor, params);
        cursor = res.getCursor();
        res.getResult().stream().map(RedisKeys::urlFromKey).forEach(urls::add);
      } while (!cursor.equals(ScanParams.SCAN_POINTER_START));
    }
    return urls;
  }

  @Override
  public Collection<String> getAliasesImpl() {
    List<String> aliases = new ArrayList<>();
    ScanParams params = new ScanParams();
    params.match(RedisKeys.ALIAS_PATTERN);
    String cursor = ScanParams.SCAN_POINTER_START;
    try (Jedis jedis = pool.getResource()) {
      do {
        ScanResult<String> res = jedis.scan(cursor, params);
        cursor = res.getCursor();
        res.getResult().stream().map(alias -> alias.substring(6)).forEach(aliases::add);
      } while (!cursor.equals(ScanParams.SCAN_POINTER_START));
    }
    return aliases;
  }

  @Override
  public void setImpl(URI url, String alias) {
    byte[] urlKey = RedisKeys.url(url);
    byte[] aliasKey = RedisKeys.alias(alias);
    String status;
    try (Jedis jedis = pool.getResource()) {
      byte[] previousAlias = jedis.get(urlKey);
      status = jedis.set(urlKey, alias.getBytes(UTF_8));
      if (!"OK".equals(status)) {
        throw new IllegalStateException(
            "failed to set URL! status=" + status + " url=" + url + " alias=" + alias);
      }
      if (previousAlias != null) {
        jedis.del(RedisKeys.alias(previousAlias));
      }
      byte[] previousUrl = jedis.get(aliasKey);
      status = jedis.set(aliasKey, url.toString().getBytes(UTF_8));
      if (!"OK".equals(status)) {
        throw new IllegalStateException(
            "failed to set alias! status=" + status + " url=" + url + " to alias=" + alias);
      }
      if (previousUrl != null) {
        jedis.del(RedisKeys.url(URI.create(new String(previousUrl, UTF_8))));
      }
    }
  }

  @Override
  public boolean deleteImpl(URI url) {
    byte[] urlKey = RedisKeys.url(url);
    try (Jedis jedis = pool.getResource()) {
      byte[] alias = jedis.get(urlKey);
      if (alias == null) {
        return false;
      }
      jedis.del(urlKey);
      jedis.del(RedisKeys.alias(new String(alias, UTF_8)));
    }
    return true;
  }

  @Override
  public boolean deleteImpl(String alias) {
    byte[] aliasKey = RedisKeys.alias(alias);
    try (Jedis jedis = pool.getResource()) {
      byte[] url = jedis.get(aliasKey);
      if (url == null) {
        return false;
      }
      jedis.del(aliasKey);
      jedis.del(RedisKeys.url(URI.create(new String(url, UTF_8))));
    }
    return true;
  }
}
