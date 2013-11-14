package city;

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
	}
	
	public void msgParkCar(PersonAgent p) {
		event = CarEvent.parking;
		destination = null;
	}

}
