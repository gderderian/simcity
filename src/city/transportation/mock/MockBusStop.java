package city.transportation.mock;

import test.mock.EventLog;
import test.mock.LoggedEvent;
import interfaces.Bus;
import interfaces.BusStop;
import interfaces.Person;
import city.PersonAgent;
import city.transportation.BusAgent;

public class MockBusStop implements BusStop {
	
	public EventLog log;
	
	public int number;
	
	public MockBusStop(int i) {
		number = i;
		log = new EventLog();
	}

	public void msgWaitingForBus(Person p) {
		log.add(new LoggedEvent("Received message: Person waiting for bus"));
		
	}

	public void msgICanPickUp(Bus b, int people) {
		log.add(new LoggedEvent("Received message: Bus can pick up more people"));
	}

}
