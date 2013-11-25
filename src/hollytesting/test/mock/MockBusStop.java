package hollytesting.test.mock;

import java.util.ArrayList;
import java.util.List;

import test.mock.EventLog;
import test.mock.LoggedEvent;
import test.mock.Mock;
import city.PersonAgent;
import city.transportation.BusAgent;
import interfaces.Bus;
import interfaces.BusStop;
import interfaces.Person;

public class MockBusStop extends Mock implements BusStop{
        
        public EventLog log = new EventLog();
        PersonAgent person;
        MockBusAgent bus;
        List<Person> people = new ArrayList<Person>();

        public MockBusStop(String name, PersonAgent p, MockBusAgent b) {
                super(name);
                person = p;
                bus = b;
                people.add(person);
        }

        @Override
        public void msgWaitingForBus(Person p) {
                log.add(new LoggedEvent("Recieved message waiting for bus from person "));
                person.msgBusIsHere(bus);
                bus.msgPeopleBoarding(people);
        }

		@Override
		public void msgICanPickUp(Bus b, int people) {
			// TODO Auto-generated method stub
			
		}

}