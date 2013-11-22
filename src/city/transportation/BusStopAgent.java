package city.transportation;

import java.util.*;

import city.PersonAgent;
import city.transportation.interfaces.BusStop;

import agent.Agent;

public class BusStopAgent extends Agent implements BusStop {
	//Data
	public List<PersonAgent> peopleWaiting = new ArrayList<PersonAgent>();
	
	public List<MyBus> buses = new ArrayList<MyBus>();
	
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
	public boolean pickAndExecuteAnAction() {
		if(!buses.isEmpty()) {
			sendPassengersToBus(buses.get(0));
			return true;
		}

		return false;
	}
	
	//Actions
	private void sendPassengersToBus(MyBus b) {
		if(peopleWaiting.isEmpty()) {
			print("Sorry, no passengers waiting for bus!");
			b.b.msgPeopleBoarding(null);
			buses.remove(b);
			return;
		}
		
		List<PersonAgent> newPassengers = new ArrayList<PersonAgent>();
		int i = 0;
		while(i < b.openSpots && !peopleWaiting.isEmpty()) {
			PersonAgent temp = peopleWaiting.get(0);
			newPassengers.add(temp);
			temp.msgBusIsHere(b.b); //Need reference to bus or not? Bus will message person anyways.
			peopleWaiting.remove(temp);
			i++;
		}
		
		print("Here are " + newPassengers.size() + " passengers!");
		b.b.msgPeopleBoarding(newPassengers);
		buses.remove(b);
	}

}
