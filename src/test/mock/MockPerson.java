package test.mock;

import astar.Position;
import Role.LandlordRole;
import city.MarketOrder;
import city.transportation.BusAgent;
import city.transportation.CarAgent;
import city.transportation.TruckAgent;
import interfaces.Bus;
import interfaces.Car;
import interfaces.Landlord;
import interfaces.Person;


public class MockPerson extends Mock implements Person {
	public EventLog log= new EventLog();
	
	public MockPerson(String name) {
		super(name);
	}

	@Override
	public void msgImHungry() {
		log.add(new LoggedEvent("Recieved msgImHungry"));
		System.out.println("Recieved msgImHungry");
	}

	
	//FROM HOUSE
	@Override
	public void msgImBroken(String type) {
		log.add(new LoggedEvent("Recieved msgImBroken from house, " + type + " is the broken appliance."));
		System.out.println("Recieved msgImBroken from house, " + type + " is the broken appliance.");
	}

	@Override
	public void msgItemInStock(String type) {
		log.add(new LoggedEvent("Recieved msgItemInStock from house, I have at least one " + type + " in my fridge."));
		System.out.println("Recieved msgItemInStock from house, I have at least one " + type + " in my fridge.");
	}

	@Override
	public void msgDontHaveItem(String food) {
		log.add(new LoggedEvent("Recieved msgDontHaveItem from house, I dont have any " + food + " in my fridge."));
		System.out.println("Recieved msgDontHaveItem from house, I dont have any " + food + " in my fridge.");
	}

	@Override
	public void msgFoodDone(String food) {
		log.add(new LoggedEvent("Recieved msgFoodDone from house, " + food + " is done cooking now."));
		System.out.println("Recieved msgFoodDone from house, " + food + " is done cooking now.");
	}
	
	@Override
	public void msgFridgeFull() {
		
	}

	@Override
	public void msgSpaceInFridge(int spaceLeft) {
		
	}

	@Override
	public void msgApplianceBrokeCantCook() {
		
	}

	
	//FROM BUS
	@Override
	public void msgArrivedAtStop(int stop) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgBusIsHere(Bus b) {
		// TODO Auto-generated method stub
		
	}

	
	//FROM LANDLORD
	@Override
	public void msgFixed(String appliance) {
		log.add(new LoggedEvent("Recieved msgFixed from landlord, " + appliance + " is now fixed."));
		System.out.println("Recieved msgFixed from landlord, " + appliance + " is now fixed.");
	}

	@Override
	public void msgRentDue(Landlord r, double rate) {
		log.add(new LoggedEvent("Recieved msgRentDue from landlord, I owe $" + rate + "."));
		System.out.println("Recieved msgRentDue from landlord, I owe $" + rate + ".");
	}

	
	//FROM MARKET
	@Override
	public void msgHereIsYourOrder(Car car) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgHereIsYourOrder(TruckAgent truck, MarketOrder order) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgPleasePayFare(Bus b, double fare) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgHereIsYourOrder(MarketOrder order) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgArrived(Car c, Position p) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgImPickingYouUp(Car c, Position p) {
		// TODO Auto-generated method stub
		
	}

}
