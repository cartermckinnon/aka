package mck.service.urlalias;

import io.dropwizard.Configuration;
import java.util.HashMap;
import java.util.Map;
import lombok.Getter;
import lombok.Setter;

/** @author Carter McKinnon {@literal <cartermckinnon@gmail.com>} */
@Getter
@Setter
public class UrlAliasServiceConfiguration extends Configuration {
  private final Map<String, String> apiCredentials = new HashMap<>();
}
