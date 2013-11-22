package city.transportation;

import interfaces.Bus;
import interfaces.BusStop;

import java.util.*;
import java.util.concurrent.Semaphore;

import city.PersonAgent;

public class BusAgent extends Vehicle implements Bus {
	//Data
	public int currentStop = 3;
	public List<BusStop> busStops = new ArrayList<BusStop>();
	double money;
	double fare;
	
	Timer timer = new Timer();
	
	public List<Passenger> passengers = new ArrayList<Passenger>();
	
	class Passenger {
		PersonAgent p;
		boolean wantsOff = false;
		boolean paidFare = false;

		Passenger(PersonAgent p) {
			this.p = p;
		}
	}
	
	public BusEvent event = BusEvent.arrivedAtStop;
	public BusState state = BusState.driving;
	
	public enum BusEvent { none, arrivedAtStop, pickingUpPassengers, boarded, everyonePaid };
	public enum BusState { driving, atStop, pickingUpPassengers, askingForFare };

	public BusAgent() {
		capacity = 10;
		fare = 3.00;
		money = 100.00;
		
		guiFinished = new Semaphore(0, true);
		
		//Send gui to stop 0
		//acquire semaphore
	}
	
	//Messages
	public void msgPeopleBoarding(List<PersonAgent> people) {
		if(people == null) {
			event = BusEvent.boarded;
			stateChanged();
			return;
		}
		
		for(PersonAgent p : people) {
			passengers.add(new Passenger(p));
		}
		event = BusEvent.boarded;
		stateChanged();
	}
	
	public void msgHereIsFare(PersonAgent pa, double money) {
		this.money += money;
		for(Passenger p : passengers) {
			if(p.p == pa) {
				p.paidFare = true;
				//stateChanged();
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
	
	public void msgFinishedUnloading() {
		event = BusEvent.pickingUpPassengers;
		stateChanged();
	}
	
	public void msgGuiFinished() {
		guiFinished.release();
	}
	
	//Scheduler
	public boolean pickAndExecuteAnAction() {
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
		print("Telling passengers we have arrived at stop #" + currentStop);
		for(Passenger p : passengers) {
			p.p.msgArrivedAtStop(currentStop);
		}
		
		DoWaitAtStop();
	}
	
	private void PickUpPassengers() {
		int numSpots = capacity - passengers.size();
		busStops.get(currentStop).msgICanPickUp(this, numSpots);
	}
	
	private void AskPassengersForFare() {
		if(allPassengersPaid()) {
			print("Everyone has paid!");
			event = BusEvent.everyonePaid;
			stateChanged();
		}
		
		for(Passenger p : passengers) {
			if(!p.paidFare) {
				print("Requesting fare from new passenger");
				p.p.msgPleasePayFare(this, fare);
			}
		}
	}
	
	private boolean allPassengersPaid() {
		for(Passenger p : passengers) {
			if(!p.paidFare)
				return false;
		}
		return true;
	}
	
	private void DriveToNextStop() {
		//gui.DoDriveToStop(currentStop + 1);
		
		print("Driving to stop #" + (currentStop + 1));
		/*try {
			guiFinished.acquire();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/

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
		/*timer.schedule(new TimerTask() {
			public void run() {
				 msgFinishedUnloading();
			}
		}, 1500	);*/
	}
}
 