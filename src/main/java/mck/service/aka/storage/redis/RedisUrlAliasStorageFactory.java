package mck.service.aka.storage.redis;

import io.dropwizard.setup.Environment;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import mck.service.aka.storage.UrlAliasStorage;
import mck.service.aka.storage.UrlAliasStorageFactory;
import redis.clients.jedis.JedisPool;

@Getter
@Setter
@ToString
@EqualsAndHashCode
public class RedisUrlAliasStorageFactory implements UrlAliasStorageFactory {

  public RedisConfiguration redis = new RedisConfiguration();

  @Override
  public UrlAliasStorage build(Environment e) {
    JedisPool pool = new JedisPool(redis.getHost(), redis.getPort(), redis.isSsl());
    e.lifecycle().manage(new JedisPoolManager(pool));
    e.healthChecks().register("redis", new JedisPoolHealthCheck(pool));
    return new RedisUrlAliasStorage(pool);
  }
}
