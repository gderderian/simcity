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
import city.gui.Bank.BankAnimationPanel;
import city.gui.Bank.BankCustomerRoleGui;
import city.gui.Bank.BankGui;
import city.gui.House.ApartmentAnimationPanel;
import city.gui.House.HouseAnimationPanel;
import city.gui.Market.MarketAnimationPanel;
import Role.BankCustomerRole;
import Role.BankManagerRole;
import Role.BankTellerRole;
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

	ControlPanel controlPanel = new ControlPanel();
	
    private static final int TIMER_INTERVAL = 30;
    private Timer timer;
    
    private CityClock masterClock;

	// Restaurants
	
		// Restaurant 1 (Trevor)
    	Restaurant1 rest1 = new Restaurant1();
		Restaurant1AnimationPanel restaurant1 = new Restaurant1AnimationPanel(rest1);

		// Restaurant 2 (Holly)
		Restaurant2 rest2 = new Restaurant2();
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
	Market market= new Market();
		

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
	
	//HouseAnimationPanel house1= new HouseAnimationPanel();
	ArrayList<HouseAnimationPanel> houses = new ArrayList<HouseAnimationPanel>();
	
	// Master list of city buildings
	List<BuildingPanel> buildingPanels = new ArrayList<BuildingPanel>();

	private JPanel infoPanel;

	private final int WINDOWX = 1300;
	private final int WINDOWY = 750;
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
		controlPanel.setCityGui(this);

		//testPerson.startThread();
		//testPerson.setGui(testPersonGui);
		//testPersonGui.addAnimationPanel(restaurant2);
		//guis.add(testPersonGui);
		//cityPanel.addGui(testPersonGui);

		setBounds(WINDOW_X_COORD, WINDOW_Y_COORD, WINDOWX, WINDOWY);

		setLayout(new BorderLayout());

		animationPanel.setCityGui(this);
		animationPanel.setPreferredSize(new Dimension(ANIMATIONX, WINDOWY));

		//Add all building animation panels to the building panel list!
		//This automatically sets dimensions and cityGui references
		restaurant2.setBackground(new Color(150, 20, 60));
		addBuildingPanel(restaurant2);
		controlPanel.addRest2ToCityMap(rest2);
		controlPanel.addRest4ToCityMap(rest4);
		controlPanel.addRest5ToCityMap(rest5);
		controlPanel.addRest1ToCityMap(rest1);
		controlPanel.addMarketToCityMap(market);
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

		//Set up and populate apartment 1
		addBuildingPanel(apt1);
		for(int i=0; i<10; i++){
			apt1List.add(new HouseAnimationPanel());
			addBuildingPanel(apt1List.get(i));
			buildingPanels.add(apt1List.get(i));
		}
		//Set up and populate apartment 2
		addBuildingPanel(apt2);
		for(int i=0; i<10; i++){
			apt2List.add(new HouseAnimationPanel());
			addBuildingPanel(apt2List.get(i));
			buildingPanels.add(apt2List.get(i));
		}
		
		List<House> houseAgents= controlPanel.getHouses();
		//Set up all of the houses
		for(int i=0; i<26; i++){ 
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
        
	}
	
	public void startMasterClock() {
		masterClock.startTime();
	}


	public void timerTick(int timeOfDay, int hourOfDayHumanTime, long minuteOfDay, String dayState, String amPm, String displayTime) {
		for (PersonAgent person : people) {
			person.msgTimeUpdate(timeOfDay);
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

	public void addPerson(String name, AStarTraversal aStarTraversal, String job, CityMap map, House h){
		PersonAgent newPerson = new PersonAgent(name, aStarTraversal, map, h);
		if(h != null){
			h.setOwner(newPerson);
		}
		
		personFactory(newPerson, job);
		
		people.add(newPerson);
		PersonGui g = new PersonGui(newPerson);
		newPerson.setGui(g);
		
		animationPanel.addGui(g);
		
		guis.add(g);
		
		newPerson.startThread();
	}

	public void enableComeBack(Restaurant2Waiter agent) {
		// TODO Auto-generated method stub

	}

	public void setEnabled(Restaurant2Waiter agent) {
		// TODO Auto-generated method stub

	}        

	public void addVehicle(String type, AStarTraversal aStarTraversal) {
		if(type == "bus") {
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
		
		if(type == "truck") {
			TruckAgent newTruck = new TruckAgent(aStarTraversal, controlPanel.getCityMap());
			vehicles.add(newTruck);
			VehicleGui g = new VehicleGui(newTruck);
			newTruck.setGui(g);
			guis.add(g);
			animationPanel.addGui(g);
			g.setMainAnimationPanel(animationPanel);
			
			newTruck.startThread();
		}
	}   
	
	private void personFactory(PersonAgent p, int i) {
		if(i == 1) {
			Restaurant2CustomerRole customerRole = new Restaurant2CustomerRole(p);
			Restaurant2CustomerGui customerGui = new Restaurant2CustomerGui(customerRole, "cust", 1);
			restaurant2.addGui(customerGui);
			Restaurant2WaiterRole waiterRole = new Restaurant2WaiterRole("waiter", p);
			p.addFirstJob(waiterRole, "rest2");
			customerRole.setGui(customerGui);
			p.addRole(customerRole, false);
		}
		if(i == 2) {
			Restaurant1CustomerRole customerRole = new Restaurant1CustomerRole(p.getName(), p);
			Restaurant1CustomerGui customerGui = new Restaurant1CustomerGui(customerRole);
			restaurant1.addGui(customerGui);
			Restaurant1WaiterRole waiterRole = new Restaurant1WaiterRole("waiter", p);
			p.addFirstJob(waiterRole, "rest1");
			customerRole.setGui(customerGui);
			p.addRole(customerRole, false);
		}
		if(i == 3) { // Restaurant 3 (Grant) Testing
			CustomerRole3 customerRole = new CustomerRole3(p.getName(), 50,50, p);
			CustomerGui3 customerGui = new CustomerGui3(customerRole, null, 50, 50, 0); // GUI should be passed into 2nd agmt
			restaurant3.addGui(customerGui);
			WaiterRole3 waiterRole = new WaiterRole3("waiter", 50, 50,p);
			p.addFirstJob(waiterRole, "rest3");
			customerRole.setGui(customerGui);
			p.addRole(customerRole, false);
		}
		
		if(i == 5) {
			Restaurant5HostRole hostrole = new Restaurant5HostRole("", p);
			Restaurant5Gui restaurant5gui = new Restaurant5Gui();
			Restaurant5CustomerRole customerRole = new Restaurant5CustomerRole(p.getName(), p);
			Restaurant5CustomerGui customerGui = new Restaurant5CustomerGui(customerRole, restaurant5gui, hostrole);
			restaurant5.addGui(customerGui);
			Restaurant5WaiterRole waiterRole = new Restaurant5WaiterRole("waiter", p);
			p.addFirstJob(waiterRole, "rest5");
			customerRole.setGui(customerGui);
			p.addRole(customerRole, false);
			
			
			
			
		}
	}
	
	private void personFactory(PersonAgent p, String job) {
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

		BankCustomerRole bankCustomerRole = new BankCustomerRole(p.wallet);
		bankCustomerRole.setPerson(p);
		BankGui bankgui = new BankGui();
		BankCustomerRoleGui bankCustomerGui = new BankCustomerRoleGui(bankCustomerRole, bankgui); 
		bankCustomerGui.setPresent(false);
		bank1Animation.addGui(bankCustomerGui);
		bankCustomerRole.setGui(bankCustomerGui);
		p.addRole(bankCustomerRole, false);
		
		
		/* Now, create a job role */
		if(!job.equals("No job")){
			Role r = getNewRole(job, p);
			if(job.contains("Restaurant2")){
				p.addFirstJob(r, "rest2");
				if(r instanceof Restaurant2HostRole){
					rest2.setHost((Restaurant2HostRole)r);
					p.setRoleActive(r);
				}
				else if(r instanceof Restaurant2WaiterRole){
					rest2.addWaiters((Restaurant2WaiterRole) r);
				}
				else if(r instanceof Restaurant2CookRole){
					rest2.setCook((Restaurant2CookRole) r);
					p.setRoleActive(r);
				}
				else if(r instanceof Restaurant2CashierRole){
					rest2.setCashier((Restaurant2CashierRole) r);
					p.setRoleActive(r);
				}
			}
			else if(job.contains("Restaurant1")) {
				p.addFirstJob(r, "rest1");
				if(r instanceof Restaurant1HostRole) {
					rest1.setHost((Restaurant1HostRole) r);
					p.setRoleActive(r);
				}
				else if(r instanceof Restaurant1WaiterRole){
					rest1.addWaiters((Restaurant1WaiterRole) r);
				}
				else if(r instanceof Restaurant1CookRole){
					rest1.setCook((Restaurant1CookRole) r);
					p.setRoleActive(r);
				}
				else if(r instanceof Restaurant1CashierRole){
					rest1.setCashier((Restaurant1CashierRole) r);
					p.setRoleActive(r);
				}
			}
			else if(job.contains("Restaurant3")) {
				p.addFirstJob(r, "rest3");
				if(r instanceof HostRole3) {
					rest3.setHost((HostRole3) r);
					p.setRoleActive(r);
				}
				else if(r instanceof WaiterRole3){
					rest3.addWaiters((WaiterRole3) r);
				}
				else if(r instanceof CookRole3){
					rest3.setCook((CookRole3) r);
					p.setRoleActive(r);
				}
				else if(r instanceof CashierRole3){
					rest3.setCashier((CashierRole3) r);
					p.setRoleActive(r);
				}
			}
			else if(job.contains("Restaurant4")) {
				p.addFirstJob(r, "rest4");
				if(r instanceof HostRole4) {
					rest4.setHost((HostRole4)r);
					p.setRoleActive(r);
				}
				else if(r instanceof CookRole4){
					rest4.setCook((CookRole4) r);
					p.setRoleActive(r);
				}
				else if(r instanceof CashierRole4){
					rest4.setCashier((CashierRole4) r);
					p.setRoleActive(r);
				}
				else if(r instanceof WaiterRole4){
					rest4.addWaiters((WaiterRole4) r);
				}
			}
			
			else if(job.contains("Restaurant5")) {
				p.addFirstJob(r, "rest5");
				if(r instanceof Restaurant5HostRole) {
					rest5.setHost((Restaurant5HostRole)r);
					p.setRoleActive(r);
				}
				else if(r instanceof Restaurant5CookRole){
					rest5.setCook((Restaurant5CookRole) r);
					p.setRoleActive(r);
				}
				else if(r instanceof Restaurant5CashierRole){
					rest5.setCashier((Restaurant5CashierRole) r);
					p.setRoleActive(r);
				}
				else if(r instanceof Restaurant5WaiterRole){
					rest5.addWaiters((Restaurant5WaiterRole) r);
				}

			}			
			
			
			else if(job.contains("Market")) {
				p.addFirstJob(r, "mark1");
				if(r instanceof MarketWorker){
					market.addWorker((MarketWorker)r);
					p.setRoleActive(r);
				}
				else if(r instanceof MarketManager){
					market.setManager((MarketManager)r);
					p.setRoleActive(r);
				}
			}
			
			
			else if(job.contains("Bank")) {
				p.addFirstJob(r, "bank1");
				     
				if(r instanceof BankManagerRole) {
					System.out.println("<<<<<<<<<<<<<<<<<<   adding bank manager");
					bank.setBankManager((BankManagerRole) r);
					p.setRoleActive(r);
				}
				if(r instanceof BankTellerRole) {
					bank.getBankManager().msgBankTellerArrivedAtBank((BankTellerRole) r);
					p.setRoleActive(r);
				}
					
			}
		}
	}
	
	public void backToCityView() {
		changeView("City");
	}

	private void addBuildingPanel(BuildingPanel bp) {
		bp.setPreferredSize(new Dimension(ANIMATIONX, WINDOWY));
		buildingPanels.add(bp);
		bp.setCityGui(this);
	}
	
    
	public Role getNewRole(String type, PersonAgent p){
		if(type.equals("Restaurant2 Waiter")){
			//Creates role, gui for role
			//Adds role to gui and gui to role
			//Adds role to correct animation panel
			Restaurant2WaiterRole role = new Restaurant2WaiterRole(p.getName(), p);
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
			gui.setPresent(true);
			return role;
		}
		else if(type.equals("Restaurant2 Cashier")){
			Restaurant2CashierRole role = new Restaurant2CashierRole(p.getName(), p);
			return role;
		}
		else if(type.equals("Restaurant2 Customer")){
			Restaurant2CustomerRole role = new Restaurant2CustomerRole(p.getName(), p);
			//Restaurant2CustomerGui gui = new Restaurant2CustomerGui(role, p.getName(), 1);
			return role;
		}
		//else if(type.equals("Bank Manager")) return new BankManagerRole();
		else if(type.equals("Restaurant1 Customer")){
			Restaurant1CustomerRole role = new Restaurant1CustomerRole(p.getName(), p);
			return role;
		}
		else if(type.equals("Restaurant1 Waiter")){
			Restaurant1WaiterRole role = new Restaurant1WaiterRole(p.getName(), p);
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
			gui.setPresent(true);
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
			gui.setPresent(true);
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
			gui.setPresent(true);
			return role;
		}
		else if(type.equals("Restaurant4 Waiter")){
			WaiterRole4 role = new SharedDataWaiterRole4(p.getName(), p); 
			WaiterGui4 gui = new WaiterGui4(role);
			role.setGui(gui);
			restaurant4.addGui(gui);
			gui.setPresent(false);
			return role;
		} else if (type.equals("Restaurant3 Customer")){
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
			gui.setPresent(true);
			return role;
		} else if (type.equals("Restaurant3 Waiter")){
			WaiterRole3 role= new WaiterRole3(p.getName(), 50, 50, p); 
			WaiterGui3 gui = new WaiterGui3(role);
			role.setGui(gui);
			restaurant3.addGui(gui);
			gui.setPresent(false);
			return role;
		}
		else if(type.equals("Market Manager")){
			MarketManager role= new MarketManager(p.getName(), p); 
			return role;
		}
		else if(type.equals("Market Worker")){
			MarketWorker role= new MarketWorker(p); 
			return role;
		}
				
		else if(type.equals("Restaurant5 Customer")){
			Restaurant5CustomerRole role = new Restaurant5CustomerRole(p.getName(), p);
			return role;
		}
		else if(type.equals("Restaurant5 Waiter")){
			Restaurant5WaiterRole role = new Restaurant5WaiterRole(p.getName(),p);
			Restaurant5HostRole hrole = new Restaurant5HostRole(p.getName(), p);
			Restaurant5Gui rest5gui = new Restaurant5Gui();
			Restaurant5WaiterGui gui = new Restaurant5WaiterGui(role, rest5gui, hrole);
			role.setGui(gui);
			restaurant5.addGui(gui);
			return role;
		}
		else if(type.equals("Restaurant5 Cook")){
			Restaurant5CookRole role = new Restaurant5CookRole(p.getName(), p);
			Restaurant5Gui rest5gui = new Restaurant5Gui();
			Restaurant5CookGui gui = new Restaurant5CookGui(role, rest5gui);
			role.setGui(gui);
			restaurant5.addGui(gui);
			return role;
		}
		else if(type.equals("Restaurant5 Host")){
			Restaurant5HostRole role = new Restaurant5HostRole(p.getName(), p);
			return role;
		}
		else if(type.equals("Restaurant5 Cashier")){
			Restaurant5CashierRole role = new Restaurant5CashierRole(p.getName(), p);
			return role;
		}
		
		
		
		else return null;
	}
}