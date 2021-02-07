package mck.service.urlalias;

import com.fasterxml.jackson.databind.SerializationFeature;
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
import mck.service.urlalias.storage.UrlAliasStorage;
import mck.service.urlalias.storage.UrlAliasStorageFactoryDeserializer;
import org.glassfish.jersey.server.filter.RolesAllowedDynamicFeature;

/** @author Carter McKinnon {@literal <cartermckinnon@gmail.com>} */
public class UrlAliasServiceApplication extends Application<UrlAliasServiceConfiguration> {

  public static void main(String[] args) throws Exception {
    new UrlAliasServiceApplication().run(args);
  }

  @Override
  public void run(UrlAliasServiceConfiguration c, Environment e) throws Exception {
    // necessary for the storage factory to be unspecified (for memory impl)
    e.getObjectMapper().disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);
    UrlAliasStorageFactoryDeserializer storageFactoryDeserializer = c.getStorage();
    UrlAliasStorage storage = storageFactoryDeserializer.deserialize(e.getObjectMapper()).build(e);
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
