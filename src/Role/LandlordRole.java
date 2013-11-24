package Role;

import interfaces.Landlord;
import interfaces.Person;
import city.PersonAgent;

import java.util.*;

import test.mock.EventLog;
import test.mock.LoggedEvent;
import Role.Role;

public class LandlordRole extends Role implements Landlord {
	//DATA
	double earnings= 0.0;
	public List<MyTenant> tenants= new ArrayList<MyTenant>();
	public EventLog log= new EventLog();
	String name;
	PersonAgent p;
	
	public LandlordRole(String name, PersonAgent p){
		super();
		name= "Landlord";
		this.p= p;
	}
	
	//This should be done when the people are created, assuming they are living in an apartment and are not a landlord
	public void addTenant(Person p){
		MyTenant t= new MyTenant(p);
		tenants.add(t);
	}
	
	
	//MESSAGES
	public void msgEndOfDay(){	
		log.add(new LoggedEvent("Recieved msgEndOfDay, all tenants now should have rent due"));
		print("Recieved msgEndOfDay, all tenants now should have rent due");
		for(MyTenant t : tenants){
			t.numOutstandingPayments++;
			t.newPayment= true;
			if(t.numOutstandingPayments > 0){
				t.paymentsUpToDate= false;
			}
		}
		this.p.stateChanged();
	}

	public void msgHereIsMyRent(Person p, double amount){ // for the normative scenario, assuming tenant always pays correct amount for rent
		log.add(new LoggedEvent("Recieved msgHereIsMyRent from tenant, tenant should now have no outstanding payments due"));
		earnings += amount;
		for(MyTenant t : tenants){
			if(t.tenant.equals(p)){
				t.numOutstandingPayments--;
				if(t.numOutstandingPayments <= 0){
					t.paymentsUpToDate= true;
				}
			}
		}
		this.p.stateChanged();
	}

	public void msgFixAppliance(Person p, String a){
		log.add(new LoggedEvent("Recieved msgFixAppliance from tenant, tenant should now have " + a + " in needsMaintenance"));
		for(MyTenant t : tenants){
			if(t.tenant.equals(p)){
				t.needsMaintenance.add(a);
			}
		}
		this.p.stateChanged();
	}
	
	//From Animation
	public void msgAnimationAtStove(){
		
	}
	
	public void msgAnimationAtOven(){
		
	}
	
	public void msgAnimationAtMicrowave(){
		
	}
	
	
	//SCHEDULER
	public boolean pickAndExecuteAnAction(){
		for(MyTenant t : tenants){
			if(t.newPayment == true){// t.paymentsUpToDate == false){
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
		System.out.println("tenant's name: " + mt.tenant);
		mt.tenant.msgRentDue(this, mt.rate);
		mt.newPayment= false;
		p.stateChanged();
	}

	private void fixAppliance(final MyTenant mt){
		System.out.println("Fixing appliance.");
		mt.tenant.msgFixed(mt.needsMaintenance.get(0));
		mt.needsMaintenance.remove(0);
		System.out.println("needsMaintenance.size(): " + mt.needsMaintenance.size());
		p.stateChanged();	
	}

	
	
	//CLASSES
	public class MyTenant{
		Person tenant;
		double rate;
		boolean paymentsUpToDate= true; 
		boolean newPayment= false;
		public int numOutstandingPayments= 0;
		public List<String> needsMaintenance= Collections.synchronizedList(new ArrayList<String>());
	
		public MyTenant(Person p){
			tenant= p;
			rate= 10.00;
		}
	}
}
