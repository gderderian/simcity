package city.Restaurant3;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.Timer;

/**
 * Order for Restaurant3
 */
public class Order {

		public enum orderStatus {waiting, preparing, ready, bounceBack};
		
		String foodItem;
		int recipTable;
		WaiterRole3 requestingWaiter;
		CookRole3 cookReference;
		Timer foodTimer;
		orderStatus status;
		
		public Order(WaiterRole3 w){
			requestingWaiter = w;
			status = orderStatus.waiting;
		}
		
		public Order(WaiterRole3 w, CookRole3 cook){
			requestingWaiter = w;
			status = orderStatus.waiting;
		}
		
		public Order(){
			status = orderStatus.waiting;
		}
		
		public Order(CustomerRole3 c, WaiterRole3 w, String foodChoice){
			requestingWaiter = w;
			foodItem = foodChoice;
			status = orderStatus.waiting;
		}
		
		public void setPreparing(){
			status = orderStatus.preparing;
		}
		
		public orderStatus getStatus(){
			return status;
		}
		
		public String getFoodName(){
			return foodItem;
		}
		
		public WaiterRole3 getWaiter(){
			return requestingWaiter;
		}
		
		public void setCooking(int cookTime){
			foodTimer = new Timer(cookTime,
					new ActionListener() { public void actionPerformed(ActionEvent event) {
			          status = orderStatus.ready;
			          foodTimer.stop();
			          if (cookReference != null){
			        	  cookReference.person.stateChanged();
			          }
			      }
			});
			foodTimer.start();
		}

	}