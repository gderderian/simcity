package city.transportation;

import interfaces.MarketManager;

import java.util.*;
import java.util.concurrent.Semaphore;

import activityLog.ActivityLog;
import activityLog.ActivityTag;
import astar.AStarTraversal;
import astar.Position;
import city.CityMap;
import city.MarketOrder;
import city.PersonAgent;

public class TruckAgent extends Vehicle {
	//Data
	MarketManager market; //Market that this truck reports to

	public List <MyMarketOrder> orders = Collections.synchronizedList(new ArrayList<MyMarketOrder>());

	private boolean test = false;
	
	String name = "Truck";

	ActivityTag tag = ActivityTag.TRUCK;

	class MyMarketOrder {
		MarketOrder o;
		boolean delivered;

		public MyMarketOrder(MarketOrder o) {
			this.o = o;
		}
	}

	public TruckAgent(AStarTraversal aStarTraversal, CityMap map) {
		super(aStarTraversal, map);
		capacity = 0;
		guiFinished = new Semaphore(0, true);
		
		type = "truck";

		currentPosition = new Position(17, 20);
		if(aStar != null)
			currentPosition.moveInto(aStar.getGrid());
	}

	//Messages
	public void msgPleaseDeliver(MarketOrder o) {
		orders.add(new MyMarketOrder(o));
		stateChanged();
	}

	public void msgOrderReceived(PersonAgent p, MarketOrder o) {
		MyMarketOrder temp = null;
		synchronized(orders) {
			for(MyMarketOrder mo : orders) {
				if(mo.o == o)
					temp = mo;
			}
			temp.delivered = true;
		}
		stateChanged();
	}

	public void msgGuiFinished() {
		guiFinished.release();
	}

	//Scheduler
	public boolean pickAndExecuteAnAction() {
		synchronized(orders) {
			for(MyMarketOrder mo : orders) {
				if(mo.delivered == true) {
					ReportToMarket(mo);
					return true;
				}
			}
		}

		synchronized(orders) {
			for(MyMarketOrder mo : orders) {
				if(mo.delivered == false) {
					DeliverOrder(mo);
					return true;
				}
			}
		}
		
		DriveAround(); //Demonstration of animation capabilities!

		return false;
	}

	//Actions
	private void DeliverOrder(MyMarketOrder o) {
		log("Going to " + o.o.destination);
		DoGoTo(o.o.destination);
		log("Delivering order from market to recipient");
		o.o.getRecipient().msgHereIsYourOrder(this, o.o);
		o.delivered = true;
		log("Returning to market");
		//Hack - must change to specific restaurant
		DoGoTo("mark1");
	}

	private void ReportToMarket(MyMarketOrder o) {
		market.msgFinishedDelivery(o.o);
		orders.remove(o);
	}
	
	private void DriveAround() {
		//Demonstration of animation capabilities - Driving loops around city.
		while(true) {
			guiMoveFromCurrentPositionTo(new Position(15, 6));
			guiMoveFromCurrentPositionTo(new Position(6, 6));
			guiMoveFromCurrentPositionTo(new Position(6, 12));
			guiMoveFromCurrentPositionTo(new Position(15, 12));
			
			guiMoveFromCurrentPositionTo(new Position(16, 5));
			guiMoveFromCurrentPositionTo(new Position(5, 5));
			guiMoveFromCurrentPositionTo(new Position(5, 13));
			guiMoveFromCurrentPositionTo(new Position(16, 13));
		}
	}

	// Accessors
	public void setMarketManager(MarketManager m) {
		market = m;
	}
	
	public int getOrderNum(){
		return orders.size();
	}

	private void log(String msg){
		print(msg);
		if(!test)
			ActivityLog.getInstance().logActivity(tag, msg, name);
	}

	public void thisIsATest() {
		test = true;
	}
}
