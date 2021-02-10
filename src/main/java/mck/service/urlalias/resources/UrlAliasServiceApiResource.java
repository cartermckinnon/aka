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
import mck.service.urlalias.auth.ApiUser;
import mck.service.urlalias.metrics.Counters;
import mck.service.urlalias.resources.api.SetUrlAliasRequest;
import mck.service.urlalias.storage.UrlAliasStorage;

/** Authenticated, private HTTP API for managing URL aliases. */
@Path("/api")
public class UrlAliasServiceApiResource {
  private final UrlAliasStorage storage;

  public UrlAliasServiceApiResource(UrlAliasStorage storage) {
    this.storage = storage;
  }

  @PermitAll
  @POST
  @Path("/set")
  public void set(@Auth ApiUser user, @NotNull @Valid SetUrlAliasRequest request) {
    Counters.HTTP_REQUESTS_WITH_AUTH.labels("POST", "/set", "200", user.getName()).inc();
    storage.set(request.getUrl(), request.getAlias());
  }

  @PermitAll
  @GET
  @Path("/urls")
  @Produces({"application/json"})
  public Collection<URI> getUrls(@Auth ApiUser user) {
    Counters.HTTP_REQUESTS_WITH_AUTH.labels("GET", "/urls", "200", user.getName()).inc();
    return storage.getUrls();
  }

  @PermitAll
  @GET
  @Path("/url/{url}")
  @Produces({"application/json"})
  public Optional<String> getUrl(@Auth ApiUser user, @PathParam("url") URI url) {
    Counters.HTTP_REQUESTS_WITH_AUTH.labels("GET", "/url/" + url, "200", user.getName()).inc();
    return storage.get(url);
  }

  @PermitAll
  @GET
  @Path("/aliases")
  @Produces({"application/json"})
  public Collection<String> getAliases(@Auth ApiUser user) {
    Counters.HTTP_REQUESTS_WITH_AUTH.labels("GET", "/aliases", "200", user.getName()).inc();
    return storage.getAliases();
  }

  @PermitAll
  @GET
  @Path("/alias/{alias}")
  @Produces({"application/json"})
  public Optional<URI> getAlias(@Auth ApiUser user, @PathParam("alias") String alias) {
    Counters.HTTP_REQUESTS_WITH_AUTH.labels("GET", "/alias/" + alias, "200", user.getName()).inc();
    return storage.get(alias);
  }

  @PermitAll
  @DELETE
  @Path("/url/{url}")
  @Produces({"application/json"})
  public boolean deleteUrl(@Auth ApiUser user, @PathParam("url") URI url) {
    Counters.HTTP_REQUESTS_WITH_AUTH.labels("DELETE", "/url/" + url, "200", user.getName()).inc();
    return storage.delete(url);
  }

  @PermitAll
  @DELETE
  @Path("/alias/{alias}")
  @Produces({"application/json"})
  public boolean deleteAlias(@Auth ApiUser user, @PathParam("alias") String alias) {
    Counters.HTTP_REQUESTS_WITH_AUTH
        .labels("DELETE", "/alias/" + alias, "200", user.getName())
        .inc();
    return storage.delete(alias);
  }
}
