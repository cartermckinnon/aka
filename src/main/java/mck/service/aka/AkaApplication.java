package mck.service.aka;

import com.fasterxml.jackson.databind.SerializationFeature;
import io.dropwizard.Application;
import io.dropwizard.auth.AuthDynamicFeature;
import io.dropwizard.auth.AuthValueFactoryProvider;
import io.dropwizard.auth.basic.BasicCredentialAuthFilter;
import io.dropwizard.setup.Environment;
import io.prometheus.client.exporter.MetricsServlet;
import mck.service.aka.auth.AllowAllAuthorizer;
import mck.service.aka.auth.ApiUser;
import mck.service.aka.auth.SimpleAuthenticator;
import mck.service.aka.resources.ApiResource;
import mck.service.aka.resources.RedirectResource;
import mck.service.aka.storage.UrlAliasStorage;
import org.glassfish.jersey.server.filter.RolesAllowedDynamicFeature;

public class AkaApplication extends Application<AkaConfiguration> {

  public static void main(String[] args) throws Exception {
    new AkaApplication().run(args);
  }

  @Override
  public void run(AkaConfiguration c, Environment e) throws Exception {
    // necessary for the storage factory to be unspecified/default in the config
    e.getObjectMapper().disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);

    // storage
    UrlAliasStorage storage = c.getStorage().deserialize(e.getObjectMapper()).build(e);

    // http resources
    e.jersey().register(new RedirectResource(storage));
    e.jersey().register(new ApiResource(storage));

    // auth
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

    // prometheus metrics
    e.getAdminContext().addServlet(MetricsServlet.class, "/prometheus-metrics");
  }
}
