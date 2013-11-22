package hollytesting.test.mock;

import java.util.ArrayList;
import java.util.List;

import test.mock.EventLog;
import test.mock.LoggedEvent;
import test.mock.Mock;
import city.PersonAgent;
import city.transportation.BusAgent;
import interfaces.BusStop;

public class MockBusStop extends Mock implements BusStop{
        
        public EventLog log = new EventLog();
        PersonAgent person;
        MockBusAgent bus;
        List<PersonAgent> people = new ArrayList<PersonAgent>();

        public MockBusStop(String name, PersonAgent p, MockBusAgent b) {
                super(name);
                person = p;
                bus = b;
                people.add(person);
        }

        @Override
        public void msgWaitingForBus(PersonAgent p) {
                log.add(new LoggedEvent("Recieved message waiting for bus from person " + p.getName()));
                person.msgBusIsHere(bus);
                bus.msgPeopleBoarding(people);
        }

		@Override
		public void msgICanPickUp(BusAgent b, int people) {
			// TODO Auto-generated method stub
			
		}

}