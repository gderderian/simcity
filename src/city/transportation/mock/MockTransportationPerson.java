package city.transportation.mock;

import test.mock.EventLog;
import test.mock.LoggedEvent;
import test.mock.Mock;
import city.MarketOrder;
import city.transportation.TruckAgent;
import interfaces.Bus;
import interfaces.Car;
import interfaces.Landlord;
import interfaces.Person;

public class MockTransportationPerson extends Mock implements Person {
	
	public EventLog log;
	
	public MockTransportationPerson(String name) {
		super(name);
		log = new EventLog();
	}
	
	@Override
	public void msgImHungry() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgImBroken(String type) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgItemInStock(String type) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgDontHaveItem(String food) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgFoodDone(String food) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgFridgeFull() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgSpaceInFridge(int spaceLeft) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgApplianceBrokeCantCook() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgArrivedAtStop(int stop) {
		log.add(new LoggedEvent("Got message: Arrived at stop"));		
	}

	@Override
	public void msgPleasePayFare(Bus b, double fare) {
		log.add(new LoggedEvent("Got message: Please pay fare"));			
	}

	@Override
	public void msgBusIsHere(Bus b) {
		log.add(new LoggedEvent("Got message: Bus is here"));
		
	}

	@Override
	public void msgArrived(Car c) {
		log.add(new LoggedEvent("Got message: Car arrived"));
	}

	@Override
	public void msgFixed(String appliance) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgRentDue(Landlord r, double rate) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgHereIsYourOrder(Car car) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgHereIsYourOrder(TruckAgent truck, MarketOrder order) {
		log.add(new LoggedEvent("Received order from market truck"));		
	}

	@Override
	public void msgHereIsYourOrder(MarketOrder order) {
		// TODO Auto-generated method stub
		
	}

}
