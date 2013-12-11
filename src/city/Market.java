/*
Market Class - Supports MarketWorker role and MarketManager role
*/
package city;

import Role.MarketManagerRole;
import Role.MarketWorkerRole;

public class Market { // Class modeled after comment made in https://github.com/usc-csci201-fall2013/simcity201/issues/28

	public MarketManagerRole mktManager;
	String marketName;
	private boolean isOpen;
	
	public Market(String name){
		marketName = name;
	}
	
	public void setManager(MarketManagerRole mk){
		mktManager = mk;
	}
	
	public void addWorker(MarketWorkerRole mw){
		mktManager.addWorker(mw);
	}

	public MarketManagerRole getMarketManager() {
		return mktManager;
	}
	
	//public void setTruck(TruckAgent newTruck){
	//	mktManager.setTruck(newTruck);
	//}
	
	public void setName(String name){
		marketName = name;
	}
	
	public String getName(){
		return marketName;
	}
	
	public boolean isOpen(){
		return isOpen;
	}

}