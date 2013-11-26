package interfaces;

import Role.LandlordRole;
import city.MarketOrder;
import city.transportation.BusAgent;
import city.transportation.CarAgent;
import city.transportation.TruckAgent;

public interface Person {

	public abstract void msgImHungry();
	
	//From house
	public abstract void msgImBroken(String type);
	
	public abstract void msgItemInStock(String type);

	public abstract void msgDontHaveItem(String food);

	public abstract void msgFoodDone(String food);
	
	public abstract void msgFridgeFull();

	public abstract void msgSpaceInFridge(int spaceLeft);
	
	public abstract void msgApplianceBrokeCantCook();
	
	
	//Messages from bus/bus stop
	public abstract void msgArrivedAtStop(int stop);
	
	public abstract void msgPleasePayFare(Bus b, double fare);
	
	public abstract void msgBusIsHere(Bus b);
	
	public abstract void msgArrived(Car c);
		
	
	//from landlord
	public abstract void msgFixed(String appliance);
	
	public abstract void msgRentDue(Landlord r, double rate);
	
	public abstract void msgHereIsYourOrder(MarketOrder order);
	
	public abstract void msgHereIsYourOrder(TruckAgent truck, MarketOrder order);
}
