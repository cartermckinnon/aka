package mck.service.urlalias.resources;

import io.dropwizard.auth.Auth;
import java.net.URI;
import java.util.Collection;
import java.util.Optional;
import javax.annotation.security.PermitAll;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
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
    storage.set(request.getUrl(), request.getAlias());
  }

  @PermitAll
  @GET
  @Path("/urls")
  @Produces({"application/json"})
  public Collection<URI> getUrls(@Auth ApiUser user) {
    LOG.info("getUrls: user={}", user);
    return storage.getUrls();
  }

  @PermitAll
  @GET
  @Path("/url/{url}")
  @Produces({"application/json"})
  public Optional<String> getUrl(@Auth ApiUser user, @PathParam("url") URI url) {
    LOG.info("getUrl: user={} url={}", user, url);
    return storage.get(url);
  }

  @PermitAll
  @GET
  @Path("/aliases")
  @Produces({"application/json"})
  public Collection<String> getAliases(@Auth ApiUser user) {
    LOG.info("getAliases: user={}", user);
    return storage.getAliases();
  }

  @PermitAll
  @GET
  @Path("/alias/{alias}")
  @Produces({"application/json"})
  public Optional<URI> getAlias(@Auth ApiUser user, @PathParam("alias") String alias) {
    LOG.info("getAlias: user={} alias={}", user, alias);
    return storage.get(alias);
  }

  @PermitAll
  @DELETE
  @Path("/url/{url}")
  @Produces({"application/json"})
  public boolean deleteUrl(@Auth ApiUser user, @PathParam("url") URI url) {
    LOG.info("deleteUrl: user={} url={}", user, url);
    return storage.delete(url);
  }

  @PermitAll
  @DELETE
  @Path("/alias/{alias}")
  @Produces({"application/json"})
  public boolean deleteAlias(@Auth ApiUser user, @PathParam("alias") String alias) {
    LOG.info("deleteAlias: user={} alias={}", user, alias);
    return storage.delete(alias);
  }
}
