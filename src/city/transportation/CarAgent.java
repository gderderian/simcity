package city.transportation;

import interfaces.Car;
import interfaces.Person;

import java.util.concurrent.Semaphore;

import city.CityMap;

import activityLog.ActivityLog;
import activityLog.ActivityTag;
import astar.AStarTraversal;
import astar.Position;

public class CarAgent extends Vehicle implements Car {
	//Data
	public CarEvent event = CarEvent.none;
	public CarState state = CarState.parked;
	
	public Person owner = null; //Car owner
	
	public String destination = null;
	
	private Position ownerLocation = null;
	
	public enum CarEvent { none, drivingToOwner, arrivingAtOwner, drivingToDestination, arrivingAtDestination, parking };
	public enum CarState { parked, driving, arrived, atOwner };
	
	String name = "Car";
	
	private boolean test = false;
	
	ActivityTag tag = ActivityTag.CAR;
	
	public CarAgent(AStarTraversal aStar, CityMap map) {
		super(aStar, map);
		
		capacity = 1;
		type = "car";
		guiFinished = new Semaphore(0, true);
		
		
		currentPosition = new Position(11, 11);
		currentPosition.moveInto(aStar.getGrid());
	}
	
	//Messages
	public void msgPickMeUp(Person p, Position pos) {
		log("Received message: Pick me up!");
		owner = p;
		ownerLocation = pos;
		event = CarEvent.drivingToOwner;
		log("Going to pick up my owner");
		stateChanged();
	}
	
	public void msgDriveTo(Person p, String dest) {
		log("Received message: Drive to " + dest + "!");
		destination = dest;
		event = CarEvent.drivingToDestination;
		stateChanged();
	}
	
	public void msgParkCar(Person p) {
		log("Received message: Go park yourself!");
		event = CarEvent.parking;
		destination = null;
		stateChanged();
	}
	
	public void msgGuiFinished() {
		guiFinished.release();
	}

	//Scheduler
	public boolean pickAndExecuteAnAction() {
		if(state == CarState.parked && event == CarEvent.drivingToOwner) {
			state = CarState.driving;
			driveToOwner();
			return true;
		}
		if(state == CarState.driving && event == CarEvent.arrivingAtOwner) {
			state = CarState.atOwner;
			pickUpOwner();
			return true;
		}
		if(state == CarState.atOwner && event == CarEvent.drivingToDestination) {
			state = CarState.driving;
			driveToDestination();
			return true;
		}
		if(state == CarState.driving && event == CarEvent.arrivingAtDestination) {
			state = CarState.arrived;
			tellOwnerWeHaveArrived();
			return true;
		}
		if(state == CarState.arrived && event == CarEvent.parking) {
			state = CarState.parked;
			parkCar();
			return true;
		}
		
		return false;
	}
	
	//Actions
	private void driveToOwner() {
		
		int x = ownerLocation.getX();
		int y = ownerLocation.getY();
		
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
		} else
			log("ERROR: Unexpected driving destination - see driveToOwner() in CarAgent.");

		event = CarEvent.arrivingAtOwner;
	}
	private void driveToDestination() {
		
		int x = cityMap.getX(destination);
		int y = cityMap.getY(destination);

		log("Driving to " + destination);
		
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
		} else { log("ERROR: Unexpected driving destination - see driveToDestination() in CarAgent."); }
		
		event = CarEvent.arrivingAtDestination;
	}
	
	private void pickUpOwner() {
		log("I'm here to pick you up!");
		owner.msgImPickingYouUp(this, currentPosition);
	}
	
	private void tellOwnerWeHaveArrived() {
		log("We have arrived at our destination!");
		owner.msgArrived(this, currentPosition);
	}
	
	private void parkCar() {
		if(aStar == null) {
			log("Driving to nearest parking entrance.");
			return;
		}
		Position parkingEntrance = cityMap.getParkingEntrance(currentPosition);
		log("Parking...");
		guiMoveFromCurrentPositionTo(parkingEntrance);
		gui.setInvisible();
		
		event = CarEvent.none;
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
