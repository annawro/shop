package pl.shop.test;

import static org.junit.Assert.assertEquals;

import org.easymock.EasyMock;
import org.junit.Before;
import org.junit.Test;

import pl.shop.cart.Cart;
import pl.shop.model.Product;
import pl.shop.warehouse.WarehouseService;

public class CartServiceTest {

	private Cart cart1;
	private WarehouseService warehouse;

	@Before
	public void init() {
		cart1 = new Cart();
		Product product1 = new Product("apple", "fresh apple ", "aple.jpg",
				60.0, 1);
		Product product2 = new Product("book", "interesting book", "book.jpg",
				35, 2);
		warehouse = EasyMock.createMock(WarehouseService.class);
		cart1.setWarehouse(warehouse);
		EasyMock.expect(warehouse.findProductById(1)).andReturn(product1).anyTimes();
		EasyMock.expect(warehouse.findProductById(2)).andReturn(product2).anyTimes();
		EasyMock.replay(warehouse);
		cart1.addProductToCart(1);
		cart1.addProductToCart(2);
	}

	@Test
	public void addProductToCartTest() {
		assertEquals(130, cart1.getPriceValue(), 0.5);
		assertEquals("interesting book", cart1.getCart().get(1)
				.getDescription());
		assertEquals(new Double(60.0), cart1.getCart().get(0).getPrice(), 0.5);

	}

	@Test
	public void removeProductFromCartTest() throws Exception {
		cart1.removeProductFromCart(1);
		assertEquals(70, cart1.getPriceValue(), 0.5);
		assertEquals(1, cart1.getCart().size());

	}

}
