package city;

import java.util.*;

import astar.Position;

public class CityMap {
	//Map of bus stop numbers to nearby destinations. Necessary?
	//Map<int, List<String>> busStops = new ArrayMap<int, List<String>>();

	//We may just keep a list of Restaurants, Banks, Markets, and BusStops here instead.
	//Depends how the gui ends up working...
	
	Map<String, Position> buildingLocations = new HashMap<String, Position>();
	
	CityMap() {
		buildingLocations.put("restaurant1", new Position(6,8));
		buildingLocations.put("restaurant2", new Position(38,6));
		buildingLocations.put("restaurant3", new Position(17,14));
		buildingLocations.put("restaurant4", new Position(29,23));
		buildingLocations.put("restaurant5", new Position(40,12));
		
		//Add in all locations
	}
	
	public int getX(String location) {
		return buildingLocations.get(location).getX();
	}
	
	public int getY(String location) {
		return buildingLocations.get(location).getY();
	}
}
