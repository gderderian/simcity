package city.transportation.mock;

import java.util.List;

import test.mock.EventLog;
import test.mock.LoggedEvent;

import city.PersonAgent;
import interfaces.Bus;
import interfaces.Person;

public class MockBus implements Bus {
	
	public EventLog log;
	
	public MockBus() {
		log = new EventLog();
	}

	@Override
	public void msgPeopleBoarding(List<Person> people) {
		if(people == null)
			log.add(new LoggedEvent("No one boarding from this bus stop"));
		else
			log.add(new LoggedEvent("Received list of boarding passengers from bus stop"));
		
	}

	@Override
	public void msgHereIsFare(Person pa, double money) {
		log.add(new LoggedEvent("Received fare from passenger"));
		
	}

	@Override
	public void msgImGettingOff(Person pa) {
		log.add(new LoggedEvent("Received message: person getting off"));
		
	}

	@Override
	public void msgFinishedUnloading() {
		log.add(new LoggedEvent("Finished unloading all passengers"));
		
	}

	@Override
	public void msgGuiFinished() {
		
	}

}
