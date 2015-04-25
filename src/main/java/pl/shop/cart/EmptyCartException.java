package pl.shop.cart;

public class EmptyCartException extends Exception {

	private static final long serialVersionUID = 1L;

	public EmptyCartException() {
	};

	public EmptyCartException(String message) {
		super(message);
	}

}
