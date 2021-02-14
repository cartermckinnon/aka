package mck.service.aka.auth;

import io.dropwizard.auth.AuthenticationException;
import io.dropwizard.auth.Authenticator;
import io.dropwizard.auth.basic.BasicCredentials;
import java.util.Map;
import java.util.Optional;

/** Naive authenticator for users of the authenticated HTTP API. */
public class SimpleAuthenticator implements Authenticator<BasicCredentials, ApiUser> {
  private final Map<String, String> apiCredentials;

  public SimpleAuthenticator(Map<String, String> apiCredentials) {
    this.apiCredentials = apiCredentials;
  }

  @Override
  public Optional<ApiUser> authenticate(BasicCredentials credentials)
      throws AuthenticationException {
    String password = apiCredentials.get(credentials.getUsername());
    if (password == null) {
      // unknown username
      return Optional.empty();
    }
    if (!password.equals(credentials.getPassword())) {
      // incorrect password
      return Optional.empty();
    }
    return Optional.of(new ApiUser(credentials.getUsername()));
  }
}
