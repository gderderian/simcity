package city;

import interfaces.Restaurant2Customer;
import interfaces.Restaurant2Host;

import java.util.Collections;
import java.util.List;
import java.util.ArrayList;
import java.util.Random;

import city.gui.PersonGui;
import city.transportation.BusStopAgent;
import city.transportation.CarAgent;
import Role.Role;
import agent.Agent;

public class PersonAgent extends Agent{
	
	//DATA
	public List<String> events = Collections.synchronizedList(new ArrayList<String>());
	public List<String> foodsToEat = new ArrayList<String>();
	
	List<Role> roles;
	enum PersonState {idle, hungry, choosingFood, destinationSet, payRent};
	PersonState state;
	HouseAgent house;
	List<Restaurant> restaurants;
	Restaurant recentlyVisitedRestaurant; 	//so the person won’t go there twice in a row
	CarAgent car;
	String destination;
	enum TransportationState{takingCar, takingBus, walking, chooseTransport};
	TransportationState transportationState;
	CityMap cityMap;
	BusStopAgent busStop;
	//List<MyMeals> meals;
	enum FoodState {cooking, done};
	//List<MyAppliances> appliancesToFix;
	enum ApplianceState {broken, beingFixed, Fixed};
	PersonAgent landlord;
	//List<Order> recievedOrders;   //orders the person has gotten that they need to deal with
	//List<MarketAgent> markets;
	List<String> groceryList;
	//List<Bills> billsToPay;
	double takeHome; 		//some amount to take out of every paycheck and put in wallet
	double wallet;
	double moneyToDeposit;
	//BankAgent bank;
	//BankerRole bankTeller;
	enum BankState {none, deposit, withdraw, loan};   //so we know what the person is doing at the bank
	BankState bankState;
	Boolean firstTimeAtBank = true;	//determines whether person needs to create account
	int accountNumber;
	
	PersonGui gui;
	

	public PersonAgent(){
		super();
		
		//populate foods list -- need to make sure this matches up with market
		foodsToEat.add("Chicken");
		foodsToEat.add("Steak");
		foodsToEat.add("Salad");
		foodsToEat.add("Pizza");
		
		msgImHungry();
		
	}
	
	public void setGui(PersonGui g){
		gui = g;
	}
	
	//MESSAGES
	public void msgImHungry(){	//sent from GUI ?
		events.add("GotHungry");
		stateChanged();
	}
	
	public void msgImBroken(String type) {
		// TODO Auto-generated method stub
		
	}

	public void msgItemInStock(String type) {
		// TODO Auto-generated method stub
		
	}

	public void msgDontHaveItem(String type) {
		// TODO Auto-generated method stub
		
	}

	public void msgFoodDone(String type) {
		// TODO Auto-generated method stub
		
	}
	
	public void msgRentDue(double rate) {
		// TODO Auto-generated method stub
		
	}

	public void msgFixed(String string) {
		// TODO Auto-generated method stub
		
	}
	
	
	//SCHEDULER
	protected boolean pickAndExecuteAnAction() {
		synchronized(events){
			for(String e : events){
				if(e.equals("GotHungry")){
					Eat();
					return true;
				}
			}
		}
		
		return false;
	}
	
	
	//ACTIONS
	
	public void Eat(){	//hacked for now so that it randomly picks eating at home or going out
		synchronized(events){
			for(String e : events){
				if(e.equals("GotHungry")){
					events.remove(e);
					break;
				}
			}
		}
		Random rand = new Random();
		int x = rand.nextInt(1);
		if(x == 1){
			int y = rand.nextInt(foodsToEat.size());
			String food = foodsToEat.get(y);
			//house.msgCheckFridge(food);
			print("I'm going to eat " + food);
		}
		else{
			goToRestaurant();
		}
	}
	
	public void goToRestaurant(){
		print("Going to go to a restaurant");
		Restaurant restaurant2 = new Restaurant();
		//restaurant2.host.msgIWantFood(restaurant2.customer);
		gui.goToRestaurant(2);
	}
	
	
	//CLASSES
	class Restaurant{
		Restaurant2Host host;	//HACK for testing: TODO: fix this
		Restaurant2Customer customer;	//HACK for testing: TODO: fix this
		
		public Restaurant(){
			//nothing yet
		}
		
		public void setHost(Restaurant2Host h){
			host = h;
		}
		
		public void setCustomer(Restaurant2Customer c){
			customer = c;
		}
	}

}
