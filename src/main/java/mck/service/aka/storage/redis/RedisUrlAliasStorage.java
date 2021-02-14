package mck.service.aka.storage.redis;

import static java.nio.charset.StandardCharsets.UTF_8;

import java.net.URI;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import mck.service.aka.storage.InstrumentedUrlAliasStorage;
import mck.service.aka.util.Pair;
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
      alias = jedis.hget(RedisKeys.url(url), RedisKeys.ALIAS_HASH_FIELD);
    }
    return Optional.ofNullable(alias).map(bytes -> new String(bytes, UTF_8));
  }

  @Override
  public Optional<Pair<URI, Long>> getImpl(String alias) {
    Map<byte[], byte[]> hashFields;
    try (Jedis jedis = pool.getResource()) {
      hashFields = jedis.hgetAll(RedisKeys.alias(alias));
    }
    if (hashFields.isEmpty()) {
      return Optional.empty();
    }
    URI url = URI.create(new String(hashFields.get(RedisKeys.URL_HASH_FIELD), UTF_8));
    long usages = 0;
    byte[] usagesField = hashFields.get(RedisKeys.USAGES_HASH_FIELD);
    if (usagesField != null) {
      usages = Long.parseLong(new String(usagesField, UTF_8));
    }
    return Optional.of(new Pair<>(url, usages));
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
    byte[] urlBytes = url.toString().getBytes(UTF_8);
    byte[] aliasKey = RedisKeys.alias(alias);
    byte[] aliasBytes = alias.getBytes(UTF_8);
    try (Jedis jedis = pool.getResource()) {
      byte[] previousAlias = jedis.hget(urlKey, RedisKeys.ALIAS_HASH_FIELD);
      if (!Arrays.equals(aliasBytes, previousAlias)) {
        // url was not mapped to an alias, or was mapped to a different alias
        jedis.hset(urlKey, RedisKeys.ALIAS_HASH_FIELD, aliasBytes);
        if (previousAlias != null) {
          jedis.del(RedisKeys.alias(previousAlias));
        }
        byte[] previousUrl = jedis.hget(aliasKey, RedisKeys.URL_HASH_FIELD);
        jedis.hset(aliasKey, RedisKeys.URL_HASH_FIELD, urlBytes);
        if (previousUrl != null) {
          jedis.del(RedisKeys.url(URI.create(new String(previousUrl, UTF_8))));
        }
      }
    }
  }

  @Override
  public boolean deleteImpl(URI url) {
    byte[] urlKey = RedisKeys.url(url);
    try (Jedis jedis = pool.getResource()) {
      byte[] alias = jedis.hget(urlKey, RedisKeys.ALIAS_HASH_FIELD);
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
      byte[] url = jedis.hget(aliasKey, RedisKeys.URL_HASH_FIELD);
      if (url == null) {
        return false;
      }
      jedis.del(aliasKey);
      jedis.del(RedisKeys.url(URI.create(new String(url, UTF_8))));
    }
    return true;
  }

  @Override
  protected long incrementUsagesImpl(String alias) {
    byte[] aliasKey = RedisKeys.alias(alias);
    try (Jedis jedis = pool.getResource()) {
      byte[] url = jedis.hget(aliasKey, RedisKeys.URL_HASH_FIELD);
      if (url != null) {
        jedis.hincrBy(aliasKey, RedisKeys.USAGES_HASH_FIELD, 1);
        byte[] urlKey = RedisKeys.url(URI.create(new String(url, UTF_8)));
        return jedis.hincrBy(urlKey, RedisKeys.USAGES_HASH_FIELD, 1);
      }
    }
    return -1;
  }
}
