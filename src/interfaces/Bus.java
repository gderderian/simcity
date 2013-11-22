package interfaces;

import java.util.List;

import city.PersonAgent;

public interface Bus {
	
	public void msgPeopleBoarding(List<PersonAgent> people);
	
	public void msgHereIsFare(PersonAgent pa, double money);

	public void msgImGettingOff(PersonAgent pa);
	
	public void msgFinishedUnloading();
	
	public void msgGuiFinished();
}
