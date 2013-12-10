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
import java.util.concurrent.Semaphore;

import test.mock.EventLog;
import test.mock.LoggedEvent;
import Role.Role;

public class LandlordRole extends Role implements Landlord {
	
	String roleName = "LandlordRole";
	
	//DATA
	double earnings= 0.0;
	public List<MyTenant> tenants= Collections.synchronizedList(new ArrayList<MyTenant>());
	public EventLog log= new EventLog();
	private Timer fix= new Timer();
	String name;
	PersonAgent p;
	LandlordGui gui;
	private Semaphore atDest= new Semaphore(0);
	boolean setRoleInactive= false;
	
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
	
	public LandlordGui getGui(){
		return gui;
	}
	
	//MESSAGES
	public void msgCollectRent(){	
		log.add(new LoggedEvent("Recieved msgEndOfDay, all tenants now should have rent due"));
		log("Time to collect rent from all of my tenants!");
		synchronized(tenants){
			for(MyTenant t : tenants){
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
		log("Yay, " + p.getName() + " payed their rent!");
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
					t.maintenance= true;
				}
			}
		}
		this.p.stateChanged();
	}
	
	//From Animation
	public void msgAnimationAtStove(){
		log("Reached the broken stove, fixin' time!");
		atDest.release();
	}
	
	public void msgAnimationAtOven(){
		log("Reached the broken oven, fixin' time!");
		atDest.release();
	}
	
	public void msgAnimationAtMicrowave(){
		log("Reached the broken microwave, fixin' time!");
		atDest.release();
	}
	
	public void msgAnimationExited(){
		log("Glad I could help, goodbye!");
		atDest.release();
	}
	
	
	//SCHEDULER
	public boolean pickAndExecuteAnAction(){
		//log("Landlord role scheduler, Y U NO WORK?");
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
		if(setRoleInactive){
			log("Setting role inactive ova here (landlord role scheduler)");
			deactivate();
		}
		return false;
	}
	
	
	//ACTIONS
	private void collectRent(MyTenant mt){
		log("Collecting rent now");
		mt.tenant.msgRentDue(this, mt.rate);
		mt.newPayment= false;
		p.stateChanged();
	}

	private void fixAppliance(final MyTenant mt){
		log("Going to fix a tenant's appliance.");
		final String temp= mt.needsMaintenance.remove(0);
		PersonTask pt= new PersonTask("goToApartment");
		p.DoGoTo(mt.tenant.getHouse().getName(), pt);
		if(temp.equals("Stove")){
			gui.goToStove();
		} else if(temp.equals("Oven")){
			gui.goToOven();
		} else if(temp.equals("Microwave")){
			gui.goToStove();
		}
		try{
			atDest.acquire();
		} catch(InterruptedException e){}
		fix.schedule(new TimerTask() {
			@Override public void run() {
				mt.tenant.msgFixed(temp);
				mt.maintenance= false;
				log("All fixed, time to go home");
				gui.goToExit();
				try{
					atDest.acquire();
				} catch(InterruptedException e){}
				gui.setPresent(false);
				mt.house.getAnimationPanel().notInHouse(gui);
				setRoleInactive= true;
				p.stateChanged();
				return;
			}}, 4000);
	}

	public void deactivate(){
		setRoleInactive= false;
		p.setRoleInactive(this);
		p.setGuiVisible();
		p.stateChanged();
	}
	
	//CLASSES
	public class MyTenant{
		Person tenant;
		HouseInterface house;
		double rate;
		boolean paymentsUpToDate= true; 
		boolean newPayment= false;
		boolean maintenance= false;
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
        ActivityLog.getInstance().logActivity(tag, msg, name, false);
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
			if(t.maintenance){
				t.house.getAnimationPanel().addGui(gui);
			}
		}
		gui.setPresent(true);
	}

	@Override
	public PersonAgent getPerson() {
		return p;
	}
}
