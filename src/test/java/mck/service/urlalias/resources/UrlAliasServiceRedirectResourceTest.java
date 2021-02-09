package mck.service.urlalias.resources;

import static org.assertj.core.api.Assertions.assertThat;

import java.net.URI;
import javax.ws.rs.core.Response;
import mck.service.urlalias.storage.UrlAliasStorage;
import mck.service.urlalias.storage.memory.MemoryUrlAliasStorage;
import org.junit.jupiter.api.Test;

public class UrlAliasServiceRedirectResourceTest {
  @Test
  public void redirect() {
    UrlAliasStorage storage = new MemoryUrlAliasStorage();
    URI url = URI.create("http://mckinnon.xyz");
    String alias = "mck";
    storage.set(url, alias);

    UrlAliasServiceRedirectResource resource = new UrlAliasServiceRedirectResource(storage);

    // known alias
    Response res = resource.redirect(alias);
    assertThat(res.getStatus()).isEqualTo(307); // temporary redirect
    assertThat(res.getLocation()).isEqualTo(url);

    // unknown alias
    res = resource.redirect("unknownAlias");
    assertThat(res.getStatus()).isEqualTo(404);
    assertThat(res.getMediaType())
        .isEqualTo(UrlAliasServiceRedirectResource.UNKNOWN_ALIAS_RESPONSE_TYPE);
    assertThat(res.getEntity()).isEqualTo(UrlAliasServiceRedirectResource.UNKNOWN_ALIAS_RESPONSE);
  }
}
