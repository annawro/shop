package pl.shop.warehouse;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.stereotype.Service;

import pl.shop.model.Product;


@Service
public class WarehouseService {

	private List<Product> warehouse = new ArrayList<Product>();

	public boolean addProduct(Product product) {
		if(findProductById(product.getId())!=null) {
			return false;
		}
		return warehouse.add(product);
	}

	public boolean removeProduct(Product product) {
		boolean productIsRemove = false;
		if (!warehouse.isEmpty()) {
			warehouse.remove(product);
			productIsRemove = true;
		}
		return productIsRemove;
	}

	public Product updateProduct(Product product) {
		Product updateProduct = findProductById(product.getId());
		if (updateProduct == null) {
			return null;
		}

		updateProduct.setDescription(product.getDescription());
		updateProduct.setPrice(product.getPrice());
		updateProduct.setQuantity(product.getQuantity());
		updateProduct.setPhoto(product.getPhoto());

		return updateProduct;
	}

	public Product findProductById(int id) {
		for (Product productTemp : warehouse) {
			if (productTemp.getId() == id) {
				return productTemp;
			}
		}
		return null;
	}

	public Set<Product> findProductsByCriteria(List<Object> criteria) {
		Set<Product> productFindByCriteria = new HashSet<Product>();
		for (Product productTemp : warehouse) {
			for (Object criterion : criteria) {
				if ((productTemp.getName().equals(criterion))
						|| (productTemp.getDescription().equals(criterion))) {
					productFindByCriteria.add(productTemp);
				} else if (criterion instanceof Number) {
					if (productTemp.getPrice() == Double.valueOf(criterion
							.toString()))
						productFindByCriteria.add(productTemp);
				}
			}
		}
		return productFindByCriteria;
	}

	public List<Product> getProducts() {
		return warehouse;
	}
}
