package interfaces;

import astar.Position;
import city.PersonAgent;

public interface Car {
	
	public void msgPickMeUp(Person p, Position pos);
	
	public void msgDriveTo(Person p, String dest);
	
	public void msgParkCar(Person p);

}
