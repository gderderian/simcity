package city;

import java.util.*;

public class BusAgent extends Vehicle {
	//Data
	int currentStop;
	List<BusStopAgent> busStops = new ArrayList<BusStopAgent>();
	
	double money;
	
	List<Passenger> passengers;
	
	class Passenger {
		PersonAgent p;
		boolean wantsOff = false;
		boolean paidFare = false;

		Passenger(PersonAgent p) {
			this.p = p;
		}
	}
	
	BusEvent event = BusEvent.arrivedAtStop;
	BusState state = BusState.driving;
	
	enum BusEvent { arrivedAtStop, pickingUpPassengers, boarded, everyonePaid };
	enum BusState { driving, atStop, pickingUpPassengers, askingForFare };

	//Messages
	public void msgPeopleBoarding(List<PersonAgent> people) {
		for(PersonAgent p : people) {
			passengers.add(new Passenger(p));
		}
		event = BusEvent.boarded;
		stateChanged();
	}
	
	//Okay to potentially have two stateChanged() calls? - Check this method.
	public void msgHereIsFare(PersonAgent pa, double money) {
		this.money += money;
		for(Passenger p : passengers) {
			if(p.p == pa) {
				p.paidFare = true;
				stateChanged();
			}
		}
		
		for(Passenger p : passengers) {
			if(p.paidFare == false) {
				return;
			}
		}
		event = BusEvent.everyonePaid;
		stateChanged();
	}
	
	public void msgImGettingOff(PersonAgent pa) {
		for(Passenger p : passengers) {
			if(p.p == pa) {
				p.wantsOff = true;
				stateChanged();
			}
		}
	}
	
	protected boolean pickAndExecuteAnAction() {
		for(Passenger p : passengers) {
			if(p.wantsOff && p.paidFare) {
				LetPassengerOff(p);
				return true;
			}
		}
		
		if(state == BusState.driving && event == BusEvent.arrivedAtStop) {
			state = BusState.atStop;
			currentStop = (currentStop + 1) % 4; //Using 4 stops
			TellPassengersWeAreAtStop();
			return true;
		}
		
		if(state == BusState.atStop && event == BusEvent.pickingUpPassengers) {
			state = BusState.pickingUpPassengers;
			PickUpPassengers();
			return true;
		}
		
		if(state == BusState.pickingUpPassengers && event == BusEvent.boarded) {
			state = BusState.askingForFare;
			AskPassengersForFare();
			return true;
		}
		
		if(state == BusState.askingForFare && event == BusEvent.everyonePaid) {
			state = BusState.driving;
			DriveToNextStop();
			return true;
		}
		
		return false;
	}
>>>>>>> ee9d61ec80fab66107df9c4a1850630078fe2ced

	//Actions
	private void TellPassengersWeAreAtStop() {
		
	}
	
	private void PickUpPassengers() {
		
	}
	
	private void AskPassengersForFare() {
		
	}
	
	private void DriveToNextStop() {
		
	}
	
	private void LetPassengerOff(Passenger p) {
		
	}
}
