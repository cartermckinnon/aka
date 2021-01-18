package mck.service.urlalias.resources;

import io.dropwizard.auth.Auth;
import java.net.URI;
import java.util.Map;
import javax.annotation.security.PermitAll;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import lombok.extern.slf4j.Slf4j;
import mck.service.urlalias.api.SetUrlAliasRequest;
import mck.service.urlalias.auth.ApiUser;
import mck.service.urlalias.storage.UrlAliasStorage;

/**
 * Authenticated, private HTTP API for managing URL aliases.
 *
 * @author Carter McKinnon {@literal <cartermckinnon@gmail.com>}
 */
@Path("/api")
@Slf4j
public class UrlAliasServiceApiResource {
  public UrlAliasStorage storage;

  public UrlAliasServiceApiResource(UrlAliasStorage storage) {
    this.storage = storage;
  }

  @PermitAll
  @POST
  @Path("/set")
  public void set(@Auth ApiUser user, @NotNull @Valid SetUrlAliasRequest request) {
    LOG.info("set: user={} request={}", user, request);
    storage.setAlias(request.getUrl(), request.getAlias());
  }

  @GET
  @Path("/urls")
  public Map<String, URI> getUrls(@Auth ApiUser user) {
    LOG.info("getUrls: user={}", user);
    return storage.getUrls();
  }
}
