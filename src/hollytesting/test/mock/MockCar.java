package hollytesting.test.mock;

import test.mock.EventLog;
import test.mock.LoggedEvent;
import city.PersonAgent;
import hollytesting.interfaces.Car;

public class MockCar extends Mock implements Car{
	
	EventLog log = new EventLog();
	PersonAgent person;

	public MockCar(String name) {
		super(name);
	}

	@Override
	public void msgDriveTo(PersonAgent p, String dest) {
		log.add(new LoggedEvent("Recieved message drive to " + dest));
		person = p;
		p.msgArrived(this);
	}

	@Override
	public void msgParkCar(PersonAgent p) {
		log.add(new LoggedEvent("Recieved message park car"));
	}

}
