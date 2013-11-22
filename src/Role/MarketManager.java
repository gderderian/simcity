package Role;

import java.util.*;

import city.MarketOrder;
import city.OrderItem;
import city.PersonAgent;
import city.transportation.TruckAgent;
import Role.Role;


public class MarketManager extends Role {

	// Data
	private String name;
	private double marketMoney;
	private ArrayList<myMarketWorker> myWorkers;
	private ArrayList<myMarketOrder> myOrders;
	private ArrayList<TruckAgent> marketTrucks;
	public Hashtable<String, MarketItem> marketStock;

	public enum orderState {pendingWorkerAssignment, assignedToWorker, pickedReady, givenToTruck, pendingBilling, billed, done};
	public enum deliveryType {inPerson, truckOrder};
	
	public enum itemType {food, car};
	
	MarketManager(String initialName){
		name = initialName;
		marketStock = new Hashtable<String, MarketItem>();
		marketStock.put("Pasta", new MarketItem("Pasta", 5, itemType.food));
		marketStock.put("Pizza", new MarketItem("Pizza", 5, itemType.food));
		marketStock.put("Chicken", new MarketItem("Chicken", 5, itemType.food));
		marketStock.put("Honda Accord", new MarketItem("Honda Accord", 5, itemType.car));
		marketStock.put("Honda Civic", new MarketItem("Honda Accord", 5, itemType.car));
	}
	
	public class myMarketOrder {
		MarketOrder order; // Contains recipient, destination, list of OrderItems
		orderState state;
		deliveryType type;
		PersonAgent assignedWorker;
		
		myMarketOrder(MarketOrder incomingOrder, orderState initialState, deliveryType initialType){
			order = incomingOrder;
			state = initialState;
			type = initialType;
		}
		
	}

	private class MarketItem { // Used for internal stock-tracking within the market
		public String itemName;
		public int quantity;
		public itemType type;
		
		MarketItem(String initItemName, int initialQuantity, itemType initialType){
			itemName = initItemName;
			quantity = initialQuantity;
			type = initialType;
		}
		
	}

	private class myMarketWorker { // Used for internal stock-tracking within the market
		public MarketWorker worker;
		public int numWorkingOrders;
	}
	
	// Messages
	public void msgHereIsOrder(MarketOrder o){
		myMarketOrder mo = new myMarketOrder(o, orderState.pendingWorkerAssignment, deliveryType.inPerson);
		myOrders.add(mo);
		stateChanged();
	}
	public void msgOrderPicked(MarketOrder o){
		myMarketOrder selectedMarketOrder = null;
		for (myMarketOrder order : myOrders) {
			if (order.order.equals(o)){
				selectedMarketOrder = order;
				return;
			}
		}
		selectedMarketOrder.state = orderState.pickedReady;
		stateChanged();
	}
	
	public void msgFinishedDelivery(MarketOrder o){
		myMarketOrder selectedMarketOrder = null;
		for (myMarketOrder order : myOrders) {
			if (order.order.equals(o)){
				selectedMarketOrder = order;
				return;
			}
		}
		selectedMarketOrder.state = orderState.pendingBilling;
		stateChanged();
	}
	// Scheduler
	public boolean pickAndExecuteAnAction(){
		if (!myOrders.isEmpty()) {
			for (myMarketOrder order : myOrders) {
				if (order.state == orderState.pendingWorkerAssignment){
					makeWorkerPrepareOrder(order);
					order.state = orderState.assignedToWorker;
					return true;
				}
				if (order.state == orderState.pickedReady){
					deliverOrder(order);
					return true;
				}
				if (order.state == orderState.pendingBilling){
					billRecipient(order);
					order.state = orderState.billed;
					return true;
				}
			}
		}
		return false;
	}

	// Actions
	private void makeWorkerPrepareOrder(myMarketOrder o){ // Distribute load of incoming orders to all workers
		
		// Decrement quantity of things in each order
		for (OrderItem item : o.order.orders){
		
			
			
		}
		
		
		if (myWorkers.size() != 0) {
			int initOrders = myWorkers.get(0).numWorkingOrders;
			myMarketWorker w_selected = null;
			for (myMarketWorker w : myWorkers){
				if (w.numWorkingOrders <= initOrders){
					initOrders = w.numWorkingOrders;
					w_selected = w;
				}
			}
			w_selected.worker.msgPrepareOrder(o.order);
			w_selected.numWorkingOrders++;
		}
	}
	
	private void deliverOrder(myMarketOrder o){
		if (o.type == deliveryType.inPerson){
			o.order.getRecipient().msgHereIsYourOrder(o.order);
			o.state = orderState.done;
		} else if (o.type == deliveryType.truckOrder){
			int initOrders = marketTrucks.get(0).getOrderNum(); // HACK, needs to maintain a myTrucks or something similar to avoid shared data
			TruckAgent selectedTruck = null;
			for (TruckAgent t : marketTrucks){
				if (t.orders.size() <= initOrders){
					initOrders = t.getOrderNum();
					selectedTruck = t;
				}
			}
			selectedTruck.msgPleaseDeliver(o.order);
			o.state = orderState.givenToTruck;
		}
	}
	
	private void billRecipient(myMarketOrder o){
		// o.order.getRecipient().msgMarketBill(); // Need to discuss with Holly on how to bill people
		o.state = orderState.billed;
	}

}