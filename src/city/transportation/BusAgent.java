package city.transportation;

import interfaces.Bus;
import interfaces.BusStop;
import interfaces.Person;

import java.util.*;
import java.util.concurrent.Semaphore;

import city.CityMap;

import activityLog.ActivityLog;
import activityLog.ActivityTag;
import astar.AStarTraversal;
import astar.Position;

public class BusAgent extends Vehicle implements Bus {
	//Data
	public int currentStop;
	public List<BusStop> busStops = new ArrayList<BusStop>();
	private Map<Integer, Position> stopPositions = new HashMap<Integer, Position>();
	public double money;
	double fare;

	private boolean test = false;

	ActivityTag tag = ActivityTag.BUS;

	String name = "Bus";

	Timer timer = new Timer();

	public List<Passenger> passengers = Collections.synchronizedList(new ArrayList<Passenger>());

	public class Passenger {
		public Person p;
		boolean wantsOff = false;
		boolean paidFare = false;

		Passenger(Person p) {
			this.p = p;
		}
	}

	public BusEvent event = BusEvent.none;
	public BusState state = BusState.driving;

	public enum BusEvent { none, arrivedAtStop, pickingUpPassengers, boarded, everyonePaid };
	public enum BusState { driving, atStop, pickingUpPassengers, askingForFare };

	public BusAgent(AStarTraversal aStar, CityMap map) {
		super(aStar, map);

		currentStop = 3;
		capacity = 10;
		fare = 3.00;
		money = 100.00;
		type = "bus";

		guiFinished = new Semaphore(0, true);

		//Bus stop locations
		stopPositions.put(0, new Position(17, 8));
		stopPositions.put(1, new Position(10, 4));
		stopPositions.put(2, new Position(4, 9));
		stopPositions.put(3, new Position(8, 14));

		currentPosition = new Position(17, 20);
		currentPosition.moveInto(aStar.getGrid());
	}

	//Messages
	public void msgPeopleBoarding(List<Person> people) {
		synchronized(passengers) {
			if(people == null) {
				event = BusEvent.boarded;
				stateChanged();
				return;
			}

			for(Person p : people) {
				passengers.add(new Passenger(p));
			}
		}
		event = BusEvent.boarded;
		stateChanged();
	}

	public void msgHereIsFare(Person pa, double money) {
		this.money += money;
		for(Passenger p : passengers) {
			if(p.p == pa) {
				p.paidFare = true;
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

	public void msgImGettingOff(Person pa) {
		synchronized(passengers) {
			for(Passenger p : passengers) {
				if(p.p == pa) {
					p.wantsOff = true;
					stateChanged();
				}
			}
		}
	}

	public void msgFinishedUnloading() {
		event = BusEvent.pickingUpPassengers;
		stateChanged();
	}
	
	public void msgScenicRouteFinished() {
		event = BusEvent.arrivedAtStop;
		stateChanged();
	}

	public void msgGuiFinished() {
		guiFinished.release();
	}

	//Scheduler
	public boolean pickAndExecuteAnAction() {
		synchronized(passengers) {
			for(Passenger p : passengers) {
				if(p.wantsOff && p.paidFare) {
					LetPassengerOff(p);
					return true;
				}
			}
		}
		if(state == BusState.driving && event == BusEvent.none) {
			GoToFirstStop();
			return true;
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
		log("We have arrived at stop #" + currentStop);
		for(Passenger p : passengers) {
			p.p.msgArrivedAtStop(currentStop, currentPosition);
		}

		DoWaitAtStop();
	}

	private void GoToFirstStop() {
		guiMoveFromCurrentPositionTo(stopPositions.get(0));
		event = BusEvent.arrivedAtStop;
	}

	private void PickUpPassengers() {
		int numSpots = capacity - passengers.size();
		log("I can pick up " + numSpots + " people.");
		busStops.get(currentStop).msgICanPickUp(this, numSpots);
	}

	private void AskPassengersForFare() {
		if(allPassengersPaid()) {
			log("Everyone has paid!");
			event = BusEvent.everyonePaid;
			stateChanged();
		}

		for(Passenger p : passengers) {
			if(!p.paidFare) {
				log("Requesting fare from new passenger");
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

		log("Driving to stop #" + (currentStop + 1));
		
		if(currentStop == 3 && passengers.isEmpty()) {
			log("(Taking the scenic route)");
			scenicRoute();
			return;
		}
		
		GoToStop((currentStop + 1) % 4);

		event = BusEvent.arrivedAtStop;

	}

	private void LetPassengerOff(Passenger p) {
		Passenger temp = null;
		synchronized(passengers) {
			for(Passenger pass : passengers) {
				if(pass == p)
					temp = pass;
			}

			passengers.remove(temp);
		}
	}

	private void DoWaitAtStop() {
		timer.schedule(new TimerTask() {
			public void run() {
				msgFinishedUnloading();
			}
		}, 1000	);
	}

	public void addBusStops(List<BusStop> stops) {
		busStops = stops;
	}

	private void GoToStop(int stop) {
		if(aStar == null) {
			log("Moving to stop #" + stop);
			return;
		}

		switch(stop) { //Moves to a corner before going to next stop - makes paths as straight as possible
		case 0:
			guiMoveFromCurrentPositionTo(new Position(17, 14));
			break;
		case 1:
			guiMoveFromCurrentPositionTo(new Position(17, 4));
			break;
		case 2:
			guiMoveFromCurrentPositionTo(new Position(4, 4));
			break;
		case 3:
			guiMoveFromCurrentPositionTo(new Position(4, 14));
			break;
		}

		guiMoveFromCurrentPositionTo(stopPositions.get(stop));
	}
	
	private void scenicRoute() { //This method has the bus drive off of the screen and then back on at the bottom if it has no passengers
		guiMoveFromCurrentPositionTo(new Position(25, 14));
		gui.moveTo(25 * 30 + 150, 14 * 30 + 60);
		try {
			guiFinished.acquire();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		gui.setInvisible();

		timer.schedule(new TimerTask() {
			public void run() {
				finishScenicRoute();
			}
		}, 4000	);
	}
	
	private void finishScenicRoute() {
		int x = 17, y = 20; //Coordinates of spot at bottom of screen
		gui.teleport(x * 30 + 120, y * 30 + 90);
		
		currentPosition.release(aStar.getGrid());
		currentPosition = new Position(x, y);
		currentPosition.moveInto(aStar.getGrid());
		
		gui.setVisible();
		
		guiMoveFromCurrentPositionTo(stopPositions.get(0));
		
		msgScenicRouteFinished();
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
