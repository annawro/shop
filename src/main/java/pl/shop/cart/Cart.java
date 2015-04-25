package pl.shop.cart;

import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import pl.shop.model.Product;
import pl.shop.warehouse.WarehouseService;

@Path("/cart")
@Component
public class Cart {

	private List<Product> cart = new ArrayList<Product>();
	private double priceValue;

	@Autowired
	private WarehouseService warehouse;

	@GET
	@Path("/allCart")
	@Produces("application/json")
	public List<Product> getCart() {
		return cart;
	}

	public void setCart(List<Product> cart) {
		this.cart = cart;
	}

	public double getPriceValue() {
		return priceValue;
	}

	public void setWarehouse(WarehouseService warehouse) {
		this.warehouse = warehouse;
	}

	@POST
	@Path("/add/{id}")
	@Produces("application/json")
	@Consumes("application/json")
	public void addProductToCart(@PathParam("id") int id) {
		Product product = warehouse.findProductById(id);
		cart.add(product);
		priceValue += product.getPrice() * product.getQuantity();

	}

	@DELETE
	@Path("/remove/{id}")
	public void removeProductFromCart(@PathParam("id") int id)
			throws EmptyCartException {
		Product product = warehouse.findProductById(id);
		if ((product != null) && (!cart.isEmpty())) {
			cart.remove(product);
			priceValue -= product.getPrice() * product.getQuantity();
		} else
			throw new EmptyCartException("Your Cart is empty");
	}

	@Override
	public String toString() {
		return "Cart [cart=" + cart + ", priceValue=" + priceValue + "]";
	}

}
