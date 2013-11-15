package interfaces;

import city.Menu;

public interface Restaurant2Customer {

		public void msgHereIsYourCheck(String food, double price, Restaurant2Cashier c);
		
		public abstract void msgHereIsYourChange(double change);

		public String getName();
		
		public void gotHungry();
		
		public void msgAtDestination();
		
		public void msgTablesAreFull();

		public void msgFollowMeToTable(Restaurant2Waiter w, Menu m, int num, int waiterNum);

		public void msgAnimationFinishedGoToSeat();
		
		public void msgAnimationFinishedLeaveRestaurant();
		
		public void ReadyToOrder();
		
		public void msgWhatDoYouWant();
		
		public void msgHereIsYourFood(String food);
		
		public void msgPleaseReorder(String m);

}
