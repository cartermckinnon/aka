package mck.service.urlalias.resources;

import javax.validation.constraints.NotBlank;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import mck.service.urlalias.metrics.Counters;
import mck.service.urlalias.storage.UrlAliasStorage;

/** Unauthenticated, public HTTP API for redirecting an alias to its target URl. */
@Path("/")
public class UrlAliasServiceRedirectResource {
  protected static final MediaType NOT_FOUND_RESPONSE_TYPE = MediaType.TEXT_PLAIN_TYPE;
  protected static final String NOT_FOUND_RESPONSE_BODY = "Huh?";

  private final UrlAliasStorage storage;

  public UrlAliasServiceRedirectResource(UrlAliasStorage storage) {
    this.storage = storage;
  }

  private static Response notFound() {
    return Response.status(404)
        .type(NOT_FOUND_RESPONSE_TYPE)
        .entity(NOT_FOUND_RESPONSE_BODY)
        .build();
  }

  /**
   * Override the default Dropwizard 404 response for the root path.
   *
   * @return "Huh?"
   */
  @GET
  public Response emptyPath() {
    return notFound();
  }

  @GET
  @Path("/{alias}")
  public Response redirect(@PathParam("alias") @NotBlank String alias) {
    var url = storage.get(alias);
    Response res;
    String statusLabel;
    if (url.isPresent()) {
      statusLabel = "200";
      res = Response.temporaryRedirect(url.get().getLeft()).build();
      storage.incrementUsages(alias);
    } else {
      statusLabel = "404";
      res = notFound();
    }
    Counters.HTTP_REQUESTS.labels("GET", "/{alias}", statusLabel).inc();
    return res;
  }
}
