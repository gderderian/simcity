package Role;

import interfaces.Landlord;

import java.util.*;

import city.PersonAgent;
import Role.Role;

public class LandlordRole extends Role implements Landlord {
	//DATA
	double earnings= 0.0;
	List<MyTenant> tenants= new ArrayList<MyTenant>();
	
	LandlordRole(){
		super();
	}
	
	
	//MESSAGES
	public void msgEndOfDay(){	
		for(MyTenant t : tenants){
			t.numOutstandingPayments++;
			t.newPayment= true;
			if(t.numOutstandingPayments > 0){
				t.paymentsUpToDate= false;
			}
		}
		stateChanged();
	}

	public void msgHereIsMyRent(PersonAgent p, double amount){ // for the normative scenario, assuming tenant always pays correct amount for rent
		earnings += amount;
		for(MyTenant t : tenants){
			if(t.tenant.equals(p)){
				t.numOutstandingPayments--;
				if(t.numOutstandingPayments <= 0){
					t.paymentsUpToDate= true;
				}
			}
		}
		stateChanged();
	}

	public void msgFixAppliance(PersonAgent p, String a){
		for(MyTenant t : tenants){
			if(t.tenant.equals(p)){
				t.needsMaintenance.add(a);
			}
		}
		stateChanged();
	}
	
	
	//SCHEDULER
	protected boolean pickAndExecuteAnAction(){
		for(MyTenant t : tenants){
			if(t.newPayment == true || t.paymentsUpToDate == false){
				collectRent(t);
				return true;
			}
		}
		for(MyTenant t : tenants){
			synchronized(t.needsMaintenance){
				for(String appliance : t.needsMaintenance){
						fixAppliance(t);
						return true;
				}
			}
		}
		return false;
	}
	
	
	//ACTIONS
	private void collectRent(MyTenant mt){
		mt.tenant.msgRentDue(mt.rate);
		mt.newPayment= false;
		stateChanged();
	}

	private void fixAppliance(MyTenant mt){
		mt.tenant.msgFixed(mt.needsMaintenance.get(0));
		mt.needsMaintenance.remove(0);
		stateChanged();
	}

	
	
	//CLASSES
	private class MyTenant{
		PersonAgent tenant;
		double rate;
		boolean paymentsUpToDate= true; 
		boolean newPayment= false;
		int numOutstandingPayments= 0;
		List<String> needsMaintenance= Collections.synchronizedList(new ArrayList<String>());
	
		public MyTenant(PersonAgent p){
			tenant= p;
			rate= 10.00;
		}
	}
}
