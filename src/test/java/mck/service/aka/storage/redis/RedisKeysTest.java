package mck.service.aka.storage.redis;

import static org.assertj.core.api.Assertions.assertThat;

import java.nio.charset.StandardCharsets;
import org.junit.jupiter.api.Test;

public class RedisKeysTest {
  @Test
  public void reverseAppendHostParts_3_parts() {
    String s = "one.two.three";
    byte[] reversed = "three.two.one".getBytes(StandardCharsets.UTF_8);
    byte[] buf = new byte[s.length()];
    RedisKeys.reverseAppendSplits(buf, 0, s, '.');
    assertThat(buf).isEqualTo(reversed);
  }

  @Test
  public void reverseAppendHostParts_2_parts() {
    String s = "one.two";
    byte[] reversed = "two.one".getBytes(StandardCharsets.UTF_8);
    byte[] buf = new byte[s.length()];
    RedisKeys.reverseAppendSplits(buf, 0, s, '.');
    assertThat(buf).isEqualTo(reversed);
  }

  @Test
  public void reverseAppendHostParts_1_parts() {
    String s = "one";
    byte[] bytes = s.getBytes(StandardCharsets.UTF_8);
    byte[] buf = new byte[s.length()];
    RedisKeys.reverseAppendSplits(buf, 0, s, '.');
    assertThat(buf).isEqualTo(bytes);
  }
}
