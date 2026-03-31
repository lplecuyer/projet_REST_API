package ca.ulaval.glo2003;

import ca.ulaval.glo2003.repositories.IProductRepository;
import ca.ulaval.glo2003.repositories.ISellerRepository;
import ca.ulaval.glo2003.repositories.inmemory.ProductRepository;
import ca.ulaval.glo2003.repositories.inmemory.SellerRepository;
import ca.ulaval.glo2003.repositories.mongo.ProductRepositoryMongo;
import ca.ulaval.glo2003.repositories.mongo.SellerRepositoryMongo;
import ca.ulaval.glo2003.resources.HealthResource;
import ca.ulaval.glo2003.resources.OfferResource;
import ca.ulaval.glo2003.resources.ProductResource;
import ca.ulaval.glo2003.resources.SellerResource;
import ca.ulaval.glo2003.persistence.mongodb.MorphiaDatastoreProvider;
import ca.ulaval.glo2003.services.OfferService;
import ca.ulaval.glo2003.services.ProductService;
import ca.ulaval.glo2003.services.SellerService;
import dev.morphia.Datastore;
import org.glassfish.hk2.utilities.binding.AbstractBinder;
import org.glassfish.jersey.grizzly2.httpserver.GrizzlyHttpServerFactory;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.server.validation.ValidationFeature;

import java.net.URI;

public class Main {
    public static final String BASE_URI = "http://0.0.0.0:8080/";

    public static void startServer() {
        String persistence = System.getProperty("persistence", "inmemory");

        IProductRepository productRepository;
        ISellerRepository sellerRepository;

      if ("mongo".equalsIgnoreCase(persistence)) {
        Datastore datastore = MorphiaDatastoreProvider.getDatastore();

        productRepository = new ProductRepositoryMongo(datastore);
        sellerRepository = new SellerRepositoryMongo(datastore);
      } else {
        productRepository = new ProductRepository();
        sellerRepository = new SellerRepository();
      }

        final ResourceConfig rc = new ResourceConfig()
                .packages("ca.ulaval.glo2003")
                .register(ValidationFeature.class)
                .register(ca.ulaval.glo2003.exceptionhandlers.ValidationExceptionMapper.class)
                .register(ca.ulaval.glo2003.exceptionhandlers.ProcessingExceptionMapper.class)
                .register(new HealthResource())
                .register(SellerResource.class)
                .register(ProductResource.class)
                .register(new AbstractBinder() {
                    @Override
                    protected void configure() {
                        bind(productRepository).to(IProductRepository.class);
                        bind(sellerRepository).to(ISellerRepository.class);
                        bind(ProductService.class).to(ProductService.class);
                        bind(SellerService.class).to(SellerService.class);
                        bind(OfferService.class).to(OfferService.class);
                    }
                });

        GrizzlyHttpServerFactory.createHttpServer(URI.create(BASE_URI), rc);
    }

    public static void main(String[] args) {
        startServer();
        System.out.printf("Jersey app started with endpoints available at %s%n", BASE_URI);
    }
}
