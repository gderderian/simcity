package city.Restaurant5;

import city.PersonAgent;

public class Restaurant5RegularWaiterRole extends Restaurant5WaiterRole {

	public Restaurant5RegularWaiterRole(String name, PersonAgent person) {
		super(name, person);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void BringOrderToCook(mycustomer customer) {
		Dogotokitchen();
		try {
			atKitchen.acquire();
		}
		catch (InterruptedException e) {
			e.printStackTrace();
		}

		cook.msgReceviedOrderFromWaiter(this, customer.choice, customer.table);


		Dogobacktolobby();
		atlobbycurrently=false;
		//log("NUMBER OF PERMITS = " + atLobby.availablePermits());
		try {
			atLobby.acquire();
			//atLobby.acquire();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		atlobbycurrently = true;
		
	}

	
	
	
}
