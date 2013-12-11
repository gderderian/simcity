package city.gui;

import interfaces.BusStop;

import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.Semaphore;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;

import restaurant1.Restaurant1;
import restaurant1.gui.Restaurant1AnimationPanel;
import restaurant1.gui.Restaurant1Panel;
import activityLog.ActivityPane;
import astar.AStarTraversal;
import city.Apartment;
import city.ApartmentBuilding;
import city.Bank;
import city.CityMap;
import city.House;
import city.Market;
import city.Restaurant3.Restaurant3;
import city.Restaurant4.Restaurant4;
import city.Restaurant5.Restaurant5;
import city.gui.restaurant2.Restaurant2InfoPanel;
import city.transportation.BusStopAgent;
import city.transportation.CarAgent;

public class ControlPanel extends JPanel implements ActionListener{

	public JScrollPane pane = new JScrollPane(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
    private JPanel view = new JPanel();
    private List<JButton> personButtonList = new ArrayList<JButton>();
    private JButton addPersonButton = new JButton("Add");
    private JTabbedPane controlPane = new JTabbedPane();
    private JPanel worldControls = new JPanel();
    private JPanel timeSelectionPanel = new JPanel();
    private JPanel worldControlPanel = new JPanel();
    private JPanel addPerson = new JPanel();
    private JPanel infoPanel = new JPanel();
    private JLabel clickBuildings = new JLabel("Click on a building to see inside!");
    private ActivityPane activityPane = new ActivityPane();
    private JButton backToCity = new JButton("Switch back to city view");
    private JButton startScenario = new JButton("Start scenario!");
    private JButton changeTime = new JButton("Change Time");
    private JPanel backButtonPanel = new JPanel();
    private JPanel personOptionsDisplay = new JPanel();
    private JButton buyCarButton = new JButton("Buy a Car");
    private JButton carCrash = new JButton("Car Crash");
    private JButton hitAndRun = new JButton("Hit and Run");
    
    /*Building panels*/
    private JPanel buildingInfoPanel = new JPanel();
    List<JPanel> buildingPanels = Collections.synchronizedList(new ArrayList<JPanel>());
    private JPanel restaurant2Panel = new Restaurant2InfoPanel(this);
	private JPanel restaurant1Panel = new Restaurant1Panel(this);
	private JPanel restaurant3Panel = new JPanel();
	private JPanel restaurant4Panel = new JPanel();
	private JPanel restaurant5Panel = new JPanel();
    private JPanel market1Panel = new JPanel();
    private JPanel market2Panel = new JPanel();
    private JPanel market3Panel = new JPanel();
    private JPanel bank1Panel = new JPanel();
    
    private String[] scenarios = {"[Please choose a test to run]", "Full Scenario", "The Weekender", "Trader Joe's", "Restaurant1",
    		"Restaurant2", "Restaurant3", "Restaurant4", "Restaurant5",  "Close Restaurants Test", "Home Meal/Visit Stores Test", 
    		"Bank Test", "Landlord Test", "Market Truck Test", "Traffic Test"
    };
    private JComboBox scenarioSelect = new JComboBox(scenarios);
    
    private String[] buildings = {"[Select a building for info and options]", "Restaurant1", "Restaurant2", "Restaurant3", "Restaurant4",
    		"Restaurant5", "Bank1", "Market1", "Market2", "Market3"
    		};
    
    private JComboBox buildingPanelSelect = new JComboBox(buildings);
    
    // Timer GUI display & control functionality
    private JLabel timeDisplay = new JLabel("12:00am  -  Monday  -  Week 1");
    
    private String[] hours = {"12", "1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11"};
    private JComboBox hourSelect = new JComboBox(hours);
    
    private String[] minutes = {"00", "15", "30", "45"};
    private JComboBox minuteSelect = new JComboBox(minutes);
    
    private String[] amPm = {"am", "pm"};
    private JComboBox amPmSelect = new JComboBox(amPm);
    
    
    private Timer timer = new Timer();
    
    private int WINDOWX = 370;
    private int WINDOWY = 750;
    private int SCROLLY = WINDOWY/4 - 20;
    private int ADDPERSONY = WINDOWY/5;
    private int BACKBUTTONY = 40;
    private int INFOPANELY = SCROLLY + 30;
    private int WINDOWXINSIDE = WINDOWX - 10;
    private int PERSONOPTIONSY = WINDOWY - (INFOPANELY + ADDPERSONY);
    
    private Dimension scrollDim = new Dimension(WINDOWXINSIDE, SCROLLY);
    private Dimension panelDim = new Dimension(WINDOWX, WINDOWY - BACKBUTTONY);
    private Dimension addPersonDim = new Dimension(WINDOWXINSIDE, ADDPERSONY);
    private Dimension infoPanelDim = new Dimension(WINDOWXINSIDE, INFOPANELY);
    private Dimension backButtonDim = new Dimension(WINDOWX, BACKBUTTONY);
    private Dimension personOptionsDim = new Dimension(WINDOWXINSIDE, PERSONOPTIONSY);
    private Dimension buildingPanelDim = new Dimension(WINDOWXINSIDE, WINDOWY - 30);

    private JTextField nameField;
    private JTextField errorDisplay = new JTextField();
    private JPanel personControlPanel = new JPanel();
    public JCheckBox isHungry;
    public JCheckBox takeBreak;
    private String[] jobs = {"[Please select a job]", "No job", "Bank Manager", "Bank Teller", "Market Manager", "Market Worker", "Landlord1", "Landlord2", 
    		"Restaurant1 Host", "Restaurant1 Cook", "Restaurant1 Waiter", "Restaurant1 Cashier","Restaurant2 Host", "Restaurant2 Cook",
    		"Restaurant2 Waiter", "Restaurant 2 WaiterSharedData", "Restaurant2 Cashier", "Restaurant3 Host", "Restaurant3 Cook", "Restaurant3 Waiter", "Restaurant3 Cashier",
    		"Restaurant4 Host", "Restaurant4 Cook", "Restaurant4 SharedDataWaiter", "Restaurant4 NormalWaiter", "Restaurant4 Cashier", "Restaurant5 Host", "Restaurant5 Cook",
    		"Restaurant5 Waiter", "Restaurant5 Cashier"
    };
    private JComboBox jobField = new JComboBox(jobs);
    
    int houseAssignmentNumber = 0;
    
    //TODO populate this
    private Map<String, String> jobLocations = new HashMap<String, String>();
    
    /** Universal city map **/
    CityMap cityMap = CityMap.getInstance();
    // Houses and apartments
    private List<House> houses = new ArrayList<House>();
    
    // Bus stops
    private List<BusStop> busStops = new ArrayList<BusStop>();
    
    // Size of astar semaphore grid
    static int gridX = 25; //# of x-axis tiles
    static int gridY = 20; //# of y-axis tiles

    // Semaphore grid for astar animation
    Semaphore[][] streetGrid = new Semaphore[gridX+1][gridY+1];
    Semaphore[][] sidewalkGrid = new Semaphore[gridX+1][gridY+1];
    
    // Set up rest4 components
    JButton closeRest4;
    JButton emptyInventory4;
    JButton fireHost4;
    
    //Set up rest5 components
    JButton closeRest5;
    JButton emptyInventory5;
    
    // Add rest3 components
    JButton closeRest3;
    JButton emptyRest3;
    JButton fireHost3;
    
    // Add market1, 2, 3, components
    JButton toggleMarket1;
    JButton toggleMarket2;
    JButton toggleMarket3;
    
    //Timer for weekend scenario
    Timer weekend= new Timer();
    
    // Add bank components
    JButton closeBank1; 
    
    CityGui cityGui;

    /**
     * Constructor for ListPanel.  Sets up all the gui
     *
     * @param rp   reference to the restaurant panel
     * @param type indicates if this is for customers or waiters
     */
    public ControlPanel(CityGui c) {
    	cityGui = c;
    	
        view.setLayout(new FlowLayout());
        setLayout(new BoxLayout((Container) this, BoxLayout.PAGE_AXIS));

        setBorder(BorderFactory.createLineBorder(Color.black, 5));
        
        backButtonPanel.add(backToCity);
    	backToCity.addActionListener(this);
    	backToCity.setEnabled(false);
        add(backButtonPanel);
        
        addPersonSection();
        
        setupWorldControls();
        
        setupBuildingControls();
                
        controlPane.setPreferredSize(panelDim);
        worldControlPanel.setPreferredSize(panelDim);
        backButtonPanel.setPreferredSize(backButtonDim);
        worldControlPanel.setLayout(new BoxLayout(worldControlPanel, BoxLayout.PAGE_AXIS));
        worldControlPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        controlPane.addTab("World", worldControlPanel);
        controlPane.addTab("People", personControlPanel);
        controlPane.addTab("Activity Log", activityPane);
        controlPane.addTab("Buildings", buildingInfoPanel);
        add(controlPane);
        
        //Set up the grids of semaphores
        populateSemaphoreGrids();
      	
        //Creation of houses and apartments
        createHouses();
      	//Creation of bus stops
        createBusStops();
                        
        scenarioSelect.setSelectedIndex(1);
    }
    
    public void addRest1ToCityMap(Restaurant1 r) {
    	cityMap.setRestaurant1(r);
    }
    
    public void addRest3ToCityMap(Restaurant3 r){
    	cityMap.setRestaurant3(r);
    }
    
    public void addRest4ToCityMap(Restaurant4 r) {
    	cityMap.setRestaurant4(r);
    }
    
    public void addRest5ToCityMap(Restaurant5 r) {
    	cityMap.seRestaurant5(r);
    }
    
    public void addMarketToCityMap(Market m, int marketNum) {
    	if (marketNum == 1){
    		cityMap.setMarket1(m);
    	} else if (marketNum == 2){
    		cityMap.setMarket2(m);
    	} else {
    		cityMap.setMarket3(m);
    	}
    }
    
    public void addBankToCityMap(Bank b) {
    	cityMap.setBank(b);
    }
    
    public void addApartment1ToCityMap(ApartmentBuilding b){
    	cityMap.setApartment1(b);
    }
    
    public void addApartment2ToCityMap(ApartmentBuilding b){
    	cityMap.setApartment2(b);
    }
    
    public List<BusStop> getBusStops() {
    	return busStops;
    }
    
    public CityMap getCityMap() {
    	return cityMap;
    }
    
    private void setupBuildingControls(){
    	buildingPanelSelect.addActionListener(this);
        buildingInfoPanel.add(buildingPanelSelect);
        buildingInfoPanel.setPreferredSize(panelDim);
        buildingPanels.add(restaurant1Panel);
        buildingPanels.add(restaurant2Panel);
        buildingPanels.add(restaurant3Panel);
        buildingPanels.add(restaurant4Panel);
        buildingPanels.add(restaurant5Panel);
        buildingPanels.add(market1Panel);
        buildingPanels.add(market2Panel);
        buildingPanels.add(market3Panel);
        buildingPanels.add(bank1Panel);
        
        /*Functions for setting up different building Panels*/
        setupRestaurant1Panel();
        //setupRestaurant2Panel();
        setupRestaurant3Panel();
        setupRestaurant4Panel();
        setupRestaurant5Panel();
        setupMarket1Panel();
        setupMarket2Panel();
        setupMarket3Panel();
        setupBank1Panel();
        
        for(int i = 0; i < buildingPanels.size(); i++){
        	buildingInfoPanel.add(buildingPanels.get(i));
        	buildingPanels.get(i).setVisible(false);
        }
        
    }
    
    private void setupRestaurant1Panel(){
        restaurant1Panel.setPreferredSize(buildingPanelDim);
        restaurant1Panel.setBorder(BorderFactory.createLineBorder(Color.black));
    }
    
    /*
    private void setupRestaurant2Panel(){
        restaurant2Panel.add(new JLabel("Restaurant 2 Info/Options"));
        restaurant2Panel.setPreferredSize(buildingPanelDim);
        restaurant2Panel.setBorder(BorderFactory.createLineBorder(Color.black));
    }
    */
    private void setupRestaurant3Panel(){
    	
        restaurant3Panel.add(new JLabel("Restaurant 3 Info/Options"));
        restaurant3Panel.setPreferredSize(buildingPanelDim);
        restaurant3Panel.setBorder(BorderFactory.createLineBorder(Color.black));
        
        closeRest3 = new JButton("Close Restaurant");
        closeRest3.addActionListener(this);
        restaurant3Panel.add(closeRest3);
        
        emptyRest3 = new JButton("Empty Restaurant Stock");
        emptyRest3.addActionListener(this);
        restaurant3Panel.add(emptyRest3);
        
        fireHost3 = new JButton("Fire Host");
        fireHost3.addActionListener(this);
        restaurant3Panel.add(fireHost3);
        
    }
    
    private void setupRestaurant4Panel(){
        restaurant4Panel.add(new JLabel("    Restaurant 4 Options"));
        restaurant4Panel.setPreferredSize(buildingPanelDim);
        restaurant4Panel.setMaximumSize(buildingPanelDim);
        restaurant4Panel.setMinimumSize(buildingPanelDim);
        restaurant4Panel.setBorder(BorderFactory.createLineBorder(Color.black));
        restaurant4Panel.setLayout(new GridLayout(15,1, 5, 5));
        
        closeRest4= new JButton("Close Restaurant");
        closeRest4.addActionListener(this);
        restaurant4Panel.add(closeRest4);
        
        emptyInventory4= new JButton("Set Inventory to 0");
        emptyInventory4.addActionListener(this);
        restaurant4Panel.add(emptyInventory4);
        
        fireHost4= new JButton("Fire Host");
        fireHost4.addActionListener(this);
        restaurant4Panel.add(fireHost4);
    }
    
    private void setupRestaurant5Panel(){
        restaurant5Panel.add(new JLabel("Restaurant 5 Info/Options"));
        restaurant5Panel.setPreferredSize(buildingPanelDim);
        restaurant5Panel.setBorder(BorderFactory.createLineBorder(Color.black));
        
        closeRest5 = new JButton("Close Restaurant5");
        closeRest5.addActionListener(this);
        restaurant5Panel.add(closeRest5);
        
        emptyInventory5 = new JButton("Empty Restaurant Stock");
        emptyInventory5.addActionListener(this);
        restaurant5Panel.add(emptyInventory5);
        
        
    }
    
    private void setupMarket1Panel(){
        market1Panel.add(new JLabel("Market 1 Info/Options"));
        market1Panel.setPreferredSize(buildingPanelDim);
        market1Panel.setBorder(BorderFactory.createLineBorder(Color.black));
        
        toggleMarket1 = new JButton("Close Market");
        toggleMarket1.addActionListener(this);
        market1Panel.add(toggleMarket1);
        
    }
    
    private void setupMarket2Panel(){
        market2Panel.add(new JLabel("Market 2 Info/Options"));
        market2Panel.setPreferredSize(buildingPanelDim);
        market2Panel.setBorder(BorderFactory.createLineBorder(Color.black));
        
        toggleMarket2 = new JButton("Close Market");
        toggleMarket2.addActionListener(this);
        market2Panel.add(toggleMarket2);
        
    }
    
    private void setupMarket3Panel(){
        market3Panel.add(new JLabel("Market 3 Info/Options"));
        market3Panel.setPreferredSize(buildingPanelDim);
        market3Panel.setBorder(BorderFactory.createLineBorder(Color.black));
        
        toggleMarket3 = new JButton("Close Market");
        toggleMarket3.addActionListener(this);
        market3Panel.add(toggleMarket3);
    }
    
    private void setupBank1Panel(){
        bank1Panel.add(new JLabel("Bank 1 Info/Options"));
        bank1Panel.setPreferredSize(buildingPanelDim);
        bank1Panel.setBorder(BorderFactory.createLineBorder(Color.black));
        
        closeBank1 = new JButton("Close Bank1");
        closeBank1.addActionListener(this);
        bank1Panel.add(closeBank1);
        
    
        
    }
    
    private void setupWorldControls(){
    	
    	// Scenario selection
    	Dimension dropDownSize = new Dimension(WINDOWX, 30);
    	startScenario.addActionListener(this);
    	scenarioSelect.addActionListener(this);
    	scenarioSelect.setPreferredSize(dropDownSize);
    	scenarioSelect.setMaximumSize(dropDownSize);
    	
    	// Manual timer Controls
    	Dimension timerControlDropdownSize = new Dimension(70, 30);
    	changeTime.addActionListener(this);
    	
    	// Hour dropdown
    	hourSelect.addActionListener(this);
    	hourSelect.setPreferredSize(timerControlDropdownSize);
    	hourSelect.setMaximumSize(timerControlDropdownSize);
    	
    	// Minute Dropdown
    	minuteSelect.addActionListener(this);
    	minuteSelect.setPreferredSize(timerControlDropdownSize);
    	minuteSelect.setMaximumSize(timerControlDropdownSize);
    	
    	// am/pm Select Dropdown
    	amPmSelect.addActionListener(this);
    	amPmSelect.setPreferredSize(timerControlDropdownSize);
    	amPmSelect.setMaximumSize(timerControlDropdownSize);
    	
    	// Add all to single panel
    	changeTime.setEnabled(false);
    	timeSelectionPanel.add(hourSelect);
    	//timeSelectionPanel.add(minuteSelect);
    	timeSelectionPanel.add(amPmSelect);
    	timeSelectionPanel.add(Box.createVerticalStrut(1));
    	timeSelectionPanel.add(changeTime);
    	
    	//This add(Box) function creates a space on the JPanel - using it here for spacing the buttons out to look nice
    	worldControlPanel.add(Box.createVerticalStrut(10));
    	clickBuildings.setFont(new Font("Trebuchet", Font.BOLD, 14));
    	worldControlPanel.add(clickBuildings);
    	worldControlPanel.add(Box.createVerticalStrut(10));
    	JLabel title = new JLabel("Running a scenario: ");
    	title.setAlignmentX(Component.CENTER_ALIGNMENT);
    	worldControlPanel.add(title);
    	worldControlPanel.add(scenarioSelect);
    	clickBuildings.setAlignmentX(Component.CENTER_ALIGNMENT);
    	backToCity.setAlignmentX(Component.CENTER_ALIGNMENT);
    	worldControlPanel.add(Box.createVerticalStrut(10));
    	worldControlPanel.add(startScenario);
    	startScenario.setAlignmentX(Component.CENTER_ALIGNMENT);
    	worldControlPanel.add(Box.createVerticalStrut(10));
    	worldControlPanel.add(timeDisplay);
    	timeDisplay.setAlignmentX(Component.CENTER_ALIGNMENT);
    	worldControlPanel.add(timeSelectionPanel);


    	carCrash.setAlignmentX(Component.CENTER_ALIGNMENT);
    	//worldControlPanel.add(Box.createVerticalStrut());
        worldControlPanel.add(carCrash);
        carCrash.addActionListener(this);
        
    	hitAndRun.setAlignmentX(Component.CENTER_ALIGNMENT);
    	//worldControlPanel.add(Box.createVerticalStrut());
        worldControlPanel.add(hitAndRun);
        hitAndRun.addActionListener(this);
    }
    
    private void addPersonSection(){
    	//personControls.add(new JLabel("<html><br><u>Add People</u><br></html>"));
    	
    	//addPerson.setAlignmentX(Component.CENTER_ALIGNMENT);
    	
    	personControlPanel.setPreferredSize(panelDim);
    	
    	addPerson.setPreferredSize(addPersonDim);
    	infoPanel.setPreferredSize(infoPanelDim);
    	personOptionsDisplay.setPreferredSize(personOptionsDim);
        pane.setViewportView(view);
        
        infoPanel.add(new JLabel("List of people in SimCity"));
    	infoPanel.add(pane);
    	
    	//Add AddPerson panel and info panel to main panel
    	FlowLayout controlsFlow = new FlowLayout();
    	personControlPanel.setLayout(controlsFlow);
    	personControlPanel.add(addPerson, controlsFlow);
    	//personControlPanel.add(personOptionsDisplay, controlsFlow);
    	personControlPanel.add(infoPanel, controlsFlow);
    	personControlPanel.add(personOptionsDisplay, controlsFlow);
    	
    	personOptionsDisplay.setBorder(BorderFactory.createLineBorder(Color.black));
    	
    	buyCarButton.addActionListener(this);
    	personOptionsDisplay.add(new JLabel("Person Options"));
		addPerson.add(Box.createVerticalStrut(10));
		//personOptionsDisplay.add(buyCarButton);

		pane.setMinimumSize(scrollDim);
		pane.setMaximumSize(scrollDim);
		pane.setPreferredSize(scrollDim);

		//set layout of control panel
		FlowLayout flow = new FlowLayout();
		addPerson.setLayout(new BoxLayout(addPerson, BoxLayout.PAGE_AXIS));

		//Adding enter name section
		addPerson.add(new JLabel("Name:"));
		nameField = new JTextField();
		nameField.setColumns(16);
		addPerson.add(nameField, flow);

		//Adding enter job section
		addPerson.add(new JLabel("Job: "));

		addPerson.add(jobField, flow);

		isHungry = new JCheckBox("Hungry?");
		isHungry.setEnabled(false);
		isHungry.addActionListener(this);
		//enterNames.add(isHungry);

		nameField.addKeyListener(new KeyListener(){

			@Override
			public void keyPressed(KeyEvent arg0) {
				if(nameField.getText().length() > 0){
					isHungry.setEnabled(true);
				}
				else isHungry.setEnabled(false);
			}
			@Override
			public void keyReleased(KeyEvent arg0) {
				if(nameField.getText().length() > 0){
					isHungry.setEnabled(true);
				}
				else isHungry.setEnabled(false);
			}
			@Override
			public void keyTyped(KeyEvent arg0) {
				if(nameField.getText().length() > 0){
					isHungry.setEnabled(true);
				}
				else{
					isHungry.setEnabled(false);
				}
			}

		});

		addPersonButton.addActionListener(this);
		addPerson.add(Box.createVerticalStrut(10));
		addPerson.add(addPersonButton, flow);
		addPerson.add(Box.createVerticalStrut(10));

		errorDisplay.setEditable(false);
		addPerson.add(errorDisplay, flow);

		view.setLayout(new BoxLayout((Container) view, BoxLayout.Y_AXIS));
		this.add(personControlPanel);
	}


	/**
	 * Method from the ActionListener interface.
	 * Handles the event of the add button being pressed
	 */
	public void actionPerformed(ActionEvent e) {
		if(e.getSource() == buildingPanelSelect){
			for(JPanel p : buildingPanels){
				p.setVisible(false);
			}
			if(buildingPanelSelect.getSelectedItem().equals("Restaurant1")){
				restaurant1Panel.setVisible(true);
			}
			if(buildingPanelSelect.getSelectedItem().equals("Restaurant2")){
				restaurant2Panel.setVisible(true);
			}
			if(buildingPanelSelect.getSelectedItem().equals("Restaurant3")){
				restaurant3Panel.setVisible(true);
			}
			if(buildingPanelSelect.getSelectedItem().equals("Restaurant4")){
				restaurant4Panel.setVisible(true);
			}
			if(buildingPanelSelect.getSelectedItem().equals("Restaurant5")){
				restaurant5Panel.setVisible(true);
			}
			if(buildingPanelSelect.getSelectedItem().equals("Market1")){
				market1Panel.setVisible(true);
			}
			if(buildingPanelSelect.getSelectedItem().equals("Market2")){
				market2Panel.setVisible(true);
			}
			if(buildingPanelSelect.getSelectedItem().equals("Market3")){
				market3Panel.setVisible(true);
			}
			if(buildingPanelSelect.getSelectedItem().equals("Bank1")){
				bank1Panel.setVisible(true);
			}
			
		}
		else if (e.getSource() == addPersonButton) {
			// Chapter 2.19 describes showInputDialog()
			if(!nameField.getText().equals("")){
				String job = null;
				if(jobField.getSelectedIndex() == 0){
					errorDisplay.setText("Please select a job");
				}
				else{
					errorDisplay.setText("");
					job = (String)jobField.getSelectedItem();
					addPerson(nameField.getText(), job);
					nameField.setText("");
					isHungry.setSelected(false);
				}
			}
			else{
				errorDisplay.setText("Please enter a name for the person");
			}
		}
		else if(e.getSource() == startScenario){
			if(scenarioSelect.getSelectedIndex() != 0){
				populateCity((String)scenarioSelect.getSelectedItem());
				cityGui.startMasterClock();
				startScenario.setEnabled(false);
				changeTime.setEnabled(true);
			}
		}
		else if(e.getSource() == backToCity) {
			cityGui.backToCityView();
			backToCity.setEnabled(false);
		}
		else if(e.getSource() == changeTime) {

			cityGui.setTime(hourSelect.getSelectedItem().toString(), minuteSelect.getSelectedItem().toString(), amPmSelect.getSelectedItem().toString());

		} else if(e.getSource() == buyCarButton){
			// Coming soon
		}
		else if(e.getSource() == carCrash) {
			runCarCrash();
			carCrash.setText("Please wait...");
			carCrash.setEnabled(false);
			
			timer.schedule(new TimerTask() {
				public void run() {
					carCrash.setEnabled(true);
					carCrash.setText("Car Crash");
				}
			}, 25000);
		}
		else if(e.getSource() == hitAndRun) {
			hitAndRun();
			hitAndRun.setText("Please wait...");
			hitAndRun.setEnabled(false);
			
			timer.schedule(new TimerTask() {
				public void run() {
					hitAndRun.setEnabled(true);
					hitAndRun.setText("Hit and Run");
				}
			}, 22000);
		} else if(e.getSource() == closeRest4){
			if(cityMap.getRest4().isOpen()){
				closeRest4.setText("Open Restaurant");
			}
			else{
				closeRest4.setText("Close Restaurant");
			}
			cityMap.getRest4().close();
		} else if(e.getSource() == emptyInventory4){
			
			cityMap.getRest4().emptyInventory();
			
		} else if(e.getSource() == closeRest3){
			
			if(cityMap.getRest3().isOpen()){
				closeRest3.setText("Open Restaurant");
			} else{
				closeRest3.setText("Close Restaurant");
			}
			cityMap.getRest3().toggleOpen();
			
		} else if(e.getSource() == emptyRest3){
			cityMap.getRest3().emptyStock();
		} else if(e.getSource() == toggleMarket1){
			
			if(cityMap.getMark1().isOpen()){
				toggleMarket1.setText("Open Market");
			} else{
				toggleMarket1.setText("Close Market");
			}
			cityMap.getMark1().toggleOpen();
		} else if(e.getSource() == toggleMarket2){
			
			if(cityMap.getMark2().isOpen()){
				toggleMarket2.setText("Open Market");
			} else{
				toggleMarket2.setText("Close Market");
			}
			cityMap.getMark2().toggleOpen();
			
		} else if(e.getSource() == toggleMarket3){
			
			if(cityMap.getMark3().isOpen()){
				toggleMarket3.setText("Open Market");
			} else{
				toggleMarket3.setText("Close Market");
			}
			cityMap.getMark3().toggleOpen();
			
		} else if(e.getSource() == closeRest5){
			
			if(cityMap.getRest5().isOpen()){
				closeRest5.setText("Open Restaurant 5");
			} else{
				closeRest5.setText("Close Restaurant 5");
			}
			cityMap.getRest5().toggleOpen();
			
		} else if(e.getSource() == emptyInventory5){
			cityMap.getRest5().emptyStock();
		}

		else if(e.getSource() == closeBank1){
			
			if(cityMap.getBank().isOpen()){
				closeBank1.setText("Open Bank 1");
			} else{
				closeBank1.setText("Close Bank 1");
			}
			cityMap.getBank().toggleOpen();
		}
		else if(e.getSource() == fireHost4){
			cityMap.getRest4().fireHost();
			addPerson("New Host4", "No job");
			fireHost4.setEnabled(false);
		} else if(e.getSource() == fireHost3){
			cityMap.getRest3().fireHost();
			addPerson("New Host3", "No job");
			fireHost3.setEnabled(false);
		}
		
	}

	/**
	 * If the add button is pressed, this function creates
	 * a spot for it in the scroll pane, and tells the restaurant panel
	 * to add a new person.
	 *
	 * @param name name of new person
	 */
	public void addPerson(String name, String job) {
		if (name != null) {
			JButton button = new JButton(name);
			button.setBackground(Color.white);

			AStarTraversal aStarTraversal = new AStarTraversal(sidewalkGrid);

			House house = houses.get(houseAssignmentNumber);
			if(houseAssignmentNumber == 62){
				houseAssignmentNumber = 61;
			}
			else{
				houseAssignmentNumber++;
			}

			cityGui.addPerson(name, aStarTraversal, job, cityMap, house, null);
			System.out.println("Adding person " + name + " with job " + job);

			Dimension paneSize = pane.getSize();
			Dimension buttonSize = new Dimension((paneSize.width - 20),
					(int) (paneSize.height / 7));
			button.setPreferredSize(buttonSize);
			button.setMinimumSize(buttonSize);
			button.setMaximumSize(buttonSize);
			button.addActionListener(this);
			personButtonList.add(button);
			view.add(button);


			isHungry.setEnabled(false);
			validate();
		}
	}

	public void addPersonNoHouse(String name, String job) {
		if (name != null) {
			JButton button = new JButton(name);
			button.setBackground(Color.white);

			AStarTraversal aStarTraversal = new AStarTraversal(sidewalkGrid);

			cityGui.addPerson(name, aStarTraversal, job, cityMap, null, null);
			System.out.println("Adding person " + name + " with job " + job);

			Dimension paneSize = pane.getSize();
			Dimension buttonSize = new Dimension((paneSize.width - 20),
					(int) (paneSize.height / 7));
			button.setPreferredSize(buttonSize);
			button.setMinimumSize(buttonSize);
			button.setMaximumSize(buttonSize);
			button.addActionListener(this);
			personButtonList.add(button);
			view.add(button);
			isHungry.setEnabled(false);
			validate();
		}
	}

	public void addPersonWithCar(String name, String job) {
		if (name != null) {
			JButton button = new JButton(name);
			button.setBackground(Color.white);

			AStarTraversal streetTraversal = new AStarTraversal(streetGrid);              
			CarAgent car = new CarAgent(streetTraversal, cityMap);
			House house = houses.get(houseAssignmentNumber);
			if(houseAssignmentNumber == 62){
				houseAssignmentNumber = 61;
			}
			else{
				houseAssignmentNumber++;
			}

			AStarTraversal aStarTraversal = new AStarTraversal(sidewalkGrid);

			cityGui.addPerson(name, aStarTraversal, job, cityMap, house, car);
			System.out.println("Adding person " + name + " with job " + job);

			Dimension paneSize = pane.getSize();
			Dimension buttonSize = new Dimension((paneSize.width - 20),
					(int) (paneSize.height / 7));
			button.setPreferredSize(buttonSize);
			button.setMinimumSize(buttonSize);
			button.setMaximumSize(buttonSize);
			button.addActionListener(this);
			personButtonList.add(button);
			view.add(button);
			isHungry.setEnabled(false);
			validate();
		}
	}

	public void addVehicle(String type) {
		AStarTraversal aStarTraversal = new AStarTraversal(streetGrid); //Create new aStarTraversal using streetGrid instead of sidewalkGrid

		cityGui.addVehicle(type, aStarTraversal);
	}

	private void populateSemaphoreGrids() {

		/*********Setting up semaphore grid***********/
		for (int i = 0; i <= gridX; i++) {
			for (int j = 0; j <= gridY; j++) {
				streetGrid[i][j] = new Semaphore(0,true);
				sidewalkGrid[i][j] = new Semaphore(0,true);
			}
		}

		//Releasing all roads and sidewalks so guis can move around on them.
		//First, the roads
		for(int i = 3; i < 19; i++) { //Top and bottom portions of road
			for(int j = 3; j < 7; j++)
				streetGrid[i][j].release();
			for(int j = 12; j < 16; j++)
				streetGrid[i][j].release();
		}
		for(int i = 7; i < 12; i++) { //Left and right portions of road
			for(int j = 3; j < 7; j++)
				streetGrid[j][i].release();
			for(int j = 15; j < 19; j++)
				streetGrid[j][i].release();
		}

		for(int i = 15; i < 19; i++) //Extra portions of road in bottom right corner
			for(int j = 16; j < 21; j++)
				streetGrid[i][j].release();
		for(int i =  19; i < 26; i++)
			for(int j = 12; j < 16; j++)
				streetGrid[i][j].release();

		//Release sidewalk semaphores
		for(int i = 1; i < 21; i++) //Top sidewalk
			for(int j = 1; j < 3; j++)
				sidewalkGrid[i][j].release();

		for(int i = 1; i < 15; i++) //Bottom sidewalk
			for(int j = 16; j < 18; j++)
				sidewalkGrid[i][j].release();

		for(int i = 3; i < 16; i++) //Left sidewalk
			for(int j = 1; j < 3; j++)
				sidewalkGrid[j][i].release();

		for(int i = 3; i < 12; i++) //Right sidewalk
			for(int j = 19; j < 21; j++)
				sidewalkGrid[j][i].release();

		for(int i = 21; i < 25; i++) { //Two rightmost chunks of sidewalk
			for(int j = 10; j < 12; j++) 
				sidewalkGrid[i][j].release();
			for(int j = 16; j < 18; j++)
				sidewalkGrid[i][j].release();
		}

		for(int i = 19; i < 22; i++) //Sidewalk below right crosswalk
			for(int j = 16; j < 21; j++)
				sidewalkGrid[i][j].release();

		for(int i = 13; i < 15; i++) //Sidewalk at bottom under left crosswalk
			for(int j = 19; j < 21; j++)
				sidewalkGrid[i][j].release();

		for(int i = 7; i < 15; i++) //Island sidewalk
			for(int j = 10; j < 12; j++)
				sidewalkGrid[i][j].release();

		//End of sidewalk grid releasing

		//Adding in crosswalks (shared semaphores between street grid and sidewalk grid)
		for(int i = 15; i < 19; i++) //Bottom crosswalk
			for(int j = 16; j < 18; j++)
				sidewalkGrid[i][j] = streetGrid[i][j];
		for(int i = 19; i < 21; i++) //Right crosswalk
			for(int j = 12; j < 16; j++)
				sidewalkGrid[i][j] = streetGrid[i][j];
		//These crosswalks are not used anymore.
		/*for(int i = 13; i < 15; i++) //Left crosswalk
      		for(int j = 12; j < 16; j++)
      			sidewalkGrid[i][j] = streetGrid[i][j];
      	for(int i = 15; i < 19; i++) //Top crosswalk
      		for(int j = 10; j < 12; j++)
      			sidewalkGrid[i][j] = streetGrid[i][j];*/

		//Releasing many semaphores on building entrances so multiple guis can "go in" to buildings
		sidewalkGrid[20][0].release(100); //rest1
		sidewalkGrid[0][3].release(100); //rest2
		sidewalkGrid[0][17].release(100); //rest3
		sidewalkGrid[10][18].release(100); //rest4
		sidewalkGrid[21][18].release(100); //rest5
		sidewalkGrid[24][10].release(100); //mark1
		sidewalkGrid[5][0].release(100); //mark2
		sidewalkGrid[5][18].release(100); //mark3
		sidewalkGrid[21][1].release(100); //bank1
		sidewalkGrid[0][12].release(100); //bank2
		sidewalkGrid[21][4].release(100); //apart1
		sidewalkGrid[21][3].release(20); //these two lines open up spots if multiple people are leaving apartment
		sidewalkGrid[21][5].release(20);
		sidewalkGrid[1][18].release(100); //apart2
		sidewalkGrid[0][18].release(20); //these two lines open up spots if multiple people are leaving apartment
		sidewalkGrid[2][18].release(20);
		
		sidewalkGrid[21][8].release(100); //stop0
		sidewalkGrid[21][9].release(20);
		
		sidewalkGrid[11][0].release(100); //stop1
		sidewalkGrid[10][0].release(20);
		
		sidewalkGrid[0][8].release(100); //stop2
		sidewalkGrid[0][9].release(20);
		
		sidewalkGrid[7][18].release(100); //stop3
		sidewalkGrid[8][18].release(20);

		sidewalkGrid[20][18].release(100); //starting point for agents
		sidewalkGrid[21][18].release(100);
		sidewalkGrid[19][18].release(100);

		sidewalkGrid[21][20].release(5); //opening up permits in front of people's houses
		sidewalkGrid[24][17].release(5);
		sidewalkGrid[25][17].release(5);
		sidewalkGrid[21][6].release(5);
		sidewalkGrid[21][2].release(5);
		sidewalkGrid[19][0].release(5);
		sidewalkGrid[17][0].release(5);
		sidewalkGrid[15][0].release(5);
		sidewalkGrid[13][0].release(5);
		sidewalkGrid[9][0].release(5);
		sidewalkGrid[7][0].release(5);
		sidewalkGrid[3][0].release(5);
		sidewalkGrid[1][0].release(5);
		sidewalkGrid[0][0].release(5);
		sidewalkGrid[0][4].release(5);
		sidewalkGrid[0][6].release(5);
		sidewalkGrid[0][10].release(5);
		sidewalkGrid[0][14].release(5);
		sidewalkGrid[0][16].release(5);
		sidewalkGrid[3][18].release(5);
		sidewalkGrid[4][18].release(5);
		sidewalkGrid[11][18].release(5);      	

		streetGrid[17][20].release(100); //starting point for vehicles
		streetGrid[25][14].release(20);

		streetGrid[7][9].release(100); //Parking entrances
		streetGrid[14][9].release(100); 
		streetGrid[10][7].release(100); 
		streetGrid[11][11].release(100); 

		/********Finished setting up semaphore grid***********/
	}
	
	public Semaphore[][] getSidewalkGrid() {
		return sidewalkGrid;
	}

	private void createBusStops() {
		for(int i = 0; i < 4; i++) {
			busStops.add(new BusStopAgent(i));
			cityMap.addBusStop(busStops.get(i));
			BusStopAgent newBus = (BusStopAgent)busStops.get(i);
			newBus.startThread();
		}
	}

	private void createHouses() {
		for(int i = 0; i < 22; i++) {
			houses.add(new House("house" + Integer.toString(i + 1)));
		}
		for(int i = 0; i < 20; i++) {
			Apartment a= new Apartment("apart1 " + i, i);
			a.setRoom(i);
			a.setBuilding(1);
			houses.add(a);
		}
		for(int i = 0; i < 20; i++) {
			Apartment a= new Apartment("apart2 " + i, i);
			a.setRoom(i);
			a.setBuilding(2);
			houses.add(a);
		}
		System.out.println("Created " + houses.size() + " houses including apartments");
	}

	public List<House> getHouses(){
		return houses; 
	}

	public void populateCity(String scenario){
		/*
		 * This will call different functions based on which scenario was chosen
		 */
		if(scenario.equals("Full Scenario"))
			runFullTest();
		else if(scenario.equals("The Weekender"))
			runTheWeekenderTest();
		else if(scenario.equals("Trader Joe's"))
			runMarketVisitTest();
		else if(scenario.equals("Restaurant1"))
			runRestaurant1Test();
		else if(scenario.equals("Restaurant2"))
			runRestaurant2Test();
		else if (scenario.equals("Restaurant3"))
			runRestaurant3Test();
		else if(scenario.equals("Restaurant4"))
			runRestaurant4Test();
		else if(scenario.equals("Restaurant5"))
			runRestaurant5Test();
		else if(scenario.equals("Close Restaurants Test"))
			runCloseRestaurantsTest();
		else if(scenario.equals("Home Meal/Visit Stores Test"))
			runEatAtHomeVisitWorkplacesTest();
		else if(scenario.equals("Bank Test"))
			runBankTest();
		else if(scenario.equals("Landlord Test"))
			runLandlordTest();
		else if(scenario.equals("Market Truck Test"))
			runMarketTruckTest();
		else if(scenario.equals("Traffic Test"))
			runTrafficTest();
	}

	public void runFullTest(){
		
		createInitialPeople();

		addPersonWithCar("rest2Test", "No job");

		addPerson("rest2Test", "No job");

		addPerson("joe", "No Job");

		addPerson("rest1Test", "No job");
		addPersonWithCar("rest1Test", "No job");

		addPerson("rest4Test", "No job");
		addPersonWithCar("rest4Test", "No job");
	
		addPerson("rest5Test", "No job");
		addPersonWithCar("rest5Test", "No job");
		
		addPerson("rest3Test", "No job");
		addPerson("rest3Test", "No job");
	}
	
	
	public void runTheWeekenderTest(){
		weekend.schedule(new TimerTask() {
			@Override public void run() {
				System.out.println("THE TIMER IS DONE, DAY SHOULD BE SATURDAY NOW");
				cityGui.getClock().setDay(6);
			}}, 500);
		
		//Initial public transportation creation.
		addVehicle("bus");
		timer.schedule(new TimerTask() {
			public void run() {
				addVehicle("bus");
			}
		}, 18000);
				
		//Rest1
		addPerson("host1", "Restaurant1 Host");
		addPerson("cashier1", "Restaurant1 Cashier");
		addPerson("cook1", "Restaurant1 Cook");
		addPerson("waiter1", "Restaurant1 Waiter");
		
		//Rest3
		addPerson("host3", "Restaurant3 Host");
		addPerson("cashier3", "Restaurant3 Cashier");
		addPerson("cook3", "Restaurant3 Cook");
		addPerson("waiter3", "Restaurant3 Waiter");
		
		//Rest4 (this will be closed but these people should not go to work
		addPerson("host4", "Restaurant4 Host");
		addPerson("cashier4", "Restaurant4 Cashier");
		addPerson("cook4", "Restaurant4 Cook");
		addPerson("sharedWaiter4", "Restaurant4 SharedDataWaiter");
		
		//Rest5
		addPerson("host5", "Restaurant5 Host");
		addPerson("cashier5", "Restaurant5 Cashier");
		addPerson("cook5", "Restaurant5 Cook");
		addPerson("waiter5", "Restaurant5 Waiter");
		addPerson("waiter5", "Restaurant5 Waiter");
		
		//People
		addPerson("George", "No job");
		addPerson("Gina", "No job");
		addPerson("Greg", "No job");
		addPerson("Ivan", "No job");
		addPerson("Irina", "No job");
		addPerson("Ian", "No job");
		addPerson("Reggie", "No job");
		addPerson("Rachel", "No job");
		addPerson("Rebecca", "No job");
		
		//Tests to show people can't go to rest4, rest2, or the bank because they are closed
		addPerson("rest2Test", "No job");
		addPerson("rest4Test", "No job");
		addPerson("bankCustomerTest", "No job");
		
		populateBanksAndMarkets();
		
		//TODO add close bank functionality and close it for the weekend
		cityMap.getBank().toggleOpen();
		cityMap.getRest4().close();
		cityMap.getRest2().closeRestaurant();
	}

	public void runRestaurant1Test(){
		//Initial public transportation creation.
		addVehicle("bus");
		timer.schedule(new TimerTask() {
			public void run() {
				addVehicle("bus");
			}
		}, 18000);
		
		populateBanksAndMarkets();
		
		addPerson("host1", "Restaurant1 Host");
		addPerson("cashier1", "Restaurant1 Cashier");
		addPerson("cook1", "Restaurant1 Cook");
		addPerson("waiter1", "Restaurant1 Waiter");

		addPerson("rest1Test", "No job");
		addPerson("rest1Test", "No job");
		addPersonWithCar("rest1Test", "No job");
		addPersonWithCar("rest1Test", "No job");
		addPersonWithCar("rest1Test", "No job");
		addPersonWithCar("rest1Test", "No job");
		addPerson("rest1Test", "No job");
		addPerson("rest1Test", "No job");
	}

	public void runRestaurant2Test(){
		//Initial public transportation creation.
		addVehicle("bus");
		timer.schedule(new TimerTask() {
			public void run() {
				addVehicle("bus");
			}
		}, 18000);		

		addPerson("rest2Test", "No job");
		populateBanksAndMarkets();
		addPersonWithCar("rest2Test", "No job");
		addPerson("host2", "Restaurant2 Host");
		addPerson("cashier2", "Restaurant2 Cashier");
		addPerson("cook2", "Restaurant2 Cook");
		addPerson("waiter2", "Restaurant2 Waiter");
		
		//addPerson("rest2Test", "No job");
		addPerson("waiter2 shared data", "Restaurant2 WaiterSharedData");
	}

	public void runRestaurant3Test(){
		//Initial public transportation creation.
		addVehicle("bus");
		timer.schedule(new TimerTask() {
			public void run() {
				addVehicle("bus");
			}
		}, 18000);

		addPerson("host3", "Restaurant3 Host");
		addPerson("cashier3", "Restaurant3 Cashier");
		addPerson("cook3", "Restaurant3 Cook");
		addPerson("waiter3", "Restaurant3 Waiter");
		
		addPerson("rest3Test", "No job");
		
		populateBanksAndMarkets();
	}

	public void runRestaurant4Test(){
		//Initial public transportation creation.
		addVehicle("bus");
		timer.schedule(new TimerTask() {
			public void run() {
				addVehicle("bus");
			}
		}, 18000);

		addPerson("host4", "Restaurant4 Host");
		addPerson("cashier4", "Restaurant4 Cashier");
		addPerson("cook4", "Restaurant4 Cook");
		addPerson("regularWaiter4", "Restaurant4 RegularWaiter");

		addPerson("sharedWaiter4", "Restaurant4 SharedDataWaiter");
		addPerson("restTest", "No job");
		addPerson("rest4Test", "No job");
		addPerson("rest4Test", "No job");
		addPerson("rest4Test", "No job");
		
		populateBanksAndMarkets();
	}

	public void runRestaurant5Test(){
		//Initial public transportation creation.
		addVehicle("bus");
		timer.schedule(new TimerTask() {
			public void run() {
				addVehicle("bus");
			}
		}, 18000);

		addPerson("host5", "Restaurant5 Host");
		addPerson("cashier5", "Restaurant5 Cashier");
		addPerson("cook5", "Restaurant5 Cook");
		addPerson("waiter5", "Restaurant5 Waiter");
		addPerson("waiter5", "Restaurant5 Waiter");
		//addPerson("rest5Test", "No job");
		addPerson("rest5Test", "No job");
		addPerson("rest5Test", "No job");
		addPerson("rest5Test", "No job");
		populateBanksAndMarkets();
	}
	
	public void runCloseRestaurantsTest(){
		//Initial public transportation creation.
				addVehicle("bus");
				timer.schedule(new TimerTask() {
					public void run() {
						addVehicle("bus");
					}
				}, 18000);
		
		//Populate rest1
		addPerson("host1", "Restaurant1 Host");
		addPerson("cashier1", "Restaurant1 Cashier");
		addPerson("cook1", "Restaurant1 Cook");
		addPerson("waiter1", "Restaurant1 Waiter");
		addPerson("rest1Test", "No job");
		
		//Populate rest2
		addPerson("host2", "Restaurant2 Host");
		addPerson("cashier2", "Restaurant2 Cashier");
		addPerson("cook2", "Restaurant2 Cook");
		addPerson("waiter2", "Restaurant2 Waiter");
		addPersonWithCar("rest2Test", "No job");
		
		//Populate rest3
		addPerson("host3", "Restaurant3 Host");
		addPerson("cashier3", "Restaurant3 Cashier");
		addPerson("cook3", "Restaurant3 Cook");
		addPerson("waiter3", "Restaurant3 Waiter");
		addPerson("rest3Test", "No job");
		
		//Populate rest4
		addPerson("host4", "Restaurant4 Host");
		addPerson("cashier4", "Restaurant4 Cashier");
		addPerson("cook4", "Restaurant4 Cook");
		addPerson("regularWaiter4", "Restaurant4 RegularWaiter");
		addPerson("restTest", "No job");
		
		//Populate rest5
		addPerson("host5", "Restaurant5 Host");
		addPerson("cashier5", "Restaurant5 Cashier");
		addPerson("cook5", "Restaurant5 Cook");
		addPerson("waiter5", "Restaurant5 Waiter");
		addPerson("rest5Test", "No job");
		
		//Add generic people who will pick a new rest if their first choice is closed
		addPerson("restTest", "No job");
		addPerson("restTest", "No job");
		addPerson("restTest", "No job");
		addPerson("restTest", "No job");
		addPerson("restTest", "No job");
	}

	public void runEatAtHomeVisitWorkplacesTest(){
		//Initial public transportation creation.
		addVehicle("bus");
		timer.schedule(new TimerTask() {
			public void run() {
				addVehicle("bus");
			}
		}, 18000);
		
		addPersonWithCar("Chris", "No job");
		addPerson("Steph", "No job");
		addPerson("Carla", "No job");
		populateBanksAndMarkets();
	}
	
	public void runTrafficTest() {
		//Initial public transportation creation.
		addVehicle("bus");
		timer.schedule(new TimerTask() {
			public void run() {
				addVehicle("bus");
			}
		}, 18000);

		addPersonWithCar("bank manager", "Bank Manager");
		addPersonWithCar("bank manager", "Bank Manager");
		addPersonWithCar("marketManager", "Market Manager1");
		addPersonWithCar("marketManager", "Market Manager2");
		addPersonWithCar("marketManager", "Market Manager3");
		
		addPersonWithCar("host1", "Restaurant1 Host");
		addPersonWithCar("host2", "Restaurant2 Host");
	}
	
	public void runBankTest() {
		//Initial public transportation creation.
		addVehicle("bus");
		timer.schedule(new TimerTask() {
			public void run() {
				addVehicle("bus");
			}
		}, 18000);

		addPerson("bank manager", "Bank Manager");
		addPerson("bank teller", "Bank Teller");
		addPerson("bank teller", "Bank Teller");
		addPerson("bank teller", "Bank Teller");
		
		addPerson("bankCustomerTest", "No Job");
		addPerson("bankCustomerTest1", "No job");
		//addPerson("bankCustomerTest", "No job");
		addPerson("bankCustomerTest2", "No job");
		//addPerson("bankCustomerTest3", "No job");
		addPerson("bankCustomerTest4", "No job");
		addPerson("bankRobber", "No job");
		

	}

	public void runRegularJoeTest(){
		//Initial public transportation creation.
		addVehicle("bus");
		timer.schedule(new TimerTask() {
			public void run() {
				addVehicle("bus");
			}
		}, 18000);
		addPerson("joe", "No Job");
	}

	public void runMarketVisitTest(){
		//Initial public transportation creation.
		addVehicle("bus");
		timer.schedule(new TimerTask() {
			public void run() {
				addVehicle("bus");
			}
		}, 18000);

		addPersonWithCar("marketManager", "Market Manager1");
		addPerson("marketWorker", "Market Worker1");
		addPersonWithCar("marketManager", "Market Manager2");
		addPerson("marketWorker", "Market Worker2");
		addPersonWithCar("marketManager", "Market Manager3");
		addPerson("marketWorker", "Market Worker3");
		
		addPerson("marketClient", "No Job");

		//addPerson("marketWorker", "Market Worker");
		//addPerson("marketWorker", "Market Worker");
		//addPerson("marketWorker", "Market Worker");
		//addPerson("marketWorker", "Market Worker");

	}

	public void runMarketTruckTest() {
		//Initial public transportation creation.
		addVehicle("bus");
		timer.schedule(new TimerTask() {
			public void run() {
				addVehicle("bus");
			}
		}, 18000);
		
		addPerson("host1", "Restaurant1 Host");
		addPerson("cashier1", "Restaurant1 Cashier");
		addPerson("cook1", "Restaurant1 Cook");
		addPerson("waiter1", "Restaurant1 Waiter");

		addPerson("rest1Test", "No job");

		addPerson("marketManager", "Market Manager");
		addPerson("marketWorker", "Market Worker");
		addPerson("marketManager", "Market Manager");
		addPerson("marketWorker", "Market Worker");
		addPerson("marketManager", "Market Manager");
		addPerson("marketWorker", "Market Worker");
		
		addVehicle("truck");
		addVehicle("truck");
		addVehicle("truck");
	}

	public void runCarCrash() {
		addVehicle("crash");
	}
	
	public void hitAndRun() {
		addVehicle("hitAndRun");
	}

	public void runLandlordTest(){
		//Initial public transportation creation.
		addVehicle("bus");
		timer.schedule(new TimerTask() {
			public void run() {
				addVehicle("bus");
			}
		}, 18000);

		addPerson("Joe", "No Job");
		addPerson("Jenny", "No Job");
		addPerson("Jake", "No Job");
		addPerson("Jess", "No Job");
		addPerson("brokenApplianceTest", "No Job");
	}


	/*
	 * This method creates all the people initally necessary to run the city
	 * All people are created with houses, start there, and will go to work at the beginnning of the day
	 * All workers are created with cars (because they have jobs so they can afford them)
	 */
	public void createInitialPeople() {
		//Initial public transportation creation.
		addVehicle("bus");
		timer.schedule(new TimerTask() {
			public void run() {
				addVehicle("bus");
			}
		}, 18000);

		/*Market workers*/
		//Spreading these out so that they don't get stuck next to each other
		addPersonWithCar("marketManager", "Market Manager1");
		addPerson("marketWorker", "Market Worker1");

		/*Bank Workers*/
		addPerson("bank manager", "Bank Manager");
		addPerson("bank teller", "Bank Teller");
		
		addPerson("bank teller", "Bank Teller");
		//addPerson("bank teller", "Bank Teller");
		
		addPersonWithCar("marketManager", "Market Manager2");
		addPerson("marketWorker", "Market Worker2");
	
		/*Restaurant2 workers*/
		addPersonWithCar("host2", "Restaurant2 Host");
		addPerson("cashier2", "Restaurant2 Cashier");
		addPerson("cook2", "Restaurant2 Cook");
		addPerson("waiter2", "Restaurant2 Waiter");
		/*Restaurant5 workers*/
		addPerson("host5", "Restaurant5 Host");
		addPerson("cashier5", "Restaurant5 Cashier");
		addPerson("cook5", "Restaurant5 Cook");
		addPerson("waiter5", "Restaurant5 Waiter");
		/*Restaurant3 workers*/
		addPerson("host3", "Restaurant3 Host");
		addPerson("cashier3", "Restaurant3 Cashier");
		addPerson("cook3", "Restaurant3 Cook");
		addPerson("waiter3", "Restaurant3 Waiter");
		/*Restaurant1 workers*/
		addPerson("host1", "Restaurant1 Host");
		addPerson("cashier1", "Restaurant1 Cashier");
		addPerson("cook1", "Restaurant1 Cook");
		addPerson("waiter1", "Restaurant1 Waiter");
		/*Restaurant4 workers*/
		addPerson("host4", "Restaurant4 Host");
		addPerson("cashier4", "Restaurant4 Cashier");
		addPerson("cook4", "Restaurant4 Cook");
		addPerson("regularWaiter4", "Restaurant4 RegularWaiter");
		
		addPersonWithCar("marketManager", "Market Manager3");
		addPerson("marketWorker", "Market Worker3");
		
		addVehicle("truck");
		addVehicle("truck");
		addVehicle("truck");
	}
	
	public void createVitalStaff() {
		addPerson("Landlord", "Landlord1");
		addPerson("Landlord2", "Landlord2");
	}
	
	public void populateBanksAndMarkets() {

		addPerson("bank manager", "Bank Manager");
		addPerson("bank teller", "Bank Teller");
		addPerson("bank teller", "Bank Teller");
		addPerson("bank teller", "Bank Teller");
		
		addPerson("marketManager", "Market Manager1");
		addPerson("marketWorker", "Market Worker1");
		addPerson("marketManager", "Market Manager2");
		addPerson("marketWorker", "Market Worker2");
		addPerson("marketManager", "Market Manager3");
		addPerson("marketWorker", "Market Worker3");
		
		addVehicle("truck");
		addVehicle("truck");
		addVehicle("truck");
	}

	public void setTimeDisplay(String timeToDisplay){
		timeDisplay.setText(timeToDisplay);
	}

	public void enableBackToCity() {
		backToCity.setEnabled(true);
	}
	
	public void changeBuildingControlPanel(String building){
		for(JPanel p : buildingPanels){
			p.setVisible(false);
		}
		if(building.equals("Restaurant1")){
			restaurant1Panel.setVisible(true);
		}
		if(building.equals("Restaurant2")){
			restaurant2Panel.setVisible(true);
		}
		if(building.equals("Restaurant3")){
			restaurant3Panel.setVisible(true);
		}
		if(building.equals("Restaurant4")){
			restaurant4Panel.setVisible(true);
		}
		if(building.equals("Restaurant5")){
			restaurant5Panel.setVisible(true);
		}
		if(building.equals("Market1")){
			market1Panel.setVisible(true);
		}
		if(building.equals("Market2")){
			market2Panel.setVisible(true);
		}
		if(building.equals("Market3")){
			market3Panel.setVisible(true);
		}
		if(building.equals("Bank1")){
			bank1Panel.setVisible(true);
		}
	}
	
	public Restaurant1 getRest1() {
		return cityMap.getRest1();
	}

	public Restaurant1AnimationPanel getRest1Animation() {
		return cityGui.restaurant1;
	}
}