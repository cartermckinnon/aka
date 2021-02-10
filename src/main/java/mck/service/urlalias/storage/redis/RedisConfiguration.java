package mck.service.urlalias.storage.redis;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/** Parameters for a connection to redis. */
@Getter
@Setter
@ToString
@EqualsAndHashCode
public class RedisConfiguration {
  public String host = "localhost";
  public int port = 6379;
  public boolean ssl = false;
}
