package hollytesting.test.mock;

import test.mock.Mock;
import city.PersonAgent;
import interfaces.Landlord;
import interfaces.Person;

public class MockLandlord extends Mock implements Landlord{

        public MockLandlord(String name) {
                super(name);
                // TODO Auto-generated constructor stub
        }

        @Override
        public void msgHereIsMyRent(Person p, double amount) {
                
        }

        @Override
        public void msgFixAppliance(Person p, String a) {
                // TODO Auto-generated method stub
                
        }

		@Override
		public void msgEndOfDay() {
			// TODO Auto-generated method stub
			
		}
        
}