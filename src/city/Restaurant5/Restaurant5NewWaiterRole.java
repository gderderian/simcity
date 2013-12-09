package city.Restaurant5;

import city.PersonAgent;

public class Restaurant5NewWaiterRole extends Restaurant5WaiterRole {

	public Restaurant5NewWaiterRole(String name, PersonAgent person) {
		super(name, person);
		// TODO Auto-generated constructor stub
	
	}

	@Override
	public void BringOrderToCook(mycustomer customer) {
		// TODO Auto-generated method stub
		
		orderspindle.addToSpindle(this, customer.choice, customer.table);
		
	}
	
	
	

}
