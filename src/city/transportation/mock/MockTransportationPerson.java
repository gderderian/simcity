package city.transportation.mock;

import city.MarketOrder;
import interfaces.Bus;
import interfaces.Car;
import interfaces.Landlord;
import interfaces.Person;

public class MockTransportationPerson implements Person {

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
		System.out.println("Got message: at stop #" + stop);
		
	}

	@Override
	public void msgPleasePayFare(Bus b, double fare) {
		System.out.println("Got message: Please pay fare of $" + fare);
		
	}

	@Override
	public void msgBusIsHere(Bus b) {
		System.out.println("Got message: Bus is here");
		
	}

	@Override
	public void msgArrived(Car c) {
		System.out.println("Got message: Car arrived");
		
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
	public void msgHereIsYourOrder(MarketOrder order) {
		// TODO Auto-generated method stub
		
	}

}
