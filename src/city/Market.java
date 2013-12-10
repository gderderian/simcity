/*
Market Class - Supports MarketWorker role and MarketManager role
*/
package city;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import city.transportation.TruckAgent;
import Role.MarketManager;
import Role.MarketWorker;

public class Market { // Class modeled after comment made in https://github.com/usc-csci201-fall2013/simcity201/issues/28

	public MarketManager mktManager;
	TruckAgent myTruck;
	String marketName;
	
	public Market(){
		
	}
	
	public Market(String name){
		marketName = name;
	}
	
	public void setManager(MarketManager mk){
		mktManager= mk;
	}
	
	public void addWorker(MarketWorker mw){
		mktManager.addWorker(mw);
	}

	public MarketManager getMarketManager() {
		return mktManager;
	}
	
	public void setTruck(TruckAgent newTruck){
		myTruck = newTruck;
	}
	
	public void setName(String name){
		marketName = name;
	}
	
	public String getName(){
		return marketName;
	}

}