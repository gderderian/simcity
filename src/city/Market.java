/*
Market Class - Supports MarketWorker role and MarketManager role
*/
package city;

import Role.BankManagerRole;
import Role.MarketManager;
import Role.MarketWorker;

public class Market { // Class modeled after comment made in https://github.com/usc-csci201-fall2013/simcity201/issues/28

	public MarketManager mktManager;
	
	public void setManager(MarketManager mk){
		mktManager= mk;
	}
	
	public void addWorker(MarketWorker mw){
		mktManager.addWorker(mw);
	}

	public MarketManager getMarketManager() {
		return mktManager;
	}

}