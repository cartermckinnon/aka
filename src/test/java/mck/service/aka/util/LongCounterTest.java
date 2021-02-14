package mck.service.aka.util;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

public class LongCounterTest {
  @Test
  public void test() {
    LongCounter counter = new LongCounter();

    assertThat(counter.increment()).isEqualTo(1);
    assertThat(counter.increment()).isEqualTo(2);
    assertThat(counter.increment(1)).isEqualTo(3);
    assertThat(counter.increment(7)).isEqualTo(10);
    assertThat(counter.value()).isEqualTo(10);
  }
}
