package interfaces;

import astar.Position;
import Role.LandlordRole;
import city.MarketOrder;
import city.transportation.BusAgent;
import city.transportation.CarAgent;
import city.transportation.TruckAgent;

public interface Person {

	public String getName();
	
	public HouseInterface getHouse();
	
	public abstract void msgImHungry();
	
	//From house
	public abstract void msgImBroken(String type);
	
	public abstract void msgItemInStock(String type);

	public abstract void msgDontHaveItem(String food);

	public abstract void msgFoodDone(String food);
	
	public abstract void msgFridgeFull();

	public abstract void msgSpaceInFridge(int spaceLeft);
	
	public abstract void msgApplianceBrokeCantCook(String type);
	
	
	//Messages from bus/bus stop
	public abstract void msgArrivedAtStop(int stop, Position p);
	
	public abstract void msgPleasePayFare(Bus b, double fare);
	
	public abstract void msgBusIsHere(Bus b, Position p);
	
	//Messages from car		
	public abstract void msgArrived(Car c, Position p);
		
	public abstract void msgImPickingYouUp(Car c, Position p);
	
	//from landlord
	public abstract void msgFixed(String appliance);
	
	public abstract void msgRentDue(Landlord r, double rate);
	
	public abstract void msgHereIsYourOrder(MarketOrder order);
	
	public abstract void msgHereIsYourOrder(TruckAgent truck, MarketOrder order);

	void msgHereIsYourOrder(Car car);
}
