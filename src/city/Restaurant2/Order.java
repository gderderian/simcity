package city.Restaurant2;

import interfaces.Restaurant2Waiter;
import city.Restaurant2.Restaurant2CookRole.OrderState;

public class Order {
		Restaurant2Waiter w;
		String choice;
		int table;
		OrderState s;
		
		Order(Restaurant2Waiter wa, String c, int t){
			w = wa;
			choice = c;
			table = t;
			s = OrderState.pending;
		}
		
		void setState(OrderState state){
			s = state;
		}
		
		Restaurant2Waiter getWaiter(){
			return w;
		}
		
		String getChoice(){
			return choice;
		}
		
		int getTable(){
			return table;
		}

}
