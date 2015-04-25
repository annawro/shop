package pl.shop.shop;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import pl.shop.model.Product;
import pl.shop.warehouse.WarehouseService;

@Path("/shop")
@Component
public class Shop {

	@Autowired
	private WarehouseService warehouse;

	@POST
	@Path("/search")
	@Produces("application/json")
	@Consumes("application/json")
	public Set<Product> searchProductsInWarehouse(Product product) {
		List<Object> criteria = new ArrayList<Object>();
		criteria.add(product.getName());
		criteria.add(product.getDescription());
		criteria.add(product.getPrice());
		Set<Product> products = warehouse.findProductsByCriteria(criteria);
		return products;

	}

	public void setWarehouse(WarehouseService warehouse) {
		this.warehouse = warehouse;
	}

	@GET
	@Path("/products")
	@Produces("application/json")
	public List<Product> productsInWarehouse() {
		return warehouse.getProducts();
	}

}
