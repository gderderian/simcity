
package city.transportation;

import Role.MarketManagerRole;

import interfaces.MarketManager;
import interfaces.Person;

import java.util.*;
import java.util.concurrent.Semaphore;

import activityLog.ActivityLog;
import activityLog.ActivityTag;
import astar.AStarTraversal;
import astar.Position;
import city.CityMap;
import city.MarketOrder;
import city.PersonAgent;
import city.transportation.CarAgent.CarEvent;

public class TruckAgent extends Vehicle {
	//Data
	MarketManager market; //Market that this truck reports to

	public List <MyMarketOrder> orders = Collections.synchronizedList(new ArrayList<MyMarketOrder>());

	private boolean test = false;
	
	String name = "Truck";

	ActivityTag tag = ActivityTag.TRUCK;
	
	private enum DeliveryState { pending, delayed, delivered, awaitingConfirmation };

	class MyMarketOrder {
		MarketOrder o;
		DeliveryState state;

		public MyMarketOrder(MarketOrder o) {
			this.o = o;
			state = DeliveryState.pending;
		}
	}

	public TruckAgent(AStarTraversal aStarTraversal, CityMap map) {
		super(aStarTraversal, map);
		capacity = 0;
		guiFinished = new Semaphore(0, true);
		
		type = "truck";

		currentPosition = new Position(10, 7);
		if(aStar != null)
			currentPosition.moveInto(aStar.getGrid());
	}

	//Messages
	public void msgPleaseDeliver(MarketOrder o) {
		orders.add(new MyMarketOrder(o));
		stateChanged();
	}

	public void msgOrderReceived(Person p, MarketOrder o) {
		log("Received message: order has been successfully received.");
		synchronized(orders) {
			for(MyMarketOrder mo : orders) {
				if(mo.o == o)
					mo.state = DeliveryState.delivered;
			}
		}
		stateChanged();
	}
	
	public void msgRestaurantIsClosed(Person p, MarketOrder o) {
		log("Restaurant is closed. Will try delivery again later.");
		synchronized(orders) {
			for(MyMarketOrder mo : orders) {
				if(mo.o == o)
					mo.state = DeliveryState.delayed;
			}
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
				if(mo.state == DeliveryState.delivered) {
					ReportToMarket(mo);
					return true;
				}
			}
		}

		synchronized(orders) {
			for(MyMarketOrder mo : orders) {
				if(mo.state == DeliveryState.pending) {
					DeliverOrder(mo);
					return true;
				}
			}
		}
		
		synchronized(orders) {
			for(MyMarketOrder mo : orders) {
				if(mo.state == DeliveryState.delayed) {
					RetryDelivery(mo);
					return true;
				}
			}			
		}

		return false;
	}

	//Actions
	private void DeliverOrder(MyMarketOrder o) {
		if(aStar == null) {
			log("Picking up order");
			log("Delivering order from market to recipient");
			o.o.getRecipient().msgHereIsYourOrder(this, o.o);
			o.state = DeliveryState.awaitingConfirmation;
			
			parkTruck();
			return;
		}
		
		gui.setVisible();
		
		log("Picking up order from market");
		DriveToMarket();
		
		log("Going to " + o.o.destination);
		DriveTo(o.o.destination);
		
		log("Delivering order from market to " + o.o.getRecipient().getName());
		o.o.getRecipient().msgHereIsYourOrder(this, o.o);
		o.state = DeliveryState.awaitingConfirmation;
		
		parkTruck();
		
	}
	
	private void RetryDelivery(MyMarketOrder o) {
		gui.setVisible();
		log("Going to " + o.o.destination);
		DriveTo(o.o.destination);
		
		log("Delivering order from market to " + o.o.getRecipient().getName());
		o.o.getRecipient().msgHereIsYourOrder(this, o.o);
		o.state = DeliveryState.awaitingConfirmation;
		
		parkTruck();
	}

	private void ReportToMarket(MyMarketOrder o) {
		market.msgFinishedDelivery(o.o);
		orders.remove(o);
	}
	
	private void DriveToMarket() {
		String dest = market.getMarketName();
		
		if(aStar == null) {
			log("Driving to the market...");
			return;
		}
		
		int x = cityMap.getX(dest);
		int y = cityMap.getY(dest);
		
		gui.setVisible();
		
		if(x < 4 && y < 4) {
			moveTo(3,3);
		} else if(x > 17 && y < 4) {
			moveTo(18,3);
		} else if(x < 4 && y > 14) {
			moveTo(3, 15);
		} else if(x == 0) {
			moveTo(3, y);
		} else if(x == 21) {
			moveTo(18, y);
		} else if(y == 0) {
			moveTo(x, 3);
		} else if(y == 18) {
			moveTo(x, 15);
		} else if(y == 17) {
			moveTo(x, 15);
		} else if(y == 10) {
			moveTo(x, 12);
		} else
			log("ERROR: Unexpected driving destination - see driveToMarket() in TruckAgent.");
	}
	
	private void DriveTo(String dest) {

		if(aStar == null) {
			log("Driving to " + dest);
			return;
		}
		
		int x = cityMap.getX(dest);
		int y = cityMap.getY(dest);

		if(x < 4 && y < 4) {
			moveTo(3,3);
		} else if(x > 17 && y < 4) {
			moveTo(18,3);
		} else if(x < 4 && y > 14) {
			moveTo(3, 15);
		} else if(x == 0) {
			moveTo(3, y);
		} else if(x == 21) {
			moveTo(18, y);
		} else if(y == 0) {
			moveTo(x, 3);
		} else if(y == 18) {
			moveTo(x, 15);
		} else if(y == 17) {
			moveTo(x, 15);
		} else if(y == 10) {
			moveTo(x, 12);
		} else
			log("ERROR: Unexpected driving destination - see driveTo(String) in TruckAgent.");
	}
	
	private void parkTruck() {
		if(aStar == null) {
			log("Driving to nearest parking entrance.");
			return;
		}
		Position parkingEntrance = cityMap.getParkingEntrance(currentPosition);
		log("Parking...");
		guiMoveFromCurrentPositionTo(parkingEntrance);
		gui.setInvisible();
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
			ActivityLog.getInstance().logActivity(tag, msg, name, false);
	}

	public void thisIsATest() {
		test = true;
	}
}
