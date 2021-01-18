package mck.service.urlalias.auth;

import io.dropwizard.auth.Authorizer;
import javax.ws.rs.container.ContainerRequestContext;

/** @author Carter McKinnon {@literal <cartermckinnon@gmail.com>} */
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
