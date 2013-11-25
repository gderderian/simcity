package city.gui;

import interfaces.BusStop;

import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TimerTask;
import java.util.Map.Entry;
import java.util.Timer;
import java.util.concurrent.Semaphore;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;

import city.Apartment;
import city.CityMap;
import city.House;
import astar.AStarTraversal;
import Role.BankManagerRole;
import Role.BankTellerRole;
import Role.LandlordRole;
import Role.MarketManager;
import Role.MarketWorker;
import Role.Role;
import city.Restaurant2.*;
import city.transportation.BusAgent;
import city.transportation.BusStopAgent;

public class ControlPanel extends JPanel implements ActionListener{
	
	public JScrollPane pane = new JScrollPane(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
    private JPanel view = new JPanel();
    private List<JButton> list = new ArrayList<JButton>();
    private JButton addPersonB = new JButton("Add");
    private JTabbedPane controlPane = new JTabbedPane();
    private JPanel worldControls = new JPanel();
    private JPanel addPerson = new JPanel();
    private JPanel infoPanel = new JPanel();
    private JButton populateCity = new JButton("Populate City");
    private JLabel timeDisplay = new JLabel("12:00am  -  Monday  -  Week 1");
    
    private Timer timer = new Timer();
    
    private int WINDOWX = 370;
    private int WINDOWY = 750;
    private int SCROLLY = WINDOWY/4;
    private int ADDPERSONY = WINDOWY/5;
    private int INFOPANELY = WINDOWY - ADDPERSONY;
    private int WINDOWXINSIDE = WINDOWX - 10;
    
    private Dimension scrollDim = new Dimension(WINDOWXINSIDE, SCROLLY);
    private Dimension panelDim = new Dimension(WINDOWX, WINDOWY);
    private Dimension addPersonDim = new Dimension(WINDOWXINSIDE, ADDPERSONY);
    private Dimension infoPanelDim = new Dimension(WINDOWXINSIDE, INFOPANELY);

    private JTextField nameField;
    private JTextField errorDisplay = new JTextField();
    private JPanel personControls = new JPanel();
    public JCheckBox isHungry;
    public JCheckBox takeBreak;
    private String[] jobs = {"[Please select a job]", "No job", "Restaurant2 Waiter", "Restaurant2 Cook", "Restaurant2 Host", "Bank Manager", "Bank Teller",
    		"Market Manager", "Market Worker", "Landlord"
    };
    private JComboBox jobField = new JComboBox(jobs);
    private Map<String, Role> jobRoles = new HashMap<String, Role>();
    //All the roles for the map
    Restaurant2WaiterRole rest2Waiter;
    Restaurant2CookRole rest2Cook;
    Restaurant2HostRole rest2Host;
    BankManagerRole bankManager;
    BankTellerRole bankTeller;
    MarketManager marketManager;
    MarketWorker marketWorker;
    LandlordRole landlord;
    
    int houseAssignmentNumber = 0;
    
    //TODO populate this
    private Map<String, String> jobLocations = new HashMap<String, String>();
    
    /** Universal city map **/
    CityMap cityMap = new CityMap();
    //Houses and apartments
    private List<House> houses = new ArrayList<House>();
    
    //Bus stops
    private List<BusStop> busStops = new ArrayList<BusStop>();
    
    //Size of astar semaphore grid
    static int gridX = 21; //# of x-axis tiles
    static int gridY = 18; //# of y-axis tiles

    //Semaphore grid for astar animation
    Semaphore[][] streetGrid = new Semaphore[gridX+1][gridY+1];
    Semaphore[][] sidewalkGrid = new Semaphore[gridX+1][gridY+1];
    
    CityGui cityGui;

    /**
     * Constructor for ListPanel.  Sets up all the gui
     *
     * @param rp   reference to the restaurant panel
     * @param type indicates if this is for customers or waiters
     */
    public ControlPanel() {
    	
    	//Adding jobs to jobRoles map
    	jobRoles.put("Restaurant2 Waiter", rest2Waiter);
    	jobRoles.put("Restaurant2Cook", rest2Cook);
    	jobRoles.put("Rest2Host", rest2Host);
    	jobRoles.put("Bank Manager", bankManager);
    	jobRoles.put("Bank Teller", bankTeller);
    	jobRoles.put("Market Manager", marketManager);
    	jobRoles.put("Market Worker", marketWorker);
    	jobRoles.put("Landlord", landlord);
    	
        view.setLayout(new FlowLayout());
        setLayout(new BoxLayout((Container) this, BoxLayout.PAGE_AXIS));

        setBorder(BorderFactory.createLineBorder(Color.black, 5));
        
        addPersonSection();
        
        setupWorldControls();
        
        controlPane.setPreferredSize(panelDim);
        worldControls.setPreferredSize(panelDim);
        worldControls.setLayout(new BoxLayout(worldControls, BoxLayout.PAGE_AXIS));
        worldControls.setAlignmentX(Component.CENTER_ALIGNMENT);
        controlPane.addTab("World", worldControls);
        controlPane.addTab("People", personControls);
        add(controlPane);
        
        List<String> stopLocations0 = new ArrayList<String>();
        List<String> stopLocations1 = new ArrayList<String>();
        List<String> stopLocations2 = new ArrayList<String>();
        List<String> stopLocations3 = new ArrayList<String>();
        
        //Add all nearby locations here
        stopLocations0.add("building0");
        stopLocations1.add("building1");
        stopLocations2.add("building2");
        stopLocations3.add("building3");
        
        cityMap.addStopDestinations(0, stopLocations0);
        cityMap.addStopDestinations(0, stopLocations0);
        cityMap.addStopDestinations(0, stopLocations0);
        cityMap.addStopDestinations(0, stopLocations0);
        
        //Set up the grids of semaphores
        populateSemaphoreGrids();
      	
        //Creation of houses and apartments
        createHouses();
      	//Creation of bus stops
        createBusStops();        
    }
    
    public void addRest2ToCityMap(Restaurant2 r){
        cityMap.setRestaurant2(r);
    }
    
    public void setCityGui(CityGui c){
    	cityGui = c;
    }
    
    public List<BusStop> getBusStops() {
    	return busStops;
    }
    
    public CityMap getCityMap() {
    	return cityMap;
    }
    
    private void setupWorldControls(){
    	
    	populateCity.addActionListener(this);
    	
    	worldControls.add(populateCity);
    	populateCity.setAlignmentX(Component.CENTER_ALIGNMENT);
    	worldControls.add(timeDisplay);
    	timeDisplay.setAlignmentX(Component.CENTER_ALIGNMENT);
    }
    
    private void addPersonSection(){
    	//personControls.add(new JLabel("<html><br><u>Add People</u><br></html>"));
    	
    	personControls.setPreferredSize(panelDim);
    	
    	addPerson.setPreferredSize(addPersonDim);
    	infoPanel.setPreferredSize(infoPanelDim);
        pane.setViewportView(view);
        
        infoPanel.add(new JLabel("List of people in SimCity"));
    	infoPanel.add(pane);
    	
    	//Add AddPerson panel and info panel to main panel
    	personControls.add(addPerson);
    	personControls.add(infoPanel);
        
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

        addPersonB.addActionListener(this);
        
        addPerson.add(addPersonB, flow);
        
        addPerson.add(new JLabel("Help messages:"));
        errorDisplay.setEditable(false);
        addPerson.add(errorDisplay, flow);

        view.setLayout(new BoxLayout((Container) view, BoxLayout.Y_AXIS));
        this.add(personControls);
    }
    

    /**
     * Method from the ActionListener interface.
     * Handles the event of the add button being pressed
     */
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == addPersonB) {
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
        else if(e.getSource() == populateCity){
        	populateCity();
        	populateCity.setEnabled(false);
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
            if(houseAssignmentNumber == 27){
            	houseAssignmentNumber = 26;
            }
            else{
                houseAssignmentNumber++;
            }
            
            cityGui.addPerson(name, aStarTraversal, job, cityMap, house);
        	System.out.println("Adding person " + name + " with job " + job);

            Dimension paneSize = pane.getSize();
            Dimension buttonSize = new Dimension((paneSize.width - 20),
                    (int) (paneSize.height / 7));
            button.setPreferredSize(buttonSize);
            button.setMinimumSize(buttonSize);
            button.setMaximumSize(buttonSize);
            button.addActionListener(this);
            list.add(button);
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
      	
      	for(int i = 15; i < 19; i++) //Crosswalk area
      		for(int j = 16; j < 19; j++)
      			streetGrid[i][j].release();
      	
      	//Release sidewalk semaphores
      	for(int i = 1; i < 21; i++) { //Top and bottom
      		for(int j = 1; j < 3; j++)
      			sidewalkGrid[i][j].release();
      		for(int j = 16; j < 18; j++)
      			sidewalkGrid[i][j].release();
      	}

      	for(int i = 3; i < 16; i++) { //Left and right
      		for(int j = 1; j < 3; j++)
      			sidewalkGrid[j][i].release();
      		for(int j = 19; j < 21; j++)
      			sidewalkGrid[j][i].release();
      	}
      	
      	for(int i = 7; i < 15; i++)
      		for(int j = 10; j < 12; j++)
      			sidewalkGrid[i][j].release();
      	
      	//End of sidewalk grid releasing
      	
      	//Adding in crosswalks (shared semaphores between street grid and sidewalk grid)
      	for(int i = 15; i < 19; i++) //Bottom right crosswalk
      		for(int j = 16; j < 18; j++)
      			sidewalkGrid[i][j] = streetGrid[i][j];
      	for(int i = 13; i < 15; i++) //Crosswalk to island
      		for(int j = 12; j < 16; j++)
      			sidewalkGrid[i][j] = streetGrid[i][j];
      	
      	//Releasing many semaphores on building entrances so multiple guis can "go in" to buildings
      	sidewalkGrid[20][0].release(100); //rest1
      	sidewalkGrid[0][3].release(100); //rest2
      	sidewalkGrid[0][17].release(100); //rest3
      	sidewalkGrid[10][18].release(100); //rest4
      	sidewalkGrid[13][9].release(100); //rest5
      	sidewalkGrid[21][11].release(100); //mark1
      	sidewalkGrid[5][0].release(100); //mark2
      	sidewalkGrid[9][9].release(100); //mark3
      	sidewalkGrid[21][1].release(100); //bank1
      	sidewalkGrid[0][12].release(100); //bank2
      	sidewalkGrid[21][4].release(100); //apart1
      	sidewalkGrid[1][18].release(100); //apart2
      	sidewalkGrid[21][8].release(100); //stop0
      	sidewalkGrid[11][0].release(100); //stop1
      	sidewalkGrid[0][8].release(100); //stop2
      	sidewalkGrid[18][7].release(100); //stop3
      	
      	sidewalkGrid[20][18].release(100); //starting point for agents
      	sidewalkGrid[21][18].release(100);
      	sidewalkGrid[19][18].release(100);

      	sidewalkGrid[21][17].release(5); //opening up permits in front of people's houses
      	sidewalkGrid[21][15].release(5);
      	sidewalkGrid[21][13].release(5);
      	sidewalkGrid[21][10].release(5);
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
      	sidewalkGrid[5][18].release(5);
      	sidewalkGrid[11][18].release(5);
      	sidewalkGrid[12][9].release(5);
      	sidewalkGrid[10][9].release(5);
      	sidewalkGrid[10][7].release(5);
      	
      	
      	streetGrid[18][18].release(100); //starting point for vehicles
      	
      	/********Finished setting up semaphore grid***********/
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
    	for(int i = 0; i < 26; i++) {
    		houses.add(new House("house" + Integer.toString(i + 1)));
    	}
    	System.out.println("Created houses.");
    	//Apartment apart1 = new Apartment("apart1", 1);
    	//houses.add(apart1);
    	//Apartment apart2 = new Apartment("apart2", 2);
    	//houses.add(apart2);
    }
    
    public void populateCity(){
    	//Add two buses at an interval
    	addVehicle("bus");
		timer.schedule(new TimerTask() {
			public void run() {
				 addVehicle("bus");
			}
		}, 16000	);
    	
		addPerson("host", "Restaurant2 Host");
		addPerson("cashier", "Restaurant2 Cashier");
		addPerson("cook", "Restaurant2 Cook");
		addPerson("waiter", "Restaurant2 Waiter");
		addPerson("RestaurantTest", "No job");
		
		addPerson("rest1test", "Restaurant1 Customer");
		
    }
    
    public void setTimeDisplay(String timeToDisplay){
    	timeDisplay.setText(timeToDisplay);
    }

}