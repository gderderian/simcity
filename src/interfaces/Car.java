package interfaces;

import city.PersonAgent;

public interface Car {
	
	public void msgDriveTo(PersonAgent p, String dest);
	
	public void msgParkCar(PersonAgent p);

}
