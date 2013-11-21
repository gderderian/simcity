package city.transportation;

import city.PersonAgent;
import city.transportation.BusAgent.BusEvent;

public class CarAgent extends Vehicle {
	//Data
	public CarEvent event = CarEvent.none;
	public CarState state = CarState.parked;
	
	PersonAgent owner; //Car owner
	
	String destination;
	
	enum CarEvent { none, driving, arriving, parking };
	enum CarState { parked, driving, arrived };
	
	public CarAgent() {
		capacity = 1;
	}
	
	//Messages
	public void msgDriveTo(PersonAgent p, String dest) {
		event = CarEvent.driving;
		destination = dest;
		stateChanged();
	}
	
	public void msgParkCar(PersonAgent p) {
		event = CarEvent.parking;
		destination = null;
		stateChanged();
	}

	//Scheduler
	protected boolean pickAndExecuteAnAction() {
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
		
		try {
			guiFinished.acquire();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		event = CarEvent.arriving;
	}
	
	private void tellOwnerWeHaveArrived() {
		//Uncomment when method is implemented within person
		owner.msgArrived();
	}
	
	private void parkCar() {
		//gui.DoParkCar();
		
		try {
			guiFinished.acquire();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		event = CarEvent.none;
	}
}
