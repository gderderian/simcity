package city.transportation;

import java.util.*;
import java.util.concurrent.Semaphore;

import city.PersonAgent;

public class BusAgent extends Vehicle {
	//Data
	int currentStop;
	List<BusStopAgent> busStops = new ArrayList<BusStopAgent>();
	double money;
	double fare;
	
	Timer timer = new Timer();
	
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

	public BusAgent() {
		capacity = 10;
		fare = 3.00;
		money = 100.00;
		
		guiFinished = new Semaphore(0, true);
	}
	
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
	
	private void msgFinishedUnloading() {
		event = BusEvent.pickingUpPassengers;
		stateChanged();
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

	//Actions
	private void TellPassengersWeAreAtStop() {
		for(Passenger p : passengers) {
			//uncomment when message is implemented within person
			//p.p.msgArrivedAtStop(currentStop);
		}
		
		DoWaitAtStop();
	}
	
	private void PickUpPassengers() {
		int numSpots = capacity - passengers.size();
		busStops.get(currentStop).msgICanPickUp(this, numSpots);
	}
	
	private void AskPassengersForFare() {
		for(Passenger p : passengers) {
			if(!p.paidFare) {
				//uncomment when message is implemented within person
				//p.p.msgPleasePayFare(this, fare);
			}
		}
	}
	
	private void DriveToNextStop() {
		//gui.DoDriveToStop(currentStop + 1);
		
		try {
			guiFinished.acquire();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		event = BusEvent.arrivedAtStop;
		
	}
	
	private void LetPassengerOff(Passenger p) {
		Passenger temp = null;
		for(Passenger pass : passengers) {
			if(pass == p)
				temp = pass;
		}
		
		passengers.remove(temp);
	}
	
	private void DoWaitAtStop() {
		timer.schedule(new TimerTask() {
			public void run() {
				 msgFinishedUnloading();
			}
		}, 1500	);
	}
}
 