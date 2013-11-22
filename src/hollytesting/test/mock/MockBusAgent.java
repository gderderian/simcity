package hollytesting.test.mock;

import java.util.List;

import test.mock.EventLog;
import test.mock.LoggedEvent;
import city.PersonAgent;
import hollytesting.interfaces.Bus;

public class MockBusAgent extends Mock implements Bus {
	
	public EventLog log = new EventLog();

	public MockBusAgent(String name) {
		super(name);
	}

	@Override
	public void msgPeopleBoarding(List<PersonAgent> people) {
		log.add(new LoggedEvent("Recieved list of people boarding the bus."));
	}

	@Override
	public void msgHereIsFare(PersonAgent pa, double money) {
		log.add(new LoggedEvent("Recieved fare of " + money + " from person " + pa.getName()));
	}

	@Override
	public void msgImGettingOff(PersonAgent pa) {
		log.add(new LoggedEvent("Person " + pa.getName() + " is getting off the bus."));
	}

	@Override
	public void msgFinishedUnloading() {
		//dont need this
	}

	@Override
	public void msgGuiFinished() {
		//dont need this
	}

}
