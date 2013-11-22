package hollytesting.test.mock;

import city.PersonAgent;
import hollytesting.interfaces.Landlord;

public class MockLandlord extends Mock implements Landlord{

	public MockLandlord(String name) {
		super(name);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void msgHereIsMyRent(PersonAgent p, double amount) {
		
	}

	@Override
	public void msgFixAppliance(PersonAgent p, String a) {
		// TODO Auto-generated method stub
		
	}
	
}
