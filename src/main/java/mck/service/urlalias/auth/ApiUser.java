package mck.service.urlalias.auth;

import java.security.Principal;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

/** @author Carter McKinnon {@literal <cartermckinnon@gmail.com>} */
@Getter
@RequiredArgsConstructor
@ToString
@EqualsAndHashCode
public class ApiUser implements Principal {
  private final String name;
}
