package city.transportation;

import interfaces.Car;

import java.util.concurrent.Semaphore;

import city.PersonAgent;
import city.transportation.BusAgent.BusEvent;

public class CarAgent extends Vehicle implements Car {
	//Data
	public CarEvent event = CarEvent.none;
	public CarState state = CarState.parked;
	
	public PersonAgent owner = null; //Car owner
	
	public String destination = null;
	
	public enum CarEvent { none, driving, arriving, parking };
	public enum CarState { parked, driving, arrived };
	
	public CarAgent() {
		super();
		
		capacity = 1;
		guiFinished = new Semaphore(0, true);
	}
	
	//Messages
	public void msgDriveTo(PersonAgent p, String dest) {
		owner = p;
		event = CarEvent.driving;
		destination = dest;
		stateChanged();
	}
	
	public void msgParkCar(PersonAgent p) {
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
		
		print("Driving to " + destination);
		/*try {
			guiFinished.acquire();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
		
		event = CarEvent.arriving;
	}
	
	private void tellOwnerWeHaveArrived() {
		//Uncomment when method is implemented within person
		owner.msgArrived(this);
	}
	
	private void parkCar() {
		//gui.DoParkCar();
		
		print("parking");
		/*try {
			guiFinished.acquire();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
		
		event = CarEvent.none;
	}
}
