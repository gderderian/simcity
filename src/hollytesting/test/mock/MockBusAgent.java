package hollytesting.test.mock;

import java.util.List;

import test.mock.EventLog;
import test.mock.LoggedEvent;
import test.mock.Mock;
import city.PersonAgent;
import interfaces.Bus;
import interfaces.Person;

public class MockBusAgent extends Mock implements Bus {
        
        public EventLog log = new EventLog();

        public MockBusAgent(String name) {
                super(name);
        }

        @Override
        public void msgFinishedUnloading() {
                //dont need this
        }

        @Override
        public void msgGuiFinished() {
                //dont need this
        }

		@Override
		public void msgHereIsFare(Person pa, double money) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void msgImGettingOff(Person pa) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void msgPeopleBoarding(List<Person> people) {
			// TODO Auto-generated method stub
			
		}

}