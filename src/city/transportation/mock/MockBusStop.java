package city.transportation.mock;

import interfaces.Bus;
import interfaces.BusStop;
import interfaces.Person;
import city.PersonAgent;
import city.transportation.BusAgent;

public class MockBusStop implements BusStop {

	public void msgWaitingForBus(Person p) {
		System.out.println("Bus stop: Received message: Person waiting for bus.");
		
	}

	public void msgICanPickUp(Bus b, int people) {
		System.out.println("Bus stop: Received message: Bus can pick up " + people + " people.");
	}

}
