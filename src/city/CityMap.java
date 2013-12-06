package city;

import interfaces.BusStop;
import interfaces.Restaurant2Customer;

import java.util.*;

import justinetesting.interfaces.Customer4;
import restaurant1.Restaurant1;
import restaurant1.Restaurant1CustomerRole;
import tomtesting.interfaces.Restaurant5Customer;
import city.Restaurant2.Restaurant2;
import city.Restaurant2.Restaurant2CustomerRole;
import city.Restaurant3.Restaurant3;
import city.Restaurant4.Restaurant4;
import city.Restaurant5.Restaurant5;
import city.transportation.BusStopAgent;
import Role.BankManagerRole;
import Role.Role;
import astar.Position;

public class CityMap {
	//Map of bus stop numbers to nearby destinations. Necessary?
	Map<Integer, List<String>> nearbyDestinations = new HashMap<Integer, List<String>>();

	//References to 4 bus stops
	List<BusStopAgent> busStops = new ArrayList<BusStopAgent>();
	
	//We may just keep a list of Restaurants, Banks, Markets, and BusStops here instead.
	//Depends how the gui ends up working...
	
	Map<String, Position> buildingLocations = new HashMap<String, Position>();
	Map<String, Position> parkingLocations = new HashMap<String, Position>();
	List<String> restaurants = new ArrayList<String>();
	
	Restaurant1 restaurant1;
	Restaurant2 restaurant2;
	Restaurant3 restaurant3;
	Restaurant4 restaurant4;
	Restaurant5 restaurant5;
	Bank bank;
	Market market;
	
	public CityMap() {
		//Restaurant locations
		buildingLocations.put("rest1", new Position(20,0));
		buildingLocations.put("rest2", new Position(0,3));
		buildingLocations.put("rest3", new Position(0,17));
		buildingLocations.put("rest4", new Position(10,18));
		buildingLocations.put("rest5", new Position(21,18));
		//Market locations
		buildingLocations.put("mark1", new Position(24,10));
		buildingLocations.put("mark2", new Position(5,0));
		buildingLocations.put("mark3", new Position(5,18));
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
		buildingLocations.put("stop3", new Position(7,18));
		//House locations
		buildingLocations.put("house1", new Position(21,20));
		buildingLocations.put("house2", new Position(24,17));
		buildingLocations.put("house3", new Position(25,17));
		buildingLocations.put("house4", new Position(21,6));
		buildingLocations.put("house5", new Position(21,2));
		buildingLocations.put("house6", new Position(19,0));
		buildingLocations.put("house7", new Position(17,0));
		buildingLocations.put("house8", new Position(15,0));
		buildingLocations.put("house9", new Position(13,0));
		buildingLocations.put("house10", new Position(9,0));
		buildingLocations.put("house11", new Position(7,0));
		buildingLocations.put("house12", new Position(3,0));
		buildingLocations.put("house13", new Position(1,0));
		buildingLocations.put("house14", new Position(0,0));
		buildingLocations.put("house15", new Position(0,4));
		buildingLocations.put("house16", new Position(0,6));
		buildingLocations.put("house17", new Position(0,10));
		buildingLocations.put("house18", new Position(0,14));
		buildingLocations.put("house29", new Position(0,16));
		buildingLocations.put("house20", new Position(3,18));
		buildingLocations.put("house21", new Position(4,18));
		buildingLocations.put("house22", new Position(11,18));

		//Parking locations
		parkingLocations.put("rest1", new Position(18,4));
		parkingLocations.put("rest2", new Position(3,4));
		parkingLocations.put("rest3", new Position(3,14));
		parkingLocations.put("rest4", new Position(10,15));
		parkingLocations.put("rest5", new Position(15,9));
		//Market locations
		parkingLocations.put("mark1", new Position(24,12));
		parkingLocations.put("mark2", new Position(5,3));
		parkingLocations.put("mark3", new Position(8,12));
		
		//Adding list of nearby locations to each bus stop
		List<String> buildingList0 = new ArrayList<String>();
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
		buildingList3.add("rest5");
		buildingList3.add("mark3");
		buildingList3.add("house1");
		nearbyDestinations.put(3, buildingList3);
		
		//Creating list of restaurants
		restaurants.add("Restaurant1");
		restaurants.add("Restaurant2");
		restaurants.add("Restaurant3");
		restaurants.add("Restaurant4");
		restaurants.add("Restaurant5");
	}
	
	
	
	
	
	public void setRestaurant1(Restaurant1 r) {
		restaurant1 = r;
	}
	
	public void setRestaurant2(Restaurant2 r){
		restaurant2 = r;
	}
	
	public void setRestaurant3(Restaurant3 r){
		restaurant3 = r;
	}
	
	public void setRestaurant4(Restaurant4 r){
		restaurant4= r;
	}
	
	public void seRestaurant5(Restaurant5 r){
		restaurant5 =r;
	}
	
	public void setMarket(Market m){
		market= m;
	}
	
	public Position getParkingLocation(String location) {
		return parkingLocations.get(location);
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
	
	/*
	 * This function takes a restaurant number and messages the host of that restaurant that the customer is hungry
	 */
	public void msgHostHungryAtRestaurant(int num, Role customer){
		if(num == 1){
			Restaurant1CustomerRole cust = (Restaurant1CustomerRole) customer;
			cust.setHost(restaurant1.getHost());
			cust.getGui().setHungry();
			//restaurant1.getHost().msgImHungry((Restaurant1CustomerRole) customer);
		}
		if(num == 2){
			restaurant2.getHost().msgIWantFood((Restaurant2Customer) customer);
		}
		if(num == 4){
			restaurant4.getHost().msgIWantFood((Customer4) customer);
		}
		if(num == 5){
			restaurant5.getHost().msgIWantFood((Restaurant5Customer) customer);
		}
	}
	
}
