package mck.service.urlalias.storage.redis;

import com.codahale.metrics.health.HealthCheck;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

/** Uses the redis {@code PING} command to verify connectivity to the server. */
public class JedisPoolHealthCheck extends HealthCheck {

  private final JedisPool pool;

  public JedisPoolHealthCheck(JedisPool pool) {
    this.pool = pool;
  }

  @Override
  protected Result check() throws Exception {
    String pong;
    try (Jedis jedis = pool.getResource()) {
      pong = jedis.ping();
    }
    return "PONG".equals(pong) ? Result.healthy() : Result.unhealthy(pong);
  }
}
