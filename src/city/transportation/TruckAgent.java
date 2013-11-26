package city.transportation;

import interfaces.MarketManager;

import java.util.*;
import java.util.concurrent.Semaphore;

import activityLog.ActivityLog;
import activityLog.ActivityTag;
import astar.AStarTraversal;
import city.MarketOrder;
import city.PersonAgent;

public class TruckAgent extends Vehicle {
        //Data
        MarketManager market; //Market that this truck reports to
        
        public List <MyMarketOrder> orders = new ArrayList<MyMarketOrder>();
        
        String name = "Truck";
        
        private boolean test = false;
        
        ActivityTag tag = ActivityTag.TRUCK;
        
        class MyMarketOrder {
                MarketOrder o;
                boolean delivered;
                
                public MyMarketOrder(MarketOrder o) {
                        this.o = o;
                }
        }
        
        public TruckAgent(MarketManager market, AStarTraversal aStarTraversal) {
        	super(aStarTraversal);
        	this.market = market;
        	capacity = 0;
        	guiFinished = new Semaphore(0, true);
        }
        
        //Messages
        public void msgPleaseDeliver(MarketOrder o) {
                orders.add(new MyMarketOrder(o));
                stateChanged();
        }
        
        public void msgOrderReceived(PersonAgent p, MarketOrder o) {
                MyMarketOrder temp = null;
                for(MyMarketOrder mo : orders) {
                        if(mo.o == o)
                                temp = mo;
                }
                temp.delivered = true;
                stateChanged();
        }
    	
    	public void msgGuiFinished() {
    		guiFinished.release();
    	}
    	
        //Scheduler
        public boolean pickAndExecuteAnAction() {
                for(MyMarketOrder mo : orders) {
                        if(mo.delivered == true) {
                                ReportToMarket(mo);
                                return true;
                        }
                }
                
                for(MyMarketOrder mo : orders) {
                        if(mo.delivered == false) {
                                DeliverOrder(mo);
                                return true;
                        }
                }
                
                return false;
        }

        //Actions
        private void DeliverOrder(MyMarketOrder o) {
        	log("Going to " + o.o.destination);
        	//DoDriveTo(o.o.destination);
        	log("Delivering order from market to recipient");
        	o.o.getRecipient().msgHereIsYourOrder(this, o.o);
        	o.delivered = true;
        	log("Returning to market");
        	//DoDriveToMarket();
        }
        
        private void ReportToMarket(MyMarketOrder o) {
                market.msgFinishedDelivery(o.o);
                orders.remove(o);
        }
        
        // Accessors
        public int getOrderNum(){
        	return orders.size();
        }
        
    	private void log(String msg){
    		print(msg);
    		if(!test)
    			ActivityLog.getInstance().logActivity(tag, msg, name);
    	}
    	
    	public void thisIsATest() {
    		test = true;
    	}
}
