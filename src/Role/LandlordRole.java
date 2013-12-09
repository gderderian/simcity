package Role;

import interfaces.HouseInterface;
import interfaces.Landlord;
import interfaces.Person;
import activityLog.ActivityLog;
import activityLog.ActivityTag;
import city.PersonAgent;
import city.PersonTask;
import city.gui.House.LandlordGui;

import java.util.*;

import test.mock.EventLog;
import test.mock.LoggedEvent;
import Role.Role;

public class LandlordRole extends Role implements Landlord {
	
	String roleName = "LandlordRole";
	
	//DATA
	double earnings= 0.0;
	public List<MyTenant> tenants= Collections.synchronizedList(new ArrayList<MyTenant>());
	public EventLog log= new EventLog();
	String name;
	PersonAgent p;
	LandlordGui gui;
	
	ActivityTag tag = ActivityTag.LANDLORD;
	
	public LandlordRole(String name, PersonAgent p){
		super();
		this.name= name;
		this.p= p;
	}
	
	//This should be done when the people are created, assuming they are living in an apartment and are not a landlord
	public void addTenant(Person p){
		boolean newTenant= true;
		for(int i=0; i<tenants.size(); i++){
			if(tenants.get(i).tenant == p){
				newTenant= false;
			}
		}
		if(newTenant){
			MyTenant t= new MyTenant(p);
			tenants.add(t);
			log("I got a new tenant, now I have " + tenants.size() + " tenants total!");
		}
	}
	
	
	//MESSAGES
	public void msgEndOfDay(){	
		log.add(new LoggedEvent("Recieved msgEndOfDay, all tenants now should have rent due"));
		synchronized(tenants){
			for(MyTenant t : tenants){
				System.out.println("Tenant: " + t);
				t.numOutstandingPayments++;
				t.newPayment= true;
				if(t.numOutstandingPayments > 0){
					t.paymentsUpToDate= false;
				}
			}
		}
		this.p.stateChanged();
	}

	public void msgHereIsMyRent(Person p, double amount){ // for the normative scenario, assuming tenant always pays correct amount for rent
		log.add(new LoggedEvent("Recieved msgHereIsMyRent from tenant, tenant should now have no outstanding payments due"));
		earnings += amount;
		synchronized(tenants){
			for(MyTenant t : tenants){
				if(t.tenant.equals(p)){
					t.numOutstandingPayments--;
					if(t.numOutstandingPayments <= 0){
						t.paymentsUpToDate= true;
					}
				}
			}
		}
		this.p.stateChanged();
	}

	public void msgFixAppliance(Person p, String a){
		log.add(new LoggedEvent("Recieved msgFixAppliance from tenant, tenant should now have " + a + " in needsMaintenance"));
		synchronized(tenants){
			for(MyTenant t : tenants){
				if(t.tenant.equals(p)){
					t.needsMaintenance.add(a);
				}
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
		synchronized(tenants){
			for(MyTenant t : tenants){
				if(t.newPayment == true){// t.paymentsUpToDate == false){
					collectRent(t);
					return true;
				}
			}
		}
		synchronized(tenants){
			for(MyTenant t : tenants){
				synchronized(t.needsMaintenance){
					for(String appliance : t.needsMaintenance){
							fixAppliance(t);
							return true;
					}
				}
			}
		}
		return false;
	}
	
	
	//ACTIONS
	private void collectRent(MyTenant mt){
		log("tenant's name: " + mt.tenant.getName());
		mt.tenant.msgRentDue(this, mt.rate);
		mt.newPayment= false;
		p.stateChanged();
	}

	private void fixAppliance(final MyTenant mt){
		log("Fixing appliance.");
		PersonTask pt= new PersonTask("goToApartment");
		p.DoGoTo(mt.tenant.getHouse().getName(), pt);
		mt.tenant.msgFixed(mt.needsMaintenance.get(0));
		mt.needsMaintenance.remove(0);
		log("needsMaintenance.size(): " + mt.needsMaintenance.size());
		p.stateChanged();	
	}

	
	
	//CLASSES
	public class MyTenant{
		Person tenant;
		HouseInterface house;
		double rate;
		boolean paymentsUpToDate= true; 
		boolean newPayment= false;
		public int numOutstandingPayments= 0;
		public List<String> needsMaintenance= Collections.synchronizedList(new ArrayList<String>());
	
		public MyTenant(Person p){
			tenant= p;
			house= p.getHouse();
			rate= 10.00;
		}
	}
	
	private void log(String msg){
		print(msg);
        ActivityLog.getInstance().logActivity(tag, msg, name);
        log.add(new LoggedEvent(msg));
	}

	@Override
	public String getRoleName() {
		return roleName;
	}

	public void setGui(LandlordGui gui) {
		this.gui= gui;
	}
	
	public void setGuiActive() {
		for(MyTenant t : tenants){
			if(t.needsMaintenance.size() > 0){
				t.house.getAnimationPanel().addGui(gui);
			}
		}
		gui.setPresent(true);
	}
}
