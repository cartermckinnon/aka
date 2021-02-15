package mck.service.aka.storage.redis;

import static java.nio.charset.StandardCharsets.UTF_8;
import static java.util.stream.Collectors.toUnmodifiableList;

import java.net.URI;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
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

  private final byte[] ZERO = "0".getBytes(UTF_8);

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
    byte[] usagesField = hashFields.get(RedisKeys.USAGES_HASH_FIELD_BYTES);
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
  public Collection<Pair<String, Long>> getAliasesImpl() {
    // redis SCAN may return a key more than once, so we need
    // to be able to track the keys we've already seen
    Map<String, Long> keys = new HashMap<>();
    ScanParams params = new ScanParams();
    params.match(RedisKeys.ALIAS_PATTERN);
    String cursor = ScanParams.SCAN_POINTER_START;
    try (Jedis jedis = pool.getResource()) {
      do {
        ScanResult<String> res = jedis.scan(cursor, params);
        for (String key : res.getResult()) {
          if (!keys.containsKey(key)) {
            String usages = jedis.hget(key, RedisKeys.USAGES_HASH_FIELD);
            if (usages != null) {
              keys.put(key, Long.parseLong(usages));
            } else {
              System.out.println("\n\nkey didn't have a usage during scan: " + key + "\n\n");
              keys.remove(key); // key was deleted while we were scanning
            }
          }
        }
        cursor = res.getCursor();
      } while (!cursor.equals(ScanParams.SCAN_POINTER_START));
    }
    return keys.entrySet().stream()
        .map(
            entry ->
                new Pair<>(
                    entry.getKey().substring(RedisKeys.ALIAS_PREFIX.length()), entry.getValue()))
        .collect(toUnmodifiableList());
  }

  @Override
  public void setImpl(URI url, String alias) {
    byte[] urlKey = RedisKeys.url(url);
    byte[] urlBytes = url.toString().getBytes(UTF_8);
    byte[] aliasKey = RedisKeys.alias(alias);
    byte[] aliasBytes = alias.getBytes(UTF_8);
    try (Jedis jedis = pool.getResource()) {
      byte[] previousAlias = jedis.hget(urlKey, RedisKeys.ALIAS_HASH_FIELD);
      // we only have work to do if the URL isn't currently mapped to this alias
      if (!Arrays.equals(aliasBytes, previousAlias)) {
        // if the URL was already mapped to a different alias, delete that alias
        if (previousAlias != null) {
          jedis.del(RedisKeys.alias(previousAlias));
        }

        // if the alias was already mapped to a different URL, delete that URL
        byte[] previousUrl = jedis.hget(aliasKey, RedisKeys.URL_HASH_FIELD);
        if (previousUrl != null) {
          jedis.del(RedisKeys.url(URI.create(new String(previousUrl, UTF_8))));
        }

        // map URL to alias
        jedis.hset(urlKey, RedisKeys.ALIAS_HASH_FIELD, aliasBytes);

        // HINCRBY will initialize the 'usages' field with zero on the first increment,
        // but we need to initialize it up front so that #getAliasesImpl() can return zero
        // for unused aliases. Otherwise, an HGET for the 'usages' field will return null,
        // and this overlaps with the behavior of SCAN, which may return a key that is deleted
        // while we are scanning. We have to be able to distinguish the two scenarios.
        Map<byte[], byte[]> hashFields = new HashMap<>();
        hashFields.put(RedisKeys.URL_HASH_FIELD, urlBytes);
        hashFields.put(RedisKeys.USAGES_HASH_FIELD_BYTES, ZERO);

        // map alias to URL
        jedis.hset(aliasKey, hashFields);
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
        jedis.hincrBy(aliasKey, RedisKeys.USAGES_HASH_FIELD_BYTES, 1);
        byte[] urlKey = RedisKeys.url(URI.create(new String(url, UTF_8)));
        return jedis.hincrBy(urlKey, RedisKeys.USAGES_HASH_FIELD_BYTES, 1);
      }
    }
    return -1;
  }
}
