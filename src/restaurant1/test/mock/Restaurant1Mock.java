package restaurant1.test.mock;

/**
 * This is the base class for all mocks.
 *
 * @author Sean Turner
 *
 */
public class Restaurant1Mock {
	private String name;

	public Restaurant1Mock(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public String toString() {
		return this.getClass().getName() + ": " + name;
	}

}
