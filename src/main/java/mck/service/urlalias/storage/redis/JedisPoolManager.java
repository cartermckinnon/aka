package mck.service.urlalias.storage.redis;


import io.dropwizard.lifecycle.Managed;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

public class JedisPoolManager implements Managed {

  private final JedisPool pool;

  public JedisPoolManager(JedisPool pool) {
    this.pool = pool;
  }

  @Override
  public void start() throws Exception {
    // open a connection to the server
    try (Jedis jedis = pool.getResource()) {
      jedis.ping(); // smoke test
    }
  }

  @Override
  public void stop() throws Exception {
    pool.close();
  }
}
