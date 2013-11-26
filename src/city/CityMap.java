package city;

import interfaces.BusStop;

import java.util.*;

import city.Restaurant2.Restaurant2;
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
	//List<Bank> banks = new ArrayList<Bank>();
	
	//We may just keep a list of Restaurants, Banks, Markets, and BusStops here instead.
	//Depends how the gui ends up working...
	
	Map<String, Position> buildingLocations = new HashMap<String, Position>();
	List<String> restaurants = new ArrayList<String>();
	
	Restaurant2 restaurant2;
	Bank bank;
	
	public CityMap() {
		//Restaurant locations
		buildingLocations.put("rest1", new Position(20,0));
		buildingLocations.put("rest2", new Position(0,3));
		buildingLocations.put("rest3", new Position(0,17));
		buildingLocations.put("rest4", new Position(10,18));
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
		buildingLocations.put("house1", new Position(21,17));
		buildingLocations.put("house2", new Position(21,15));
		buildingLocations.put("house3", new Position(21,13));
		buildingLocations.put("house4", new Position(21,10));
		buildingLocations.put("house5", new Position(21,6));
		buildingLocations.put("house6", new Position(21,2));
		buildingLocations.put("house7", new Position(19,0));
		buildingLocations.put("house8", new Position(17,0));
		buildingLocations.put("house9", new Position(15,0));
		buildingLocations.put("house10", new Position(13,0));
		buildingLocations.put("house11", new Position(9,0));
		buildingLocations.put("house12", new Position(7,0));
		buildingLocations.put("house13", new Position(3,0));
		buildingLocations.put("house14", new Position(1,0));
		buildingLocations.put("house15", new Position(0,0));
		buildingLocations.put("house16", new Position(0,4));
		buildingLocations.put("house17", new Position(0,6));
		buildingLocations.put("house18", new Position(0,10));
		buildingLocations.put("house19", new Position(0,14));
		buildingLocations.put("house20", new Position(0,16));
		buildingLocations.put("house21", new Position(3,18));
		buildingLocations.put("house22", new Position(5,18));
		buildingLocations.put("house23", new Position(11,18));
		buildingLocations.put("house24", new Position(12,9));
		buildingLocations.put("house25", new Position(10,9));
		buildingLocations.put("house26", new Position(10,7));
		
		//Adding list of nearby locations to each bus stop
		List<String> buildingList0 = new ArrayList<String>();
		buildingList0.add("house1");
		buildingList0.add("house2");
		buildingList0.add("house3");
		buildingList0.add("mark1");
		buildingList0.add("house4");
		buildingList0.add("house5");
		buildingList0.add("apart1");
		buildingList0.add("house6");
		buildingList0.add("bank1");
		nearbyDestinations.put(0, buildingList0);
		
		List<String> buildingList1 = new ArrayList<String>();
		buildingList1.add("rest1");
		buildingList1.add("house7");
		buildingList1.add("house8");
		buildingList1.add("house9");
		buildingList1.add("house10");
		buildingList1.add("house11");
		buildingList1.add("house12");
		buildingList1.add("mark2");
		buildingList1.add("house13");
		buildingList1.add("house14");
		nearbyDestinations.put(1, buildingList1);
		
		List<String> buildingList2 = new ArrayList<String>();
		buildingList2.add("house15");
		buildingList2.add("rest2");
		buildingList2.add("house16");
		buildingList2.add("house17");
		buildingList2.add("house18");
		buildingList2.add("house19");
		buildingList2.add("house20");
		buildingList2.add("bank2");
		buildingList2.add("rest3");
		nearbyDestinations.put(2, buildingList2);
		
		List<String> buildingList3 = new ArrayList<String>();
		buildingList3.add("apart2");
		buildingList3.add("house21");
		buildingList3.add("house22");
		buildingList3.add("rest4");
		buildingList3.add("house23");
		buildingList3.add("rest5");
		buildingList3.add("house24");
		buildingList3.add("house25");
		buildingList3.add("mark3");
		buildingList3.add("house26");
		nearbyDestinations.put(3, buildingList3);
		
		//Creating list of restaurants
		restaurants.add("Restaurant2");
	}
	
	public void setRestaurant2(Restaurant2 r){
		restaurant2 = r;
	}
	
	public int getX(String location) {
		return buildingLocations.get(location).getX();
	}
	
	public int getY(String location) {
		return buildingLocations.get(location).getY();
	}
	
	public int getClosestBusStop(String destination) { //Returns number of bus stop closest to destination. Returns -1 if destination is not found
		for(int i = 0; i < 4; i++) {
			if(nearbyDestinations.get(i).contains(destination)) {
				return i;
			}
		}
		return -1;
	}
	
	public int getClosestBusStop(Position p) {
		int minStop = 0;
		double distance = 1000;
		for(int i = 0; i < 4; i++) {
			double newDistance = buildingLocations.get("stop" + Integer.toString(i)).distance(p);
			if(newDistance < distance) {
				distance = newDistance;
				minStop = i;
			}
		}
		return minStop;
	}
	
	public void addBusStop(BusStop busStop) {
		busStops.add((BusStopAgent) busStop);
	}
	
	public BusStopAgent getBusStop(int num) {
		return busStops.get(num);
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
	/*
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
	*/
	
	class Bank{
		BankManagerRole manager;
		String name;
		
		public Bank(BankManagerRole m, String n){
			manager = m;
			name = n;
		}
		
		public BankManagerRole getBankManager() {
			return manager;
		}
	}
	
	/*public String getClosestBank(){
		return banks.get(0).name;
	}*/
	
	
	
	public String getClosestPlaceFromHere(String here, String type){
		int housex = 0;
		int housey = 0;
		int posx = 0;
		int posy = 0;
		double shortestDistance = 0;
		double currentDistance = 0;
		String closestPlace = null;
		String currentPlace = null;
		for (Map.Entry<String, Position> entry : buildingLocations.entrySet()){
			if(entry.getKey().equals(here)){
				housex = entry.getValue().getX();
				housey = entry.getValue().getY();
			}
		}
		for (Map.Entry<String, Position> entry : buildingLocations.entrySet()){
			if(entry.getKey().contains(type)){
				posx = entry.getValue().getX();
				posy = entry.getValue().getY();
				
				currentPlace = entry.getKey();
				
				currentDistance = Math.sqrt(Math.pow(posx - housex,2) +
						Math.pow(posy - housey,2));
				
				if(currentDistance < shortestDistance){
					shortestDistance = currentDistance;
					closestPlace = currentPlace;
				}
			}
		}
		return closestPlace;
	}
	
}
