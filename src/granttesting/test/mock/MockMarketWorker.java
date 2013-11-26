package granttesting.test.mock;

import java.util.List;
import test.mock.EventLog;
import test.mock.LoggedEvent;
import test.mock.Mock;
import city.PersonAgent;
import interfaces.Cook;

public class MockMarketWorker extends Mock implements Cook {

	public MockMarketWorker(String name) {
		super(name);
	}
	
}