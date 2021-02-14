package mck.service.aka.util;

import lombok.EqualsAndHashCode;
import lombok.ToString;

@ToString
@EqualsAndHashCode
public class LongCounter {
  private long n = 0;

  public long increment() {
    return increment(1);
  }

  public long increment(long n) {
    return (this.n += n);
  }

  public long value() {
    return n;
  }
}
