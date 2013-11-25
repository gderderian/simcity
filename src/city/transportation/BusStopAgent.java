package city.transportation;

import interfaces.Bus;
import interfaces.BusStop;
import interfaces.Person;

import java.util.*;

import city.PersonAgent;
import activityLog.ActivityLog;
import activityLog.ActivityTag;
import agent.Agent;

public class BusStopAgent extends Agent implements BusStop {
	//Data
	public List<Person> peopleWaiting = new ArrayList<Person>();
	
	public List<MyBus> buses = new ArrayList<MyBus>();
	
	int number;
	
	String name = "BusStop" + number;
	
	ActivityTag tag = ActivityTag.BUSSTOP;
	
	class MyBus {
		Bus b;
		int openSpots;
		
		MyBus(Bus b, int spots) {
			this.b = b;
			this.openSpots = spots;
		}
	}
	
	public BusStopAgent(int number) {
		this.number = number;
	}
	
	//Messages
	public void msgWaitingForBus(Person p) {
		peopleWaiting.add(p);
		stateChanged();
	}
	
	public void msgICanPickUp(Bus b, int people) {
		buses.add(new MyBus(b, people));
		stateChanged();
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
		
		List<Person> newPassengers = new ArrayList<Person>();
		int i = 0;
		while(i < b.openSpots && !peopleWaiting.isEmpty()) {
			Person temp = peopleWaiting.get(0);
			newPassengers.add(temp);
			temp.msgBusIsHere(b.b); //Need reference to bus or not? Bus will message person anyways.
			peopleWaiting.remove(temp);
			i++;
		}
		
		print("Here are " + newPassengers.size() + " passengers!");
		b.b.msgPeopleBoarding(newPassengers);
		buses.remove(b);
	}
	
	private void log(String msg){
		print(msg);
        ActivityLog.getInstance().logActivity(tag, msg, name);
	}

}
