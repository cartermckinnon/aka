package mck.service.aka.auth;

import java.security.Principal;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

/** A user of the authenticated HTTP API. */
@Getter
@RequiredArgsConstructor
@ToString
@EqualsAndHashCode
public class ApiUser implements Principal {
  private final String name;
}
