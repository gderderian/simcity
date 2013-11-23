package city;

import interfaces.BusStop;

import java.util.*;

import city.Restaurant2.Restaurant2CustomerRole;
import city.Restaurant2.Restaurant2HostRole;
import city.transportation.BusStopAgent;
import Role.BankManagerRole;
import Role.Role;
import astar.Position;

public class CityMap {
	//Map of bus stop numbers to nearby destinations. Necessary?
	Map<Integer, List<String>> nearbyDestinations = new HashMap<Integer, List<String>>();

	//References to 4 bus stops
	List<BusStopAgent> busStops = new ArrayList<BusStopAgent>();
	List<Bank> banks = new ArrayList<Bank>();
	
	//We may just keep a list of Restaurants, Banks, Markets, and BusStops here instead.
	//Depends how the gui ends up working...
	
	Map<String, Position> buildingLocations = new HashMap<String, Position>();
	List<String> restaurants = new ArrayList<String>();
	
	Restaurant2 restaurant2 = new Restaurant2();
	
	public CityMap() {
		//Restaurant locations
		buildingLocations.put("rest1", new Position(20,0));
		buildingLocations.put("rest2", new Position(0,3));
		buildingLocations.put("rest3", new Position(0,17));
		buildingLocations.put("rest4", new Position(18,10));
		buildingLocations.put("rest5", new Position(13,9));
		//Market locations
		buildingLocations.put("mark1", new Position(21,11));
		buildingLocations.put("mark2", new Position(5,0));
		buildingLocations.put("mark3", new Position(9,9));
		//Bank locations
		buildingLocations.put("bank1", new Position(21,1));
		buildingLocations.put("bank2", new Position(0,12));
		//Apartment locations
		buildingLocations.put("apart1", new Position(21,4));
		buildingLocations.put("apart2", new Position(1,18));
		//Bus Stop locations
		buildingLocations.put("stop0", new Position(21,8));
		buildingLocations.put("stop1", new Position(11,0));
		buildingLocations.put("stop2", new Position(0,8));
		buildingLocations.put("stop3", new Position(18,7));
		//House locations
		//...
		
		//Creating list of restaurants
		restaurants.add("Restaurant2");
	}
	
	public int getX(String location) {
		return buildingLocations.get(location).getX();
	}
	
	public int getY(String location) {
		return buildingLocations.get(location).getY();
	}
	
	public int getClosestBusStop(String destination) { //Returns number of bus stop closest to destination. Returns -1 if destination is not found
		for(int i = 0; i < 3; i++) {
			if(nearbyDestinations.get(i).contains(destination)) {
				return i;
			}
		}
		return -1;
	}
	
	public void addBusStop(BusStop busStop) {
		busStops.add((BusStopAgent) busStop);
	}
	
	public void addStopDestinations(int number, List<String> destinations) {
		nearbyDestinations.put(number, destinations);
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
		public Restaurant2CustomerRole getNewCustomerRole(){
			customer = new Restaurant2CustomerRole();
			return customer;
		}
		public String getName(){
			return name;
		}
		
	}
	
	class Bank{
		BankManagerRole manager;
		String name;
		
		public Bank(BankManagerRole m, String n){
			manager = m;
			name = n;
		}
	}
	
	public String getClosestBank(){
		return banks.get(0).name;
	}
	
}
