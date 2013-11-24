package city.gui;

import interfaces.Restaurant2Waiter;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import restaurant1.gui.Restaurant1AnimationPanel;
import city.Restaurant2.Restaurant2WaiterRole;
import city.gui.House.ApartmentAnimationPanel;
import city.gui.House.HouseAnimationPanel;
import city.gui.Market.MarketAnimationPanel;
import Role.Role;
import astar.AStarTraversal;
import city.Restaurant2.Restaurant2;
import city.gui.CityClock;
import city.gui.restaurant4.AnimationPanel4;
import city.CityMap;
import city.House;
import city.Market;
import city.PersonAgent;
import city.gui.restaurant2.Restaurant2AnimationPanel;
import city.transportation.BusAgent;
import city.transportation.Vehicle;

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
    
    Restaurant2 rest2 = new Restaurant2();
    Restaurant2AnimationPanel restaurant2 = new Restaurant2AnimationPanel(rest2);
    Restaurant1AnimationPanel restaurant1 = new Restaurant1AnimationPanel();
        AnimationPanel4 restaurant4 = new AnimationPanel4();
        //PersonAgent testPerson = new PersonAgent("test");
        //PersonGui testPersonGui = new PersonGui();
        
    MarketAnimationPanel market1Animation = new MarketAnimationPanel(this);
    
    ApartmentAnimationPanel apt1= new ApartmentAnimationPanel(1);
    ArrayList<HouseAnimationPanel> apt1List= new ArrayList<HouseAnimationPanel>();
    ApartmentAnimationPanel apt2= new ApartmentAnimationPanel(2);
    ArrayList<HouseAnimationPanel> apt2List= new ArrayList<HouseAnimationPanel>();
    
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

    	restaurant2.setBackground(new Color(150, 20, 60));
    	addBuildingPanel(restaurant2);
    	restaurant1.setBackground(Color.LIGHT_GRAY);
    	addBuildingPanel(restaurant1);           

    	//restaurant4.setCityGui(this);
    	controlPanel.addRest2ToCityMap(rest2);
    	
    	addBuildingPanel(market1Animation);

    	add(animationPanel, BorderLayout.EAST);

    	//Set up and populate apartment 1
    	apt1.setCityGui(this);
    	for(int i=0; i<10; i++){
    		apt1List.add(new HouseAnimationPanel());
    		buildingPanels.add(apt1List.get(i));
    	}
    	//Set up and populate apartment 2
    	apt2.setCityGui(this);
    	for(int i=0; i<10; i++){
    		apt2List.add(new HouseAnimationPanel());
    		buildingPanels.add(apt2List.get(i));
    	}

    	Dimension panelDim = new Dimension(WINDOWX - ANIMATIONX, WINDOWY);
    	infoPanel = new JPanel();
    	infoPanel.setPreferredSize(panelDim);
    	infoPanel.setMinimumSize(panelDim);
    	infoPanel.setMaximumSize(panelDim);
    	infoPanel.setBorder(BorderFactory.createTitledBorder("Information"));

    	infoPanel.setLayout(new FlowLayout());

    	//add(infoPanel, BorderLayout.WEST);
    	add(controlPanel, BorderLayout.WEST);

    	CityClock masterClock = new CityClock(this);
    	masterClock.startTime();

    }
    
    public void timerTick(int timeOfDay, int hourOfDayHumanTime, long minuteOfDay, String dayState, String amPm) {
    	for (PersonAgent person : people) {
    		person.msgTimeUpdate(timeOfDay);
    	}
    }
    
    public void addGui(Gui g){
            guis.add(g);
    }
    
    public void addPerson(PersonAgent p){
            people.add(p);
    }

    public void actionPerformed(ActionEvent e) {
            //if(e.getSource() == 
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
                if(building.equals("Restaurant2")){
                        animationPanel.setVisible(false);
                add(restaurant2, BorderLayout.EAST);
                        restaurant2.setVisible(true);
                }
                if(building.equals("City")){
                	for(BuildingPanel bp : buildingPanels) {
                		bp.setVisible(false);
                	}
                        animationPanel.setVisible(true);
                        add(animationPanel, BorderLayout.EAST);
                }       
                if(building.equals("Restaurant1")){
                	animationPanel.setVisible(false);
                	add(restaurant1, BorderLayout.EAST);
                	restaurant1.setVisible(true);
                }
                if(building.equals("Market1")){
                	animationPanel.setVisible(false);
                	add(market1Animation, BorderLayout.EAST);
                	market1Animation.setVisible(true);
                }
                if(building.equals("Apartment1")){
                	animationPanel.setVisible(false);
                	add(apt1, BorderLayout.EAST);
                	apt1.setVisible(true);
                }
                if(building.equals("Apartment2")){
                	animationPanel.setVisible(false);
                	add(apt2, BorderLayout.EAST);
                	apt2.setVisible(true);
                }
        }
        
        public void changeView(int building, int num){
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
        }
        
        public void addPerson(String name, AStarTraversal aStarTraversal, String job, CityMap map, House h){
                PersonAgent newPerson = new PersonAgent(name, aStarTraversal, map, h);
                
                //TODO finish the job thing
                Role r = Role.getNewRole(job, newPerson);
                
                if(r != null){
                	//Add location to this
                    newPerson.addFirstJob(r, "Unknown");
                }
                people.add(newPerson);
                PersonGui g = new PersonGui(newPerson);
                newPerson.setGui(g);
                guis.add(g);
                animationPanel.addGui(g);
                g.addAnimationPanel(restaurant2);
                
                newPerson.startThread();
                
                if(name.equals("RestaurantTest")){
                	newPerson.msgImHungry();
                }
        }

        public void enableComeBack(Restaurant2Waiter agent) {
                // TODO Auto-generated method stub
                
        }

        public void setEnabled(Restaurant2Waiter agent) {
                // TODO Auto-generated method stub
                
        }        
        
        public void addVehicle(String type, AStarTraversal aStarTraversal) {
        	if(type == "bus") {
        		BusAgent newBus = new BusAgent(aStarTraversal);
        		newBus.addBusStops(controlPanel.getBusStops());
        		vehicles.add(newBus);
        		VehicleGui g = new VehicleGui(newBus);
        		newBus.setGui(g);
        		guis.add(g);
        		animationPanel.addGui(g);
        		g.setMainAnimationPanel(animationPanel);

        		newBus.startThread();   
        	}
        }   
        
        private void addBuildingPanel(BuildingPanel bp) {
        	bp.setPreferredSize(new Dimension(ANIMATIONX, WINDOWY));
        	buildingPanels.add(bp);
        	bp.setCityGui(this);        	
        }
        
}