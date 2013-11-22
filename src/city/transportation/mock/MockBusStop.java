package city.transportation.mock;

import interfaces.BusStop;
import city.PersonAgent;
import city.transportation.BusAgent;

public class MockBusStop implements BusStop {

	@Override
	public void msgWaitingForBus(PersonAgent p) {
		System.out.println("Bus stop: Received message: waiting for bus.");
		
	}

	@Override
	public void msgICanPickUp(BusAgent b, int people) {
		System.out.println("Bus stop: Received message: Bus can pick up " + people + " people.");
		
	}

}
