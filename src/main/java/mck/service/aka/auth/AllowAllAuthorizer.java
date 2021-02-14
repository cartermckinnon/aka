package mck.service.aka.auth;

import io.dropwizard.auth.Authorizer;
import javax.ws.rs.container.ContainerRequestContext;

/** Allows all users to access all roles/domains. */
public class AllowAllAuthorizer implements Authorizer<ApiUser> {

  @Override
  public boolean authorize(ApiUser principal, String role, ContainerRequestContext requestContext) {
    return true;
  }

  @Override
  public boolean authorize(ApiUser p, String string) {
    return true;
  }
}
