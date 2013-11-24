package interfaces;

import java.util.List;

import city.PersonAgent;

public interface Bus {
	
	public void msgPeopleBoarding(List<Person> people);
	
	public void msgHereIsFare(Person pa, double money);

	public void msgImGettingOff(Person pa);
	
	public void msgFinishedUnloading();
	
	public void msgGuiFinished();
}
