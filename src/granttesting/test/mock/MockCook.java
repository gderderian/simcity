package granttesting.test.mock;

import java.util.List;
import test.mock.EventLog;
import test.mock.LoggedEvent;
import test.mock.Mock;
import city.PersonAgent;
import interfaces.Cook;

public class MockCook extends Mock implements Cook {

	public EventLog log;
	
	public MockCook(String name) {
		super(name);
		log = new EventLog();
	}
	
}