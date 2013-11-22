package interfaces;

import Role.LandlordRole;
import city.MarketOrder;
import city.transportation.BusAgent;
import city.transportation.CarAgent;

public interface Person {

	public abstract void msgImHungry();
	
	
	//From house
	public abstract void msgImBroken(String type);
	
	public abstract void msgItemInStock(String type);

	public abstract void msgDontHaveItem(String food);

	public abstract void msgFoodDone(String food);
	
	
	//Messages from bus/bus stop
	public abstract void msgArrivedAtStop(int stop);
	
	public abstract void msgPleasePayFare(BusAgent b, double fare);
	
	public abstract void msgBusIsHere(BusAgent b);
	
	public abstract void msgArrived();
	
	
	//from landlord
	public abstract void msgFixed(String appliance);
	
	public abstract void msgRentDue(LandlordRole r, double rate);
	
	public abstract void msgHereIsYourOrder(CarAgent car);
	
	public abstract void msgHereIsYourOrder(MarketOrder order);
}