package interfaces;

import java.util.HashMap;

import city.MarketOrder;
import city.Restaurant2.Order;

public interface Restaurant2Cook {
		
		public void msgHereIsOrder(Restaurant2Waiter w, String choice, int table);
		
		public void msgFoodDone(Order o);
		
		public void msgFailedOrder(HashMap<String, Integer> failedOrder);
		
		public void msgHereIsYourOrder(MarketOrder goodOrder);
		
		//public void msgOutOfAllFood(Restaurant2Market m);
		
		public void msgGotFood();

}
