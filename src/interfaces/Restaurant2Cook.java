package interfaces;

import java.util.HashMap;
import city.Restaurant2.Restaurant2CookRole.Order;

public interface Restaurant2Cook {
		
		public void msgHereIsOrder(Restaurant2Waiter w, String choice, int table);
		
		public void msgFoodDone(Order o);
		
		public void msgFailedOrder(HashMap<String, Integer> failedOrder);
		
		public void msgHereIsShipment(HashMap<String, Integer> goodOrder);
		
		public void msgOutOfAllFood(Restaurant2Market m);
		
		public void msgGotFood();

}
