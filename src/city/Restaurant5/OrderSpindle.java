package city.Restaurant5;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class OrderSpindle {
	
	enum state {recordedbywaiter, pending, cooking, donecooking, waiting, pickedupbywaiter};
	List<CustomerOrder> customerorders;
	
	public OrderSpindle () {
		
		customerorders = Collections.synchronizedList( new ArrayList<CustomerOrder>());
		
	}
	
	public void addToSpindle(Restaurant5WaiterRole setwaiter, String setorder, int settable)
	{
		
		customerorders.add(new CustomerOrder( setorder, settable, setwaiter));
		
	}
	
	
	public class CustomerOrder {
		
		public int table;
		public String order;
		public Restaurant5WaiterRole waiter;
		public state orderstate;
		
		
		public CustomerOrder(String setorder, int settable, Restaurant5WaiterRole setwaiter)
		{
			order = setorder;
			table = settable;
			waiter = setwaiter;
			orderstate = state.recordedbywaiter;
		}

	}
	
	
}
