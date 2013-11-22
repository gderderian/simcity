package city.Restaurant5;
import tomtesting.interfaces.Restaurant5Customer;
import tomtesting.interfaces.Restaurant5Waiter;

public class Restaurant5Check {
	//fucking check
	public int total;
	public int subtotal;
	public Restaurant5Customer customer;
	public Restaurant5Waiter waiter;
	public int assignedtable;
	public int amountcustomerpaid;
	
	public Restaurant5Check(Restaurant5Customer customer, int total, int assignedtable)
	{
		this.customer = customer;
		this.total = total;
		this.assignedtable = assignedtable;
	}
	
	void amountcustomerpaid( int amountcustomerpaid) {
		this.amountcustomerpaid = amountcustomerpaid; 
	}
	
	public void setwaiter(Restaurant5Waiter waiter2) {
		this.waiter = waiter2;
	}
	
	int returnamountcustomerpaid() {
		return this.amountcustomerpaid;
	}
	
	int returntotal() {
		return this.total;
	}
	
	void setsubtotal( int subtotal) {
		this.subtotal = subtotal;
	}
	
}
