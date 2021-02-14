package mck.service.aka.util;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

/** An immutable 2-tuple. */
@Getter
@ToString
@EqualsAndHashCode
@RequiredArgsConstructor
public class Pair<L, R> {
  private final L left;
  private final R right;
}
