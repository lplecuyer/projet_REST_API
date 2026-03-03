package ca.ulaval.glo2003;

import ca.ulaval.glo2003.resources.OfferResource;
import ca.ulaval.glo2003.resources.ProductResource;
import ca.ulaval.glo2003.resources.SellerResource;
import org.glassfish.jersey.grizzly2.httpserver.GrizzlyHttpServerFactory;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.server.validation.ValidationFeature;

import java.net.URI;

public class Main {
    public static final String BASE_URI = "http://0.0.0.0:8080/";

    public static void startServer() {
        final ResourceConfig rc = new ResourceConfig()
                .register(ValidationFeature.class)
                .register(ca.ulaval.glo2003.ExceptionHandlers.ValidationExceptionMapper.class)
                .register(new HealthResource())
                .register(new SellerResource())
                .register(new ProductResource())
                .register(new OfferResource());

        GrizzlyHttpServerFactory.createHttpServer(URI.create(BASE_URI), rc);
    }

    public static void main(String[] args) {
        startServer();
        System.out.printf("Jersey app started with endpoints available at %s%n", BASE_URI);
    }
}
