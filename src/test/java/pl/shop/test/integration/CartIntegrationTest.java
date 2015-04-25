package pl.shop.test.integration;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;

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
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Ignore;
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
import pl.shop.cart.EmptyCartException;
import pl.shop.model.Product;
import pl.shop.warehouse.WarehouseService;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:dispatcher-servlet.xml" })
@DirtiesContext
@Ignore
public class CartIntegrationTest implements ApplicationContextAware {

	private static final String REMOVE = "remove";
	private static final String ADD = "add";
	private static final String ALL_CART = "allCart";
	private static final int PORT=12789;
	private static final String HTTP_LOCALHOST_CART = "http://localhost:"+PORT+"/cart/";
	private static TJWSEmbeddedJaxrsServer server;
	private static List<Product> products;

	ApplicationContext applicationContext;
	WarehouseService warehouse;
	Cart cart;

	@BeforeClass
	public static void start() throws InterruptedException {
		Product product1 = new Product("Book", "interesting book", "book.jpg",
				35, 2);
		Product product2 = new Product("Spring", "good book", "book1.jpg", 30,
				1);
		Product product3 = new Product("gloves", "woolen, green gloves",
				"gloves.jpg", 25, 1);

		products = new ArrayList<>();
		products.add(product1);
		products.add(product2);
		products.add(product3);
		server = TestServer.getInstance().startServerOnPort(PORT);
	}

	@Before
	public void init() throws EmptyCartException {
		ResteasyDeployment deployment = server.getDeployment();
		Dispatcher dispatcher = deployment.getDispatcher();
		SpringBeanProcessor processor = new SpringBeanProcessor(dispatcher,
				deployment.getRegistry(), deployment.getProviderFactory());
		((ConfigurableApplicationContext) applicationContext)
				.addBeanFactoryPostProcessor(processor);
		SpringResourceFactory noDefaults = new SpringResourceFactory("cart",
				applicationContext, Cart.class);
		dispatcher.getRegistry().addResourceFactory(noDefaults);

		warehouse = applicationContext.getBean(WarehouseService.class);
		cart = applicationContext.getBean(Cart.class);

		List<Integer> idsToClean = new ArrayList<>();
		for (Product p : cart.getCart()) {
			idsToClean.add(p.getId());
		}

		for (Integer id : idsToClean) {
			cart.removeProductFromCart(id);
		}
		productInitializer();
	}

	@SuppressWarnings("unchecked")
	@Test
	public void testProductsInCart() {
		Response response = setUpClient(ALL_CART).get();
		List<Product> productsInCart = (List<Product>) response
				.readEntity(List.class);
		System.out.println(productsInCart);
		assertEquals(2, productsInCart.size());
		response.close();

	}

	@SuppressWarnings("static-access")
	@Test
	public void testAddProductToCart() {

		Product product = warehouse.getProducts().get(2);
		int addForm = product.getId();

		Response response = setUpClient(ADD + "/" + addForm).post(
				Entity.entity(addForm, "application/json"));

		response.ok();
		System.out.println(cart.getCart());
		assertEquals(3, cart.getCart().size());
		response.close();

	}

	@SuppressWarnings("static-access")
	@Test
	public void testDeleteProductFromCart() {

		Product product = warehouse.getProducts().get(1);
		int deleteForm = product.getId();

		Response response = setUpClient(REMOVE + "/" + deleteForm).delete();

		response.ok();
		System.out.println(cart.getCart());
		assertEquals(1, cart.getCart().size());
		response.close();

	}

	private void productInitializer() {
		for (Product product : products) {
			warehouse.addProduct(product);
		}
		cart.addProductToCart(warehouse.getProducts().get(0).getId());
		cart.addProductToCart(warehouse.getProducts().get(1).getId());
	}

	private Builder setUpClient(String name) {
		ResteasyClient client = new ResteasyClientBuilder().build();
		Builder target = client.target(HTTP_LOCALHOST_CART + name)
				.request();
		return target;
	}

	@AfterClass
	public static void stop() throws InterruptedException {
		server.stop();		
		server = null;
	}

	@Override
	public void setApplicationContext(final ApplicationContext context)
			throws BeansException {
		applicationContext = context;
	}

}
