package pl.shop.test;

import static org.junit.Assert.*;


import java.util.Set;
import org.junit.Before;
import org.junit.Test;
import pl.shop.model.Product;
import pl.shop.shop.Shop;
import pl.shop.warehouse.WarehouseService;

public class ShopTest {

	private Shop shop;

	@Before
	public void init() {
		shop = new Shop();
		WarehouseService warehouseService = new WarehouseService();
		shop.setWarehouse(warehouseService);
		Product product1 = new Product("Book", "interesting book", "book.jpg",
				35, 2);
		Product product2 = new Product("Spring", "good book", "book1.jpg", 30,
				1);
		Product product3 = new Product("Book", "good book", "book3.jpg", 37,
				1);
		warehouseService.addProduct(product1);
		warehouseService.addProduct(product2);
		warehouseService.addProduct(product3);
	}

	@Test
	public void searchProductsInWarehouseTest() {
		Product product = new Product("Book", "interesting book", "book.jpg",
				35, 2);
		Set<Product> findProducts = shop.searchProductsInWarehouse(product);
		assertEquals(2,findProducts.size());

	}
}
