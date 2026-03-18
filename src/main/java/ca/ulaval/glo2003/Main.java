package ca.ulaval.glo2003;

import ca.ulaval.glo2003.repositories.IProductRepository;
import ca.ulaval.glo2003.repositories.ISellerRepository;
import ca.ulaval.glo2003.repositories.ProductRepository;
import ca.ulaval.glo2003.repositories.SellerRepository;
import ca.ulaval.glo2003.resources.HealthResource;
import ca.ulaval.glo2003.resources.OfferResource;
import ca.ulaval.glo2003.resources.ProductResource;
import ca.ulaval.glo2003.resources.SellerResource;
import org.glassfish.hk2.utilities.binding.AbstractBinder;
import org.glassfish.jersey.grizzly2.httpserver.GrizzlyHttpServerFactory;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.server.validation.ValidationFeature;

import java.net.URI;

public class Main {
    public static final String BASE_URI = "http://0.0.0.0:8080/";

    public static void startServer() {
        final ResourceConfig rc = new ResourceConfig()
                .packages("ca.ulaval.glo2003")
                .register(ValidationFeature.class)
                .register(ca.ulaval.glo2003.exceptionHandlers.ValidationExceptionMapper.class)
                .register(ca.ulaval.glo2003.exceptionHandlers.ProcessingExceptionMapper.class)
                .register(new HealthResource())
                .register(SellerResource.class)
                .register(ProductResource.class)
                .register(new OfferResource())
                .register(new AbstractBinder() {
                    @Override
                    protected void configure() {
                        bind(ProductRepository.INSTANCE).to(IProductRepository.class);
                        bind(SellerRepository.INSTANCE).to(ISellerRepository.class);
                    }
                });

        GrizzlyHttpServerFactory.createHttpServer(URI.create(BASE_URI), rc);
    }

    public static void main(String[] args) {
        startServer();
        System.out.printf("Jersey app started with endpoints available at %s%n", BASE_URI);
    }
}
