package pl.shop.test.integration;

import static org.junit.Assert.assertEquals;

import java.util.List;
import java.util.Set;

import javax.ws.rs.client.Entity;
import javax.ws.rs.client.Invocation.Builder;
import javax.ws.rs.core.Response;

import org.jboss.resteasy.client.jaxrs.ResteasyClient;
import org.jboss.resteasy.client.jaxrs.ResteasyClientBuilder;
import org.jboss.resteasy.core.Dispatcher;
import org.jboss.resteasy.plugins.server.tjws.TJWSEmbeddedJaxrsServer;
import org.jboss.resteasy.plugins.spring.SpringBeanProcessor;
import org.jboss.resteasy.plugins.spring.SpringResourceFactory;
import org.jboss.resteasy.spi.ResteasyDeployment;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.support.DirtiesContextTestExecutionListener;

import pl.shop.cart.Cart;
import pl.shop.model.Product;
import pl.shop.shop.Shop;
import pl.shop.warehouse.WarehouseService;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:dispatcher-servlet.xml" })
@DirtiesContext
public class ShopIntegrationTest implements ApplicationContextAware {

	private static final String PRODUCTS = "products";

	private static final String SEARCH = "search";
	
	private static final int PORT = 12345;

	private static final String HTTP_LOCALHOST_SHOP = "http://localhost:"+PORT+"/shop/";

	private static TJWSEmbeddedJaxrsServer server;

	ApplicationContext applicationContext;

	WarehouseService warehouse;

	@BeforeClass
	public static void start() {

		server = TestServer.INSTANCE.startServerOnPort(PORT);
	}

	@Before
	public void init() {
		ResteasyDeployment deployment = server.getDeployment();
		Dispatcher dispatcher = deployment.getDispatcher();
		SpringBeanProcessor processor = new SpringBeanProcessor(dispatcher,
				deployment.getRegistry(), deployment.getProviderFactory());
		((ConfigurableApplicationContext) applicationContext)
				.addBeanFactoryPostProcessor(processor);
		SpringResourceFactory noDefaults = new SpringResourceFactory("shop",
				applicationContext, Shop.class);
		dispatcher.getRegistry().addResourceFactory(noDefaults);

		
		warehouse = applicationContext.getBean(WarehouseService.class);
		
		
	}

	@SuppressWarnings("unchecked")
	@Test
	public void testProductsInWarehouse() throws Exception {

		productInitializer();

		Response response = setUpClient(PRODUCTS).get();
		List<Product> products = (List<Product>) response
				.readEntity(List.class);
		assertEquals(2, products.size());
		response.close();

	}

	@SuppressWarnings("unchecked")
	@Test
	public void testFindProducts() throws Exception {

		productInitializer();

		Product searchForm = new Product("Spring", null, null, 0, 0);

		Response response = setUpClient(SEARCH).post(
				Entity.entity(searchForm, "application/json"));

		Set<Product> products = (Set<Product>) response.readEntity(Set.class);
		assertEquals(1, products.size());
		response.close();

	}

	private void productInitializer() {
		Product product1 = new Product("Book", "interesting book", "book.jpg",
				35, 2);
		Product product2 = new Product("Spring", "good book", "book1.jpg", 30,
				1);
		warehouse.addProduct(product1);
		warehouse.addProduct(product2);
	}

		
	private Builder setUpClient(String name) {
		ResteasyClient client = new ResteasyClientBuilder().build();
		Builder target = client.target(HTTP_LOCALHOST_SHOP + name)
				.request();
		return target;

	}

	@AfterClass
	public static void stop() {
		server.stop();
		server =null;
		
	}

	@Override
	public void setApplicationContext(final ApplicationContext context)
			throws BeansException {
		applicationContext = context;
	}

}
