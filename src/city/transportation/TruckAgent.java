package city.transportation;

import java.util.*;

import city.MarketOrder;
import city.PersonAgent;

public class TruckAgent extends Vehicle {
        //Data
        //MarketAgent market; //Market that this truck reports to
        
        List <MyMarketOrder> orders = new ArrayList<MyMarketOrder>();
        
        class MyMarketOrder {
                MarketOrder o;
                boolean delivered;
                
                public MyMarketOrder(MarketOrder o) {
                        this.o = o;
                }
        }
        
        public TruckAgent() {
        	capacity = 0;
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
        
        protected boolean pickAndExecuteAnAction() {
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
                //gui.DoDriveTo(o.o.destination);
                //o.o.recipient.msgHereIsDelivery(o.o);
                //gui.DoDriveToMarket();
        }
        
        private void ReportToMarket(MyMarketOrder o) {
                //market.msgFinishedDelivery(o.o);
                orders.remove(o);
        }
}
