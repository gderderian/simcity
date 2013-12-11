package Role;

import interfaces.MarketManager;
import interfaces.MarketWorker;

import java.util.*;
import java.util.concurrent.Semaphore;
import activityLog.ActivityLog;
import activityLog.ActivityTag;
import city.MarketOrder;
import city.OrderItem;
import city.PersonAgent;
import city.gui.Market.MarketWorkerGui;
import Role.Role;

public class MarketWorkerRole extends Role implements MarketWorker {
	
	String roleName = "MarketWorkerRole";
	
	// Data
	int numWorkingOrders;
	public List<PickableOrder> pickOrders;
	PersonAgent p;
	ActivityTag tag = ActivityTag.MARKETWORKER;
	
	private Semaphore isAnimating = new Semaphore(0,true);
	
	MarketWorkerGui gui;
	public enum orderPickState {pending, picking, done};
	
	public class PickableOrder {
		
		MarketOrder order; // Contains recipient, destination, list of OrderItems
		MarketManager recipientManager;
		public orderPickState state;
		Hashtable<String, Boolean> itemPickStatus; // Tracks the pick status of individual items in the market
		
		PickableOrder(MarketOrder incomingOrder, MarketManager initialSender){
			order = incomingOrder;
			state = orderPickState.pending;
			recipientManager = initialSender;
			itemPickStatus = new Hashtable<String, Boolean>();
			synchronized(incomingOrder.orders){
				for (OrderItem item : incomingOrder.orders){
					itemPickStatus.put(item.name, false);
				}
			}
		}
		
	}
	
	public MarketWorkerRole(PersonAgent person){
		p = person;
		pickOrders = Collections.synchronizedList(new ArrayList<PickableOrder>());
	}
	
	// Messages
	public void msgPrepareOrder(MarketOrder o, MarketManager recipientManager){
		log("I have a new order to process.");
		PickableOrder newPickableOrder = new PickableOrder(o, recipientManager);
		pickOrders.add(newPickableOrder);
		p.stateChanged();
	}

	// Scheduler
	public boolean pickAndExecuteAnAction() {
		synchronized(pickOrders){
			if (!pickOrders.isEmpty()) {
				for (PickableOrder o : pickOrders) {
					if (o.state == orderPickState.pending){
						pickSingleOrder(o);
						return true;
					}
				}
				for (PickableOrder o : pickOrders) {
					if (o.state == orderPickState.done){
						returnCompletedOrder(o);
						return true;
					}
				}
			}
		}
		return false;
	}
	
	// Actions
	private void pickSingleOrder(PickableOrder o){
		log("Picking order for my manager.");
		o.state = orderPickState.picking;
		for (OrderItem item : o.order.orders){
			// Gui command to go to that item's specific location in the market "warehouse"/back stock room
			o.itemPickStatus.put(item.name, true); // Item in this order has been picked
			// Generate spot on the shelf for this item
			int pickLocationY = 100 + (int)(Math.random() * 800); 
			gui.setDestination(500, pickLocationY);
			gui.beginAnimate();
			try {
				isAnimating.acquire();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		// When finished running through items to pick, then be done!
		
		// Move to deliver order to manager to give to person
		gui.setDestination(275, 250);
		gui.beginAnimate();
		try {
			isAnimating.acquire();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		// Move back to home position
		gui.setDestination(675, 250);
		gui.beginAnimate();
		try {
			isAnimating.acquire();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		o.state = orderPickState.done;
		p.stateChanged();
		
	}
	
	private void returnCompletedOrder(PickableOrder o){
		log("Notifying manager a customer's order is done!");
		o.recipientManager.msgOrderPicked(o.order);
		pickOrders.remove(o);
	}
	
	private void log(String msg){
		print(msg);
        ActivityLog.getInstance().logActivity(tag, msg, getName(), false);
	}

	public String getRoleName() {
		return roleName;
	}
	
	public void releaseSemaphore(){
		isAnimating.release();
	}

	public void setPerson(PersonAgent workerPerson) {
		p = workerPerson;
	}

	public void setGui(MarketWorkerGui setGui) {
		gui = setGui;
	}
	
	public MarketWorkerGui getGui() {
		return gui;
	}

	public PersonAgent getPerson() {
		return p;
	}	
	
}