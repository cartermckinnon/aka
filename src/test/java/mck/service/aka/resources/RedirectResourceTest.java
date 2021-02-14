package mck.service.aka.resources;

import static org.assertj.core.api.Assertions.assertThat;

import java.net.URI;
import javax.ws.rs.core.Response;
import mck.service.aka.storage.UrlAliasStorage;
import mck.service.aka.storage.memory.MemoryUrlAliasStorage;
import org.junit.jupiter.api.Test;

public class RedirectResourceTest {
  @Test
  public void redirect() {
    UrlAliasStorage storage = new MemoryUrlAliasStorage();
    URI url = URI.create("http://mckinnon.xyz");
    String alias = "mck";
    storage.set(url, alias);

    RedirectResource resource = new RedirectResource(storage);

    // known alias
    Response res = resource.redirect(alias);
    assertThat(res.getStatus()).isEqualTo(307); // temporary redirect
    assertThat(res.getLocation()).isEqualTo(url);

    // unknown alias
    res = resource.redirect("unknownAlias");
    assertThat(res.getStatus()).isEqualTo(404);
    assertThat(res.getMediaType()).isEqualTo(RedirectResource.NOT_FOUND_RESPONSE_TYPE);
    assertThat(res.getEntity()).isEqualTo(RedirectResource.NOT_FOUND_RESPONSE_BODY);
  }
}
