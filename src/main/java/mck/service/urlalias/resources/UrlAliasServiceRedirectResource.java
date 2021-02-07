package mck.service.urlalias.resources;

import javax.validation.constraints.NotBlank;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Response;
import mck.service.urlalias.storage.UrlAliasStorage;

/**
 * Unauthenticated, public HTTP API for redirecting an alias to its target URl.
 *
 * @author Carter McKinnon {@literal <cartermckinnon@gmail.com>}
 */
@Path("/")
public class UrlAliasServiceRedirectResource {
  public UrlAliasStorage storage;

  public UrlAliasServiceRedirectResource(UrlAliasStorage storage) {
    this.storage = storage;
  }

  @GET
  @Path("/{alias}")
  public Response redirect(@PathParam("alias") @NotBlank String alias) {
    var url = storage.get(alias);
    if (url.isPresent()) {
      return Response.temporaryRedirect(url.get()).build();
    }
    return Response.status(404).type("text/plain").entity("Huh?").build();
  }
}
