package mck.service.urlalias.storage.redis;

import mck.service.urlalias.storage.UrlAliasStorage;
import mck.service.urlalias.storage.UrlAliasStorageTest;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;
import redis.clients.jedis.JedisPool;

@Testcontainers
public class RedisUrlAliasStorageTest extends UrlAliasStorageTest {
  @Container
  public GenericContainer<?> redis =
      new GenericContainer<>(DockerImageName.parse("redis:alpine")).withExposedPorts(6379);

  private JedisPool pool = null;

  @BeforeEach
  public void before() {
    pool = new JedisPool(redis.getHost(), redis.getMappedPort(6379));
  }

  @AfterEach
  public void after() {
    pool.close();
  }

  @Override
  public UrlAliasStorage createImpl() {
    return new RedisUrlAliasStorage(pool);
  }
}
