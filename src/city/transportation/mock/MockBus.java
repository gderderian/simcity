package city.transportation.mock;

import java.util.List;

import city.PersonAgent;
import interfaces.Bus;
import interfaces.Person;

public class MockBus implements Bus {

	@Override
	public void msgPeopleBoarding(List<Person> people) {
		System.out.println("Received list of boarding passengers");
		
	}

	@Override
	public void msgHereIsFare(Person pa, double money) {
		System.out.println("Received fare from passenger");
		
	}

	@Override
	public void msgImGettingOff(Person pa) {
		System.out.println("Received message: person getting off");
		
	}

	@Override
	public void msgFinishedUnloading() {
		System.out.println("Finished unloading passengers");
		
	}

	@Override
	public void msgGuiFinished() {
		
	}

}
