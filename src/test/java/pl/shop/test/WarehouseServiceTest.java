package pl.shop.test;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;

import pl.shop.model.Product;
import pl.shop.warehouse.WarehouseService;

public class WarehouseServiceTest {

	WarehouseService warehouseService;

	@Before
	public void init() {
		warehouseService = new WarehouseService();
		Product product1 = new Product("Book", "interesting book", "book.jpg",
				35, 2);
		Product product2 = new Product("Spring", "good book", "book1.jpg", 30,
				1);
		warehouseService.addProduct(product1);
		warehouseService.addProduct(product2);
	}

	@Test
	public void addProductTest() {
		Product product3 = new Product("cap", "fashionable cap", "cap.jpg",
				15.49, 5);
		boolean add = warehouseService.addProduct(product3);
		assertEquals(true, add);
		assertEquals(3, warehouseService.getProducts().size());
		assertEquals("cap", warehouseService.getProducts().get(2).getName());
	}

	@Test
	public void removeProductTest() {
		Product product1 = warehouseService.getProducts().get(0);
		boolean remove = warehouseService.removeProduct(product1);
		assertEquals(true, remove);
		assertEquals(1, warehouseService.getProducts().size());
		assertEquals("Spring", warehouseService.getProducts().get(0).getName());

	}

	@Test
	public void findProductByIdTest() {
		int id = warehouseService.getProducts().get(0).getId();
		Product product = warehouseService.findProductById(id);
		assertEquals(warehouseService.getProducts().get(0).getId(),
				product.getId());
		assertEquals("Book", product.getName());

	}

	@Test
	public void updateProductTest() {
		Product product = warehouseService.getProducts().get(0);
		product.setDescription("interesting geographic book");
		assertEquals("interesting geographic book", warehouseService
				.updateProduct(product).getDescription());

	}

	@Test
	public void findProductsByCriteriaTest() {
		List<Object> criteria = new ArrayList<Object>();
		criteria.add(warehouseService.getProducts().get(0).getName());
		criteria.add(35);
		Set<Product> products = warehouseService
				.findProductsByCriteria(criteria);
		assertEquals(1, products.size());
		assertEquals(true,
				products.contains(warehouseService.getProducts().get(0)));

	}

}
