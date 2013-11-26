package city.transportation;

import interfaces.Car;
import interfaces.Person;

import java.util.concurrent.Semaphore;

import activityLog.ActivityLog;
import activityLog.ActivityTag;
import astar.AStarTraversal;

public class CarAgent extends Vehicle implements Car {
	//Data
	public CarEvent event = CarEvent.none;
	public CarState state = CarState.parked;
	
	public Person owner = null; //Car owner
	
	public String destination = null;
	
	public enum CarEvent { none, driving, arriving, parking };
	public enum CarState { parked, driving, arrived };
	
	String name = "Car";
	
	private boolean test = false;
	
	ActivityTag tag = ActivityTag.CAR;
	
	public CarAgent(AStarTraversal aStar) {
		super(aStar);
		
		capacity = 1;
		type = "car";
		guiFinished = new Semaphore(0, true);
	}
	
	//Messages
	public void msgDriveTo(Person p, String dest) {
		owner = p;
		event = CarEvent.driving;
		destination = dest;
		stateChanged();
	}
	
	public void msgParkCar(Person p) {
		event = CarEvent.parking;
		destination = null;
		stateChanged();
	}
	
	public void msgGuiFinished() {
		guiFinished.release();
	}

	//Scheduler
	public boolean pickAndExecuteAnAction() {
		if(state == CarState.parked && event == CarEvent.driving) {
			state = CarState.driving;
			driveToDestination();
			return true;
		}
		if(state == CarState.driving && event == CarEvent.arriving) {
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
	private void driveToDestination() {
		//gui.DoDriveTo(destination);
		
		log("Driving to " + destination);
		/*try {
			guiFinished.acquire();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
		
		event = CarEvent.arriving;
	}
	
	private void tellOwnerWeHaveArrived() {
		owner.msgArrived(this);
	}
	
	private void parkCar() {
		//gui.DoParkCar();
		
		log("Parking...");
		/*try {
			guiFinished.acquire();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
		
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
