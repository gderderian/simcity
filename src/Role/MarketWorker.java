package Role;

import java.util.*;

import test.mock.LoggedEvent;
import activityLog.ActivityLog;
import activityLog.ActivityTag;
import city.MarketOrder;
import city.OrderItem;
import city.PersonAgent;
import Role.Role;

public class MarketWorker extends Role {
	
	MarketWorker(PersonAgent person){
		p = person;
	}
	
	// Data
	int numWorkingOrders;
	private ArrayList<PickableOrder> pickOrders;
	PersonAgent p;
	
	String name = p.getName();
	ActivityTag tag = ActivityTag.MARKETWORKER;
	
	public enum orderPickState {pending, picking, done};
	
	public class PickableOrder {
		MarketOrder order; // Contains recipient, destination, list of OrderItems
		MarketManager recipientManager;
		orderPickState state;
		Hashtable<String, Boolean> itemPickStatus; // Tracks the pick status of individual items in the market
		
		PickableOrder(MarketOrder incomingOrder, MarketManager initialSender){
			order = incomingOrder;
			state = orderPickState.pending;
			recipientManager = initialSender;
			for (OrderItem item : incomingOrder.orders){
				itemPickStatus.put(item.name, false);
			}
		}
		
	}
	
	// Messages
	public void msgPrepareOrder(MarketOrder o, MarketManager recipientManager){
		PickableOrder newPickableOrder = new PickableOrder(o, recipientManager);
		pickOrders.add(newPickableOrder);
		p.stateChanged();
	}

	// Scheduler
	public boolean pickAndExecuteAnAction() {
		if (!pickOrders.isEmpty()) {
			for (PickableOrder o : pickOrders) {
				if (o.state == orderPickState.pending){
					pickSingleOrder(o);
					return true;
				}
				if (o.state == orderPickState.done){
					returnCompletedOrder(o);
					return true;
				}
			}
		}
		return false;
	}
	
	// Actions
	private void pickSingleOrder(PickableOrder o){
		for (OrderItem item : o.order.orders){
			// Gui command to go to that item's specific location in the market "warehouse"/back stock room
			o.itemPickStatus.put(item.name, true); // Item in this order has been picked
		}
		// When finished running through items to pick, then be done!
		o.state = orderPickState.done;
	}
	
	private void returnCompletedOrder(PickableOrder o){
		o.recipientManager.msgOrderPicked(o.order);
		pickOrders.remove(o);
	}
	
	private void log(String msg){
		print(msg);
        ActivityLog.getInstance().logActivity(tag, msg, name);
	}
	
}