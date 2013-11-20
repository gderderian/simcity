package city.transportation;

import java.util.*;

import city.PersonAgent;

import agent.Agent;

public class BusStopAgent extends Agent {
	//Data
	List<PersonAgent> peopleWaiting = new ArrayList<PersonAgent>();
	
	List<MyBus> buses = new ArrayList<MyBus>();
	
	class MyBus {
		BusAgent b;
		int openSpots;
		
		MyBus(BusAgent b, int spots) {
			this.b = b;
			this.openSpots = spots;
		}
	}
	
	//Messages
	public void msgWaitingForBus(PersonAgent p) {
		peopleWaiting.add(p);
	}
	
	public void msgICanPickUp(BusAgent b, int people) {
		buses.add(new MyBus(b, people));
	}
	
	//Scheduler
	protected boolean pickAndExecuteAnAction() {
		if(!buses.isEmpty()) {
			sendPassengersToBus(buses.get(0));
			return true;
		}

		return false;
	}
	
	//Actions
	private void sendPassengersToBus(MyBus b) {
		List<PersonAgent> newPassengers = new ArrayList<PersonAgent>();
		int i = 0;
		while(i < b.openSpots && !peopleWaiting.isEmpty()) {
			PersonAgent temp = peopleWaiting.get(0);
			newPassengers.add(temp);
			//temp.msgBusIsHere(); //Need reference to bus or not? Bus will message person anyways.
			peopleWaiting.remove(temp);
			i++;
		}
		
		b.b.msgPeopleBoarding(newPassengers);
		buses.remove(b);
	}

}
