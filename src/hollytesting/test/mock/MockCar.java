package hollytesting.test.mock;

import astar.Position;
import test.mock.EventLog;
import test.mock.LoggedEvent;
import test.mock.Mock;
import city.PersonAgent;
import interfaces.Car;
import interfaces.Person;

public class MockCar extends Mock implements Car{
        
        EventLog log = new EventLog();
        PersonAgent person;

        public MockCar(String name) {
                super(name);
        }

        @Override
        public void msgDriveTo(Person p, String dest) {
                log.add(new LoggedEvent("Recieved message drive to " + dest));
                p.msgArrived(this);
        }

        @Override
        public void msgParkCar(Person p) {
                log.add(new LoggedEvent("Recieved message park car"));
        }

		@Override
		public void msgPickMeUp(Person p, Position pos) {
			log.add(new LoggedEvent("Received message pick me up"));
			person = (PersonAgent) p;
			
		}

}