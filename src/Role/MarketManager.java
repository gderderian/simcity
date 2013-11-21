package Role;

import java.util.*;

import city.MarketOrder;
import city.PersonAgent;
import city.transportation.Vehicle;
import Role.Role;

public class MarketManager extends Role {

	// Data
	private String name;
	private double marketMoney;
	private ArrayList<myMarketWorker> myWorkers;
	private ArrayList<myMarketOrder> myOrders;
	private ArrayList<MarketItem> marketStock;
	private ArrayList<Vehicle> marketTrucks;

	public enum orderState {pendingWorkerAssignment, assignedToWorker, inProgress, done};
	public enum deliveryType {inPerson, truckOrder};
	
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

	// Scheduler
	protected boolean pickAndExecuteAnAction(){
		if (!myOrders.isEmpty()) {
			for (myMarketOrder order : myOrders) {
				if (order.state == orderState.pendingWorkerAssignment){
					makeWorkerPrepareOrder(order);
					order.state = orderState.assignedToWorker;
					return true;
				}
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
			
		}
	}
	
	
}