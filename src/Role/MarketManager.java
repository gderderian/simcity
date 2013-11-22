package Role;

import java.util.*;

import city.MarketOrder;
import city.PersonAgent;
<<<<<<< HEAD
import city.transportation.TruckAgent;
=======
>>>>>>> 95f9124601994057c3754bd9dd1cebc0b97917c3
import city.transportation.Vehicle;
import Role.Role;

public class MarketManager extends Role {

	// Data
	private String name;
	private double marketMoney;
	private ArrayList<myMarketWorker> myWorkers;
	private ArrayList<myMarketOrder> myOrders;
	private ArrayList<MarketItem> marketStock;
<<<<<<< HEAD
	private ArrayList<TruckAgent> marketTrucks;

	public enum orderState {pendingWorkerAssignment, assignedToWorker, pickedReady, givenToTruck, pendingBilling, billed, done};
	public enum deliveryType {inPerson, truckOrder};
	
	public enum itemType {food, car};
	
=======
	private ArrayList<Vehicle> marketTrucks;

	public enum orderState {pendingWorkerAssignment, assignedToWorker, inProgress, done};
	public enum deliveryType {inPerson, truckOrder};
	
>>>>>>> 95f9124601994057c3754bd9dd1cebc0b97917c3
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
<<<<<<< HEAD
		public itemType type;
		
=======
>>>>>>> 95f9124601994057c3754bd9dd1cebc0b97917c3
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
<<<<<<< HEAD
	
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
=======
>>>>>>> 95f9124601994057c3754bd9dd1cebc0b97917c3

	// Scheduler
	protected boolean pickAndExecuteAnAction(){
		if (!myOrders.isEmpty()) {
			for (myMarketOrder order : myOrders) {
				if (order.state == orderState.pendingWorkerAssignment){
					makeWorkerPrepareOrder(order);
					order.state = orderState.assignedToWorker;
					return true;
				}
<<<<<<< HEAD
				if (order.state == orderState.pickedReady){
					deliverOrder(order);
					return true;
				}
				if (order.state == orderState.pendingBilling){
					billRecipient(order);
					order.state = orderState.billed;
					return true;
				}
=======
>>>>>>> 95f9124601994057c3754bd9dd1cebc0b97917c3
			}
		}
		return false;
	}

	// Actions
	private void makeWorkerPrepareOrder(myMarketOrder o){ // Distribute load of incoming orders to all workers
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
<<<<<<< HEAD
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
=======
			
		}
	}
	
>>>>>>> 95f9124601994057c3754bd9dd1cebc0b97917c3
	
}