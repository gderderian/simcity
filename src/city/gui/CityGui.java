package city.gui;

import interfaces.Restaurant2Waiter;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import restaurant1.Restaurant1;
import restaurant1.Restaurant1CashierRole;
import restaurant1.Restaurant1CookRole;
import restaurant1.Restaurant1CustomerRole;
import restaurant1.Restaurant1HostRole;
import restaurant1.Restaurant1NormalWaiterRole;
import restaurant1.Restaurant1WaiterRole;
import restaurant1.gui.Restaurant1AnimationPanel;
import restaurant1.gui.Restaurant1CashierGui;
import restaurant1.gui.Restaurant1CookGui;
import restaurant1.gui.Restaurant1CustomerGui;
import restaurant1.gui.Restaurant1WaiterGui;
import city.Restaurant2.Restaurant2CashierRole;
import city.Restaurant2.Restaurant2CookRole;
import city.Restaurant2.Restaurant2CustomerRole;
import city.Restaurant2.Restaurant2HostRole;
import city.Restaurant2.Restaurant2WaiterRole;
import city.Restaurant2.Restaurant2WaiterRoleRegular;
import city.Restaurant2.Restaurant2WaiterRoleSharedData;
import city.gui.Bank.BankAnimationPanel;
import city.gui.Bank.BankCustomerRoleGui;
import city.gui.Bank.BankGui;
import city.gui.Bank.BankManagerRoleGui;
import city.gui.Bank.BankTellerRoleGui;
import city.gui.House.ApartmentAnimationPanel;
import city.gui.House.HouseAnimationPanel;
import city.gui.House.LandlordGui;
import city.gui.Market.MarketAnimationPanel;
import city.gui.Market.MarketCustomerGui;
import city.gui.Market.MarketGui;
import city.gui.Market.MarketManagerGui;
import city.gui.Market.MarketWorkerGui;
import Role.BankCustomerRole;
import Role.BankManagerRole;
import Role.BankTellerRole;
import Role.LandlordRole;
import Role.MarketCustomerRole;
import Role.MarketManager;
import Role.MarketWorker;
import Role.Role;
import astar.AStarTraversal;
import city.Restaurant2.Restaurant2;
import city.gui.CityClock;
import city.gui.restaurant4.AnimationPanel4;
import city.gui.restaurant4.CookGui4;
import city.gui.restaurant4.CustomerGui4;
import city.gui.restaurant4.WaiterGui4;
import city.ApartmentBuilding;
import city.Bank;
import city.CityMap;
import city.House;
import city.Market;
import city.PersonAgent;
import city.gui.restaurant2.Restaurant2AnimationPanel;
import city.gui.restaurant2.Restaurant2CookGui;
import city.gui.restaurant2.Restaurant2CustomerGui;
import city.gui.restaurant2.Restaurant2WaiterGui;
import city.transportation.BusAgent;
import city.transportation.CarAgent;
import city.transportation.TruckAgent;
import city.transportation.Vehicle;
import city.Restaurant3.*;
import city.Restaurant4.CashierRole4;
import city.Restaurant4.CookRole4;
import city.Restaurant4.CustomerRole4;
import city.Restaurant4.SharedDataWaiterRole4;
import city.Restaurant4.WaiterRole4;
import city.Restaurant4.HostRole4;
import city.Restaurant4.Restaurant4;
import city.Restaurant5.Restaurant5;
import city.Restaurant5.Restaurant5CashierRole;
import city.Restaurant5.Restaurant5CookRole;
import city.Restaurant5.Restaurant5CustomerRole;
import city.Restaurant5.Restaurant5HostRole;
import city.Restaurant5.Restaurant5RegularWaiterRole;
import city.Restaurant5.Restaurant5WaiterRole;
import city.gui.Restaurant3.*;
import city.gui.Restaurant5.Restaurant5AnimationPanel;
import city.gui.Restaurant5.Restaurant5CookGui;
import city.gui.Restaurant5.Restaurant5CustomerGui;
import city.gui.Restaurant5.Restaurant5Gui;
import city.gui.Restaurant5.Restaurant5WaiterGui;

import java.awt.*;
import java.awt.event.*;
import java.util.List;
import java.util.ArrayList;

/**
 * Main GUI class.
 * Contains the main frame and subsequent panels
 */
public class CityGui extends JFrame implements ActionListener, ChangeListener {

	AnimationPanel animationPanel = new AnimationPanel();

	ControlPanel controlPanel = new ControlPanel(this);

	private static final int TIMER_INTERVAL = 30;
	private Timer timer;

	private CityClock masterClock;
	
	Restaurant5Gui rest5gui = new Restaurant5Gui();
	BankGui bankgui = new BankGui();

	// Restaurants

	// Restaurant 1 (Trevor)
	Restaurant1 rest1 = new Restaurant1();
	Restaurant1AnimationPanel restaurant1 = new Restaurant1AnimationPanel(rest1);

	// Restaurant 2 (Holly)
	Restaurant2 rest2 = Restaurant2.getInstance();
	Restaurant2AnimationPanel restaurant2 = new Restaurant2AnimationPanel(rest2);

	// Restaurant 3 (Grant)
	Restaurant3 rest3 = new Restaurant3();
	AnimationPanel3 restaurant3 = new AnimationPanel3();

	// Restaurant 4 (Justine)
	Restaurant4 rest4 = new Restaurant4();
	AnimationPanel4 restaurant4 = new AnimationPanel4();

	Restaurant5 rest5 = new Restaurant5();
	Restaurant5AnimationPanel restaurant5 = new Restaurant5AnimationPanel(rest5);
	// Restaurant 5 (Tom)


	// Market
	Market market1 = new Market("mark1");
	Market market2 = new Market("mark2");
	Market market3 = new Market("mark3");

	int truckMarketCounter = 0; //This will balance the trucks being added to markets
	int managerMarketCounter = 0; //This balances the managers so one is sent to each market
	int workerMarketCounter = 0; //This will balance the workers being added to markets

	// Market Animation Panels
	MarketAnimationPanel market1Animation = new MarketAnimationPanel(this);
	MarketAnimationPanel market2Animation = new MarketAnimationPanel(this);
	MarketAnimationPanel market3Animation = new MarketAnimationPanel(this);

	// Bank
	Bank bank = new Bank();

	// Bank Animation Panels
	BankAnimationPanel bank1Animation = new BankAnimationPanel(this);

	// Apartment Animation Panels
	ApartmentAnimationPanel apt1= new ApartmentAnimationPanel(1);
	ArrayList<HouseAnimationPanel> apt1List= new ArrayList<HouseAnimationPanel>();
	ApartmentAnimationPanel apt2= new ApartmentAnimationPanel(2);
	ArrayList<HouseAnimationPanel> apt2List= new ArrayList<HouseAnimationPanel>();
	ApartmentBuilding apart1= new ApartmentBuilding();

	//HouseAnimationPanel house1= new HouseAnimationPanel();
	ArrayList<HouseAnimationPanel> houses = new ArrayList<HouseAnimationPanel>();
	ApartmentBuilding apart2= new ApartmentBuilding();

	// Master list of city buildings
	List<BuildingPanel> buildingPanels = new ArrayList<BuildingPanel>();

	private JPanel infoPanel;

	private final int WINDOWX = 1300;
	private final int WINDOWY = 730;
	private final int ANIMATIONX = 900;
	private final int WINDOW_X_COORD = 50;
	private final int WINDOW_Y_COORD = 50;

	ArrayList<Gui> guis = new ArrayList<Gui>();
	ArrayList<PersonAgent> people = new ArrayList<PersonAgent>();

	ArrayList<Vehicle> vehicles = new ArrayList<Vehicle>();

	/**
	 * Constructor for RestaurantGui class.
	 * Sets up all the gui components.
	 */
	public CityGui() {            

		setBounds(WINDOW_X_COORD, WINDOW_Y_COORD, WINDOWX, WINDOWY);

		setLayout(new BorderLayout());

		animationPanel.setCityGui(this);
		animationPanel.setPreferredSize(new Dimension(ANIMATIONX, WINDOWY));

		//Add all building animation panels to the building panel list!
		//This automatically sets dimensions and cityGui references
		restaurant2.setBackground(new Color(150, 20, 60));
		addBuildingPanel(restaurant2);
		controlPanel.addRest3ToCityMap(rest3);
		controlPanel.addRest4ToCityMap(rest4);
		controlPanel.addRest5ToCityMap(rest5);
		controlPanel.addRest1ToCityMap(rest1);
		controlPanel.addMarketToCityMap(market1, 1);
		controlPanel.addMarketToCityMap(market2, 2);
		controlPanel.addMarketToCityMap(market3, 3);
		controlPanel.addBankToCityMap(bank);
		//controlPanel.addBankToCityMap(bank);

		restaurant1.setBackground(Color.LIGHT_GRAY);
		addBuildingPanel(restaurant1);
		addBuildingPanel(restaurant4);
		addBuildingPanel(restaurant5);

		addBuildingPanel(bank1Animation);
		addBuildingPanel(market1Animation);
		addBuildingPanel(market2Animation);
		addBuildingPanel(market3Animation);


		addBuildingPanel(restaurant3);

		add(animationPanel, BorderLayout.EAST);

		List<House> houseAgents= controlPanel.getHouses();
		//Set up and populate apartment 1
		controlPanel.addApartment1ToCityMap(apart1);
		addBuildingPanel(apt1);

		for(int i=0; i<20; i++){
			HouseAnimationPanel temp= new HouseAnimationPanel();
			apt1List.add(temp);
			houseAgents.get(i + 22).setHouseAnimationPanel(temp);
			addBuildingPanel(apt1List.get(i));
			buildingPanels.add(apt1List.get(i));
		}
		//Set up and populate apartment 2
		controlPanel.addApartment2ToCityMap(apart2);
		addBuildingPanel(apt2);
		for(int i=0; i<20; i++){
			HouseAnimationPanel temp= new HouseAnimationPanel();
			apt2List.add(temp);
			houseAgents.get(i + 42).setHouseAnimationPanel(temp);
			addBuildingPanel(apt2List.get(i));
			buildingPanels.add(apt2List.get(i));
		}
		//Set up all of the houses

		for(int i=0; i<22; i++){ 
			HouseAnimationPanel temp= new HouseAnimationPanel();
			houseAgents.get(i).setHouseAnimationPanel(temp);
			houses.add(temp);
			addBuildingPanel(houses.get(i));
		}
		//addBuildingPanel(house1);
		//End of adding building panels!

		Dimension panelDim = new Dimension(WINDOWX - ANIMATIONX, WINDOWY);
		infoPanel = new JPanel();
		infoPanel.setPreferredSize(panelDim);
		infoPanel.setMinimumSize(panelDim);
		infoPanel.setMaximumSize(panelDim);
		infoPanel.setBorder(BorderFactory.createTitledBorder("Information"));

		infoPanel.setLayout(new FlowLayout());

		//add(infoPanel, BorderLayout.WEST);
		add(controlPanel, BorderLayout.WEST);

		masterClock = new CityClock(this);
		//masterClock.startTime(); //Started by populateCity button in ControlPanel

		timer = new Timer(TIMER_INTERVAL, this);
		timer.start();

		controlPanel.createInitialPeople();

	}

	public void startMasterClock() {
		masterClock.startTime();
	}


	public void timerTick(int timeOfDay, int hourOfDayHumanTime, long minuteOfDay, String dayState, String amPm, String displayTime) {
		for (PersonAgent person : people) {
			person.msgTimeUpdate(timeOfDay, hourOfDayHumanTime, minuteOfDay, amPm);
		}
		controlPanel.setTimeDisplay(displayTime);
	}

	public void addGui(Gui g){
		guis.add(g);
	}

	public void addPerson(PersonAgent p){
		people.add(p);
	}

	public void actionPerformed(ActionEvent e) {
		animationPanel.updatePos();
		synchronized(buildingPanels){
			for(BuildingPanel b : buildingPanels){
				b.updatePos();
			}
		}
	}

	/**
	 * Main routine to get gui started
	 */
	public static void main(String[] args) {
		CityGui gui = new CityGui();
		gui.setTitle("SimCity201");
		gui.setVisible(true);
		gui.setResizable(true);
		gui.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}

	public void stateChanged(ChangeEvent e) {
		//if(e.getSource() ==
		//(slider)
	}

	public void changeView(String building){
		
		controlPanel.changeBuildingControlPanel(building);
		
		if(building.equals("City")){
			for(BuildingPanel bp : buildingPanels) {
				bp.setVisible(false);
			}
			animationPanel.setVisible(true);
			animationPanel.setEnabled(true);
			add(animationPanel, BorderLayout.EAST);
			return;
		}  

		controlPanel.enableBackToCity(); //If not switching to city, enable "Change back to city view" button

		if(building.equals("Restaurant2")){
			animationPanel.setVisible(false);
			add(restaurant2, BorderLayout.EAST);
			restaurant2.setVisible(true);
			return;
		}     
		if(building.equals("Restaurant1")){
			animationPanel.setVisible(false);
			add(restaurant1, BorderLayout.EAST);
			restaurant1.setVisible(true);
			return;
		}
		if(building.equals("Restaurant3")){
			animationPanel.setVisible(false);
			add(restaurant3, BorderLayout.EAST);
			restaurant3.setVisible(true);
			return;
		}
		if(building.equals("Restaurant4")){
			animationPanel.setVisible(false);
			add(restaurant4, BorderLayout.EAST);
			restaurant4.setVisible(true);
			return;
		}
		if(building.equals("Restaurant5")) {
			animationPanel.setVisible(false);
			add(restaurant5, BorderLayout.EAST);
			restaurant5.setVisible(true);
			return;
		}

		if(building.equals("Market1")){
			animationPanel.setVisible(false);
			add(market1Animation, BorderLayout.EAST);
			market1Animation.setVisible(true);
			return;
		}
		if(building.equals("Market2")){
			animationPanel.setVisible(false);
			add(market2Animation, BorderLayout.EAST);
			market2Animation.setVisible(true);
			return;
		}
		if(building.equals("Market3")){
			animationPanel.setVisible(false);
			add(market3Animation, BorderLayout.EAST);
			market3Animation.setVisible(true);
			return;
		}
		if(building.equals("Bank1")){
			animationPanel.setVisible(false);
			add(bank1Animation, BorderLayout.EAST);
			bank1Animation.setVisible(true);
			return;
		}
		if(building.equals("Apartment1")){
			animationPanel.setVisible(false);
			add(apt1, BorderLayout.EAST);
			apt1.setVisible(true);
			return;
		}
		if(building.equals("Apartment2")){
			animationPanel.setVisible(false);
			add(apt2, BorderLayout.EAST);
			apt2.setVisible(true);
			return;
		}
	}

	public void changeView(int building, int num){
		if(building == 0) {
			animationPanel.setVisible(false);
			add(houses.get(num), BorderLayout.EAST);
			houses.get(num).setVisible(true);
		}
		if(building == 1) {
			apt1.setVisible(false);
			for(int i = 0; i < 10; i++)
				apt1List.get(i).setVisible(false);
			add(apt1List.get(num), BorderLayout.EAST);
			apt1List.get(num).setVisible(true);
		}
		if(building == 2){
			apt2.setVisible(false);
			for(int i = 0; i < 10; i++)
				apt2List.get(i).setVisible(false);
			add(apt2List.get(num), BorderLayout.EAST);
			apt2List.get(num).setVisible(true);
		}
		controlPanel.enableBackToCity();
	}

	public void addPerson(String name, AStarTraversal aStarTraversal, String job, CityMap map, House h, CarAgent c){
		PersonAgent newPerson = new PersonAgent(name, aStarTraversal, map, h);
		if(h != null){
			h.setOwner(newPerson);
		}

		PersonGui g = new PersonGui(newPerson);
		newPerson.setGui(g);

		newPerson.setClock(masterClock);

		//if(!job.equals("No job")){
		//	newPerson.addTask("goToWork");
		//}

		//newPerson.addTask("gotHungry");
		//newPerson.addTask("goToBank");
		//newPerson.addTask("goToMarket");

		animationPanel.addGui(g);

		guis.add(g);

		personFactory(newPerson, job, g);

		people.add(newPerson);

		if(c != null)
			addCar(newPerson, c);

		newPerson.startThread();

		newPerson.setBank(bank);
	}

	public void enableComeBack(Restaurant2Waiter agent) {
		// TODO Auto-generated method stub

	}

	public void setEnabled(Restaurant2Waiter agent) {
		// TODO Auto-generated method stub

	}        

	public void addVehicle(String type, AStarTraversal aStarTraversal) {
		if(type.equals("bus")) {
			BusAgent newBus = new BusAgent(aStarTraversal, controlPanel.getCityMap());
			newBus.addBusStops(controlPanel.getBusStops());
			vehicles.add(newBus);
			VehicleGui g = new VehicleGui(newBus);
			newBus.setGui(g);
			guis.add(g);
			animationPanel.addGui(g);
			g.setMainAnimationPanel(animationPanel);

			newBus.startThread();   
		}

		if(type.equals("truck")) {
			TruckAgent newTruck = new TruckAgent(aStarTraversal, controlPanel.getCityMap());
			vehicles.add(newTruck);
			VehicleGui g = new VehicleGui(newTruck);
			newTruck.setGui(g);
			guis.add(g);
			animationPanel.addGui(g);
			g.setMainAnimationPanel(animationPanel);

			switch(truckMarketCounter) {
			case 0: 
				market1.getMarketManager().setTruck(newTruck);
				newTruck.setMarketManager(market1.getMarketManager());
				break;
			case 1: 
				market2.getMarketManager().setTruck(newTruck);
				newTruck.setMarketManager(market2.getMarketManager());
				break;
			case 2: 
				market3.getMarketManager().setTruck(newTruck);
				newTruck.setMarketManager(market3.getMarketManager());
				break;
			}
			truckMarketCounter = (truckMarketCounter + 1) % 3;

			newTruck.startThread();
		}

		if(type.equals("crash")) {
			/*CarAgent c = new CarAgent(aStarTraversal, controlPanel.getCityMap());
			vehicles.add(c);
			VehicleGui g = new VehicleGui(c);
			c.setGui(g);
			guis.add(g);
			animationPanel.addGui(g);
			g.setMainAnimationPanel(animationPanel);

			c.startThread();*/
		}
	}   

	public void addCar(PersonAgent p, CarAgent c) {
		vehicles.add(c);
		VehicleGui g = new VehicleGui(c);
		c.setGui(g);
		animationPanel.addGui(g);
		g.setMainAnimationPanel(animationPanel);

		p.addCar(c);

		c.startThread();
	}

	private void personFactory(PersonAgent p, int i) {
		if(i == 1) {
			Restaurant2CustomerRole customerRole = new Restaurant2CustomerRole(p);
			Restaurant2CustomerGui customerGui = new Restaurant2CustomerGui(customerRole, "cust", 1);
			restaurant2.addGui(customerGui);
			Restaurant2WaiterRole waiterRole = new Restaurant2WaiterRoleRegular("waiter", p);
			p.addFirstJob(waiterRole, "rest2", 3);
			customerRole.setGui(customerGui);
			p.addRole(customerRole, false);
		}
		if(i == 2) {
			Restaurant1CustomerRole customerRole = new Restaurant1CustomerRole(p.getName(), p);
			Restaurant1CustomerGui customerGui = new Restaurant1CustomerGui(customerRole);
			restaurant1.addGui(customerGui);
			Restaurant1WaiterRole waiterRole = new Restaurant1NormalWaiterRole("waiter", p);
			p.addFirstJob(waiterRole, "rest1", 3);
			customerRole.setGui(customerGui);
			p.addRole(customerRole, false);
		}
		if(i == 3) { // Restaurant 3 (Grant) Testing
			CustomerRole3 customerRole = new CustomerRole3(p.getName(), -20, -20, p);
			CustomerGui3 customerGui = new CustomerGui3(customerRole, null, -20, -20, 0);
			restaurant3.addGui(customerGui);
			WaiterRole3 waiterRole = new WaiterRole3Normal("waiter", 230, 230,p);
			p.addFirstJob(waiterRole, "rest3", 3);
			customerRole.setGui(customerGui);
			p.addRole(customerRole, false);
		}

		if(i == 5) {
			Restaurant5HostRole hostrole = new Restaurant5HostRole("", p);
			Restaurant5Gui restaurant5gui = new Restaurant5Gui();
			Restaurant5CustomerRole customerRole = new Restaurant5CustomerRole(p.getName(), p);
			Restaurant5CustomerGui customerGui = new Restaurant5CustomerGui(customerRole, restaurant5gui, hostrole);
			restaurant5.addGui(customerGui);
			Restaurant5RegularWaiterRole waiterRole = new Restaurant5RegularWaiterRole("waiter", p);
			p.addFirstJob(waiterRole, "rest5", 3);
			customerRole.setGui(customerGui);
			p.addRole(customerRole, false);




		}
	}

	private void personFactory(PersonAgent p, String job, PersonGui gui) {
		/* Creating customer role for eating at restaurant1 */
		Restaurant1CustomerRole customerRole1 = new Restaurant1CustomerRole(p.getName(), p);
		Restaurant1CustomerGui customerGui1 = new Restaurant1CustomerGui(customerRole1);
		customerRole1.setGui(customerGui1);
		restaurant1.addGui(customerGui1);
		customerGui1.setPresent(false);
		p.addRole(customerRole1, false);		

		/* Creating customer role for eating at restaurant2 */
		Restaurant2CustomerRole customerRole2 = new Restaurant2CustomerRole(p);
		Restaurant2CustomerGui customerGui2 = new Restaurant2CustomerGui(customerRole2, p.getName(), 1);
		customerGui2.setPresent(false);
		restaurant2.addGui(customerGui2);
		customerRole2.setGui(customerGui2);
		p.addRole(customerRole2, false);

		/* Creating customer role for eating at restaurant3 */
		CustomerRole3 customerRole3 = new CustomerRole3(p.getName(), 5, 5, p);
		CustomerGui3 customerGui3 = new CustomerGui3(customerRole3, 5, 5, 5);
		customerGui3.setPresent(false);
		restaurant3.addGui(customerGui3);
		customerRole3.setGui(customerGui3);
		p.addRole(customerRole3, false);

		/* Creating a customer role for eating at restaurant4 */
		CustomerRole4 customerRole4 = new CustomerRole4(p.getName(), p);
		CustomerGui4 customerGui4 = new CustomerGui4(customerRole4); 
		customerGui4.setPresent(false);
		restaurant4.addGui(customerGui4);
		customerRole4.setGui(customerGui4);
		p.addRole(customerRole4, false);

		//Creating a customer role for eating at restaurant5
		Restaurant5CustomerRole customerRole5 = new Restaurant5CustomerRole( p.getName(),p);
		Restaurant5Gui restaurant5gui = new Restaurant5Gui();
		Restaurant5HostRole restaurant5host = new Restaurant5HostRole("", null);
		Restaurant5CustomerGui customerGui5 = new Restaurant5CustomerGui(customerRole5, restaurant5gui, restaurant5host );
		customerGui5.setPresent(false);
		restaurant5.addGui(customerGui5);
		customerRole5.setGui(customerGui5);
		p.addRole(customerRole5, false);

		//Add bank customer role to go to bank
		BankCustomerRole bankCustomerRole = new BankCustomerRole(p.wallet);
		bankCustomerRole.setPerson(p);
		BankGui bankgui = new BankGui();
		BankCustomerRoleGui bankCustomerGui = new BankCustomerRoleGui(bankCustomerRole, bankgui); 
		bankCustomerGui.setPresent(false);
		bank1Animation.addGui(bankCustomerGui);
		bankCustomerRole.setGui(bankCustomerGui);
		p.addRole(bankCustomerRole, false);

		//Add market customer role to go to market
		MarketCustomerRole marketCustomer = new MarketCustomerRole(p.getName(), p);
		MarketGui marketGui = new MarketGui();
		MarketCustomerGui mktCustomerGui = new MarketCustomerGui(marketCustomer, marketGui); 
		mktCustomerGui.setPresent(false);
		market1Animation.addGui(mktCustomerGui);
		marketCustomer.setGui(mktCustomerGui);
		p.addRole(marketCustomer, false);


		/* Check if the person lives in an apartment and add them to the correct tenant list */
		if((p.house != null) && (!job.contains("Landlord"))){
			if((p.house.getNum() >= 21) && (p.house.getNum() <= 41)){
				apart1.addTenant(p);
			}
			else if((p.house.getNum() >= 42) && (p.house.getNum() <= 62)){
				apart2.addTenant(p);
			}
		}

		/* Now, create a job role */
		if(!job.equals("No job")){
			Role r = getNewRole(job, p);
			if(job.contains("Restaurant2")){
				if(r instanceof Restaurant2HostRole){
					rest2.setHost((Restaurant2HostRole)r);
					p.addFirstJob(r, "rest2", 1);	//goes to work at 1am
				}
				else if(r instanceof Restaurant2WaiterRole){
					rest2.addWaiters((Restaurant2WaiterRole) r);
					p.addFirstJob(r, "rest2", 3);
				}
				else if(r instanceof Restaurant2CookRole){
					rest2.setCook((Restaurant2CookRole) r);
					p.addFirstJob(r, "rest2", 3);
				}
				else if(r instanceof Restaurant2CashierRole){
					rest2.setCashier((Restaurant2CashierRole) r);
					p.addFirstJob(r, "rest2", 2);
				}
			}
			else if(job.contains("Restaurant1")) {
				if(r instanceof Restaurant1HostRole) {
					rest1.setHost((Restaurant1HostRole) r);
					p.addFirstJob(r, "rest1", 1);
				}
				else if(r instanceof Restaurant1WaiterRole){
					rest1.addWaiters((Restaurant1WaiterRole) r);
					p.addFirstJob(r, "rest1", 3);
				}
				else if(r instanceof Restaurant1CookRole){
					((Restaurant1CookRole) r).addMarket(market1.getMarketManager());
					rest1.setCook((Restaurant1CookRole) r);
					p.addFirstJob(r, "rest1", 3);
				}
				else if(r instanceof Restaurant1CashierRole){
					rest1.setCashier((Restaurant1CashierRole) r);
					p.addFirstJob(r, "rest1", 2);
				}
			}
			else if(job.contains("Restaurant3")) {
				if(r instanceof HostRole3) {
					rest3.setHost((HostRole3) r);
					p.addFirstJob(r, "rest3", 1);
				}
				else if(r instanceof WaiterRole3){
					rest3.addWaiters((WaiterRole3) r);
					p.addFirstJob(r, "rest3", 3);
				}
				else if(r instanceof CookRole3){
					rest3.setCook((CookRole3) r);
					p.addFirstJob(r, "rest3", 3);
				}
				else if(r instanceof CashierRole3){
					rest3.setCashier((CashierRole3) r);
					p.addFirstJob(r, "rest3", 2);
				}
			}
			else if(job.contains("Restaurant4")) {
				if(r instanceof HostRole4) {
					rest4.setHost((HostRole4)r);
					p.addFirstJob(r, "rest4", 1);
				}
				else if(r instanceof CookRole4){
					rest4.setCook((CookRole4) r);
					p.addFirstJob(r, "rest4", 3);
				}
				else if(r instanceof CashierRole4){
					rest4.setCashier((CashierRole4) r);
					p.addFirstJob(r, "rest4", 2);
				}
				else if(r instanceof WaiterRole4){
					rest4.addWaiters((WaiterRole4) r);
					p.addFirstJob(r, "rest4", 3);
				}
			} else if(job.contains("Restaurant5")) {
				if(r instanceof Restaurant5HostRole) {
					rest5.setHost((Restaurant5HostRole)r);
					p.addFirstJob(r, "rest5", 1);
				}
				else if(r instanceof Restaurant5CookRole){
					rest5.setCook((Restaurant5CookRole) r);
					p.addFirstJob(r, "rest5", 3);
				}
				else if(r instanceof Restaurant5CashierRole){
					rest5.setCashier((Restaurant5CashierRole) r);
					p.addFirstJob(r, "rest5", 2);
				}
				else if(r instanceof Restaurant5WaiterRole){
					rest5.addWaiters((Restaurant5WaiterRole) r);
					p.addFirstJob(r, "rest5", 3);
				}

			} else if(job.contains("market") || job.contains("Market")) {
				if(r instanceof MarketWorker){
					
					switch(workerMarketCounter) {
					case 0: 
						market1.addWorker((MarketWorker)r);
						p.addFirstJob(r, "mark1", 2);
						break;
					case 1: 
						market2.addWorker((MarketWorker)r);
						p.addFirstJob(r, "mark2", 2);
						break;
					case 2: 
						market3.addWorker((MarketWorker)r);
						p.addFirstJob(r, "mark3", 2);
						break;
					}
					
					workerMarketCounter = (workerMarketCounter + 1) % 3;
				}
				else if(r instanceof MarketManager){
					if(managerMarketCounter > 2) {
						System.out.println("NO    (Already have 3 market managers...)");
						return;
					}
					
					switch(managerMarketCounter) {
					case 0: 
						market1.setManager((MarketManager)r);
						p.addFirstJob(r, "mark1", 1);
						break;
					case 1: 
						market2.setManager((MarketManager)r);
						p.addFirstJob(r, "mark2", 1);
						break;
					case 2: 
						market3.setManager((MarketManager)r);
						p.addFirstJob(r, "mark3", 1);
						break;
					}
					managerMarketCounter++;
				}
			} else if(job.contains("Bank")) {
				/*
				if(r instanceof BankTellerRole) {
					bank.addBankTeller((BankTellerRole)r);
					p.setRoleActive(r);
				}
				 */
				if(r instanceof BankManagerRole) {
					System.out.println("adding bank manager in the bank!");
					bank.setBankManager((BankManagerRole) r);
					p.setRoleActive(r);
					p.addFirstJob(r, "bank1", 1);
				}

			} else if(job.contains("Landlord")){
				if(job.equals("Landlord1")){
					p.addFirstJob(r, "apart1", 1);
					p.setRoleActive(r);
					apart1.setLandlord((LandlordRole)r);
				}
				else if(job.equals("Landlord2")){
					p.addFirstJob(r, "apart2", 1);
					p.setRoleActive(r);
					apart2.setLandlord((LandlordRole)r);
				}
			}
		}
	}

	public void backToCityView() {
		changeView("City");
	}

	public void addBuildingPanel(BuildingPanel bp) {
		bp.setPreferredSize(new Dimension(ANIMATIONX, WINDOWY));
		buildingPanels.add(bp);
		bp.setCityGui(this);
	}


	public Role getNewRole(String type, PersonAgent p){
		//Creates role, gui for role
		//Adds role to gui and gui to role
		//Adds role to correct animation panel
		if(type.equals("Restaurant2 Waiter")){
			Restaurant2WaiterRole role = new Restaurant2WaiterRoleSharedData(p.getName(), p);
			Restaurant2WaiterGui gui = new Restaurant2WaiterGui(role, p.getName(), this, 1);
			role.setGui(gui);
			restaurant2.addGui(gui);
			gui.setPresent(false);
			return role;
		}
		else if(type.equals("Restaurant2 Host")){
			Restaurant2HostRole role = new Restaurant2HostRole(p.getName(), p);
			return role;
		}
		else if(type.equals("Restaurant2 Cook")){
			Restaurant2CookRole role = new Restaurant2CookRole(p.getName(), p);
			Restaurant2CookGui gui = new Restaurant2CookGui(role);
			role.setGui(gui);
			restaurant2.addGui(gui);
			//gui.setPresent(true);
			gui.setPresent(false);
			return role;
		}
		else if(type.equals("Restaurant2 Cashier")){
			Restaurant2CashierRole role = new Restaurant2CashierRole(p.getName(), p);
			return role;
		}
		else if(type.equals("Restaurant2 Customer")){
			Restaurant2CustomerRole role = new Restaurant2CustomerRole(p.getName(), p);
			return role;
		}
		//else if(type.equals("Bank Manager")) return new BankManagerRole();
		else if(type.equals("Restaurant1 Customer")){
			Restaurant1CustomerRole role = new Restaurant1CustomerRole(p.getName(), p);
			return role;
		}
		else if(type.equals("Restaurant1 Waiter")){
			Restaurant1WaiterRole role = new Restaurant1NormalWaiterRole(p.getName(), p);
			Restaurant1WaiterGui gui = new Restaurant1WaiterGui(role);
			gui.setHome(rest4.getWaiterListSize() * 40 + 200, 60);
			role.setGui(gui);
			restaurant1.addGui(gui);
			gui.setPresent(false);
			return role;
		}
		else if(type.equals("Restaurant1 Cook")){
			Restaurant1CookRole role = new Restaurant1CookRole(p.getName(), p);
			Restaurant1CookGui gui = new Restaurant1CookGui(role);
			role.setGui(gui);
			restaurant1.addGui(gui);
			gui.setPresent(false);
			return role;
		}
		else if(type.equals("Restaurant1 Host")){
			Restaurant1HostRole role = new Restaurant1HostRole(p.getName(), p);
			return role;
		}
		else if(type.equals("Restaurant1 Cashier")){
			Restaurant1CashierRole role = new Restaurant1CashierRole(p.getName(), p);
			Restaurant1CashierGui gui = new Restaurant1CashierGui(role);
			restaurant1.addGui(gui);
			gui.setPresent(false);
			return role;
		}
		else if(type.equals("Restaurant4 Customer")){
			CustomerRole4 role= new CustomerRole4(p.getName(), p);
			return role;
		}
		else if(type.equals("Restaurant4 Host")){
			HostRole4 role= new HostRole4(p.getName(), p);
			System.out.println("Host shouldnt be null: " + role);
			return role;
		}
		else if(type.equals("Restaurant4 Cashier")){
			CashierRole4 role= new CashierRole4(p.getName(), p);
			return role;
		}
		else if(type.equals("Restaurant4 Cook")){
			CookRole4 role= new CookRole4(p.getName(), p);
			CookGui4 gui = new CookGui4(role);
			role.setGui(gui);
			restaurant4.addGui(gui);
			gui.setPresent(false);
			return role;
		}
		else if(type.equals("Restaurant4 Waiter")){
			WaiterRole4 role = new SharedDataWaiterRole4(p.getName(), p); 
			WaiterGui4 gui = new WaiterGui4(role);
			role.setGui(gui);
			restaurant4.addGui(gui);
			gui.setPresent(false);
			return role;
		}
		else if (type.equals("Restaurant3 Customer")){
			CustomerRole3 role= new CustomerRole3(p.getName(), 50, 50, p);
			return role;
		} else if (type.equals("Restaurant3 Host")){
			HostRole3 role= new HostRole3(p.getName(), p);
			return role;
		} else if (type.equals("Restaurant3 Cashier")){
			CashierRole3 role= new CashierRole3(p.getName(), p);
			return role;
		} else if (type.equals("Restaurant3 Cook")){
			CookRole3 role= new CookRole3(p.getName(), p);
			CookGui3 gui = new CookGui3(role);
			role.setGui(gui);
			restaurant3.addGui(gui);
			gui.setPresent(false);
			return role;
		} else if (type.equals("Restaurant3 Waiter")){
			WaiterRole3 role= new WaiterRole3Normal(p.getName(), 230, 230, p); 
			WaiterGui3 gui = new WaiterGui3(role);
			role.setGui(gui);
			restaurant3.addGui(gui);
			gui.setPresent(false);
			return role;
		}
		else if(type.equals("Market Manager")){
			MarketManager role = new MarketManager("Market ManagerJoe", p, market1);
			role.setPerson(p);
			MarketGui mktGui = new MarketGui(); 
			MarketManagerGui gui = new MarketManagerGui(role, mktGui);
			role.setGui(gui);
			market1Animation.addGui(gui);
			gui.setPresent(false);
			return role;

		}
		else if(type.equals("Market Worker")){
			MarketWorker role = new MarketWorker(p);
			role.setPerson(p);
			MarketGui mktGui = new MarketGui(); 
			MarketWorkerGui gui1 = new MarketWorkerGui(role, mktGui);
			role.setGui(gui1);
			market1Animation.addGui(gui1);
			gui1.setPresent(false);
			return role;
		}
		else if(type.equals("Market Customer")){
			System.out.println("CREATING CUSTOMER GUI FOR MARKET CUSTOMERCREATING CUSTOMER GUI FOR MARKET CUSTOMERCREATING CUSTOMER GUI FOR MARKET CUSTOMERCREATING CUSTOMER GUI FOR MATKET CUSTOMERCREATING CUSTOMER GUI FOR MATKET CUSTOMERCREATING CUSTOMER GUI FOR MATKET CUSTOMER");
			MarketCustomerRole role = new MarketCustomerRole(p.getName(), p);
			role.setPerson(p);
			MarketGui mktGui = new MarketGui(); 
			MarketCustomerGui gui2 = new MarketCustomerGui(role, mktGui);
			role.setGui(gui2);
			market1Animation.addGui(gui2);
			gui2.setPresent(false);
			return role;
		}	
		else if(type.equals("Restaurant5 Customer")){
			Restaurant5CustomerRole role = new Restaurant5CustomerRole(p.getName(), p);
			return role;
		}
		else if(type.equals("Restaurant5 Waiter")){
			Restaurant5RegularWaiterRole role = new Restaurant5RegularWaiterRole(p.getName(),p);
			Restaurant5HostRole hrole = new Restaurant5HostRole(p.getName(), p);
			Restaurant5WaiterGui gui = new Restaurant5WaiterGui(role, rest5gui, hrole);
			role.setGui(gui);
			restaurant5.addGui(gui);
			gui.setPresent(false);
			return role;
		}
		else if(type.equals("Restaurant5 Cook")){
			Restaurant5CookRole role = new Restaurant5CookRole(p.getName(), p);
			Restaurant5CookGui gui = new Restaurant5CookGui(role, rest5gui);
			role.setGui(gui);
			restaurant5.addGui(gui);
			gui.setPresent(false);
			return role;
		}
		else if(type.equals("Restaurant5 Host")){
			Restaurant5HostRole role = new Restaurant5HostRole(p.getName(), p);
			return role;
		}
		else if(type.equals("Restaurant5 Cashier")){
			Restaurant5CashierRole role = new Restaurant5CashierRole(p.getName(), p);
			return role;
		} else if(type.equals("Bank Manager")){
			System.out.println("!!!!!!!!!!!!  I'm in get role");
			BankManagerRole role = new BankManagerRole(bank);
			role.setPerson(p);
			BankManagerRoleGui gui = new BankManagerRoleGui(role, bankgui);
			role.setGui(gui);
			bank1Animation.addGui(gui);
			return role;
		} else if(type.equals("Bank Teller")) {
			BankTellerRole role = new BankTellerRole(null);
			role.setPerson(p);
			System.out.println("!!!!!!!!!!!!!!!!!!!!!! creating teller in  getnewrole()");
			BankTellerRoleGui gui = new BankTellerRoleGui(role, bankgui);
			role.setGui(gui);
			bank1Animation.addGui(gui);
			return role;	
		} else if(type.equals("Landlord1")){
			LandlordRole role = new LandlordRole(p.getName(), p); 
			LandlordGui gui = new LandlordGui(role);
			role.setGui(gui);
			apt1.addGui(gui); 
			gui.setPresent(false);
			return role;
		} else if(type.equals("Landlord2")){
			LandlordRole role = new LandlordRole(p.getName(), p); 
			LandlordGui gui = new LandlordGui(role);
			role.setGui(gui);
			apt2.addGui(gui); 
			gui.setPresent(false);
			return role;
		}

		else return null;
	}

	public void setTime(String hour, String minute, String amPm){
		masterClock.setDayTime(Integer.parseInt(hour), Integer.parseInt(minute), amPm);
	}

}