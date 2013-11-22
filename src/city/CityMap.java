package city;

import java.util.*;

import city.Restaurant2.Restaurant2CustomerRole;
import city.Restaurant2.Restaurant2HostRole;
import Role.Role;
import astar.Position;

public class CityMap {
	//Map of bus stop numbers to nearby destinations. Necessary?
	Map<Integer, List<String>> busStops = new HashMap<Integer, List<String>>();

	//We may just keep a list of Restaurants, Banks, Markets, and BusStops here instead.
	//Depends how the gui ends up working...
	
	Map<String, Position> buildingLocations = new HashMap<String, Position>();
	List<String> restaurants = new ArrayList<String>();
	
	public CityMap() {
		buildingLocations.put("restaurant1", new Position(6,8));
		buildingLocations.put("restaurant2", new Position(38,6));
		buildingLocations.put("restaurant3", new Position(17,14));
		buildingLocations.put("restaurant4", new Position(29,23));
		buildingLocations.put("restaurant5", new Position(40,12));
		
		//Add in all locations
		
		//Creating list of restaurants
		restaurants.add("Restaurant2");
	}
	
	public int getX(String location) {
		return buildingLocations.get(location).getX();
	}
	
	public int getY(String location) {
		return buildingLocations.get(location).getY();
	}
	
	public int getClosestBusSTop(String destination) { //Returns number of closest bus stop to destination, or a -1 if destination is not found
		for(int i = 0; i < 3; i++) {
			if(busStops.get(i).contains(destination))
				return i;
		}
		
		return -1;
	}
	
	
	//Classes which act as a "directory"
	
	/*
	 * host role: (the role the customer messages when they get to the restaurant
	 * customer role: the role the person takes on when he/she gets to the restaurant
	 * Person must create a *new* instance of the customer role to enter the restaurant
	 * Host Role can be addressed as is: there will only ever be one host at a time
	 */
	class Restaurant2{		//HACKEY TODO: fix this maybe?
		Restaurant2HostRole host;
		Restaurant2CustomerRole customer;
		String name;
		
		public Restaurant2(){
			host = new Restaurant2HostRole("Sarah");
			name = "Restaurant2";
		}
		public Role getHost(){
			return host;
		}
		public Role getNewCustomerRole(){
			customer = new Restaurant2CustomerRole();
			return customer;
		}
		public String getName(){
			return name;
		}
	}
}
