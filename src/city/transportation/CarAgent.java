package city.transportation;

import city.PersonAgent;

public class CarAgent extends Vehicle {
	//Data
	CarEvent event = CarEvent.none;
	CarState state = CarState.parked;
	
	PersonAgent p; //Car owner
	
	String destination;
	
	enum CarEvent { none, driving, arriving, parking };
	enum CarState { parked, driving, arrived };
	
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
		
	}
	
	private void tellOwnerWeHaveArrived() {
		
	}
	
	private void parkCar() {
		
	}
}
