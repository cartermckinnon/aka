package mck.service.urlalias;

import io.dropwizard.Application;
import io.dropwizard.auth.AuthDynamicFeature;
import io.dropwizard.auth.AuthValueFactoryProvider;
import io.dropwizard.auth.basic.BasicCredentialAuthFilter;
import io.dropwizard.setup.Environment;
import mck.service.urlalias.auth.AllowAllAuthorizer;
import mck.service.urlalias.auth.ApiUser;
import mck.service.urlalias.auth.SimpleAuthenticator;
import mck.service.urlalias.resources.UrlAliasServiceApiResource;
import mck.service.urlalias.resources.UrlAliasServiceRedirectResource;
import mck.service.urlalias.storage.MemoryUrlAliasStorage;
import org.glassfish.jersey.server.filter.RolesAllowedDynamicFeature;

/** @author Carter McKinnon {@literal <cartermckinnon@gmail.com>} */
public class UrlAliasServiceApplication extends Application<UrlAliasServiceConfiguration> {

  public static void main(String[] args) throws Exception {
    new UrlAliasServiceApplication().run(args);
  }

  @Override
  public void run(UrlAliasServiceConfiguration c, Environment e) throws Exception {
    var storage = new MemoryUrlAliasStorage();
    e.jersey().register(new UrlAliasServiceRedirectResource(storage));
    e.jersey().register(new UrlAliasServiceApiResource(storage));
    e.jersey()
        .register(
            new AuthDynamicFeature(
                new BasicCredentialAuthFilter.Builder<ApiUser>()
                    .setAuthenticator(new SimpleAuthenticator(c.getApiCredentials()))
                    .setAuthorizer(new AllowAllAuthorizer())
                    .setRealm("API")
                    .buildAuthFilter()));
    e.jersey().register(RolesAllowedDynamicFeature.class);
    e.jersey().register(new AuthValueFactoryProvider.Binder<>(ApiUser.class));
  }
}
