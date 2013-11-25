package city.gui.Restaurant3;

import city.Restaurant3.*;
import javax.swing.*;
import java.awt.*;
import java.util.Vector;


/**
 * Panel in frame that contains all the restaurant information,
 * including host, cook, waiters, and customers.
 */
public class RestaurantPanel3 extends JPanel {

	// Declare lists to store agents with multiple instances
    private Vector<CustomerRole3> customers = new Vector<CustomerRole3>();
    private Vector<WaiterRole3> waiterList = new Vector<WaiterRole3>();
	
    // Instantiate cook, host, and three markets
   // private HostAgent host = new HostAgent("Sarah");
   // private CookRole3 cook = new CookRole3("Jeff");
   // private CashierRole3 cashier = new CashierRole3("Bob");
    
    /*
    private MarketAgent3 market1 = new MarketAgent3("Ralphs", cashier);
    private MarketAgent3 market2 = new MarketAgent3("Vons", cashier);
    private MarketAgent3 market3 = new MarketAgent3("Albertsons", cashier);
    */
    
    private JPanel restLabel = new JPanel();
    private ListPanel3 customerPanel = new ListPanel3(this, "Customers");
    private ListPanel3 waiterPanel = new ListPanel3(this, "Waiters");
    private JPanel group = new JPanel();

    private RestaurantGui3 gui;

    private static final int REST_PANEL_ROWS = 1;
    private static final int REST_PANEL_COLS = 2;
    private static final int REST_PANEL_SPACE = 20;
    private static final int X_DIM = 500;
    private static final int Y_DIM = 550;
    private static final int THIRD = 3;

    public RestaurantPanel3(RestaurantGui3 gui) {
    	
        this.gui = gui;

        /*
        host.startThread();
        cook.startThread();
        cashier.startThread();
        market1.startThread();
        market2.startThread();
        market3.startThread();
        
        cook.addMarket(market1);
        cook.addMarket(market2);
        cook.addMarket(market3);
        */
        
        //CookGui3 cg = new CookGui3(cook, gui);
        //cook.setGui(cg);
       // gui.animationPanel.addGui(cg);

        setLayout(new GridLayout(REST_PANEL_ROWS, REST_PANEL_COLS, REST_PANEL_SPACE, REST_PANEL_SPACE));
        group.setLayout(new GridLayout(REST_PANEL_ROWS, REST_PANEL_COLS, REST_PANEL_SPACE, REST_PANEL_SPACE));

        JPanel personControl = new JPanel();
        personControl.setLayout(new BoxLayout(personControl, BoxLayout.Y_AXIS));
        
        Dimension custPanelDim = new Dimension(X_DIM, Y_DIM/THIRD);
        customerPanel.setPreferredSize(custPanelDim);
        customerPanel.setMinimumSize(custPanelDim);
        customerPanel.setMaximumSize(custPanelDim);
        
        Dimension waitPanelDim = new Dimension(X_DIM, Y_DIM/THIRD);
        waiterPanel.setPreferredSize(waitPanelDim);
        waiterPanel.setMinimumSize(waitPanelDim);
        waiterPanel.setMaximumSize(waitPanelDim);
        
        personControl.add(customerPanel);
        personControl.add(waiterPanel);
        
        group.add(personControl);

        initRestLabel();
        
        add(restLabel);
        add(group);

    }
    
    public void toggleTimer(){
    	
    	// Pause waiter agents
    	for(WaiterRole3 waiter : waiterList){
    		//waiter.toggleAgentPause();
    	}
    	
    	// Pause customer agents
    	for(CustomerRole3 customer : customers){
    		//customer.toggleAgentPause();
    	}
    	
    	// Pause cook
    	//cook.toggleAgentPause();
    	
    	// Pause host
    	//host.toggleAgentPause();
    	
    	gui.animationPanel.toggleTimer();
    	
    }

    /**
     * Sets up the restaurant label that includes the menu,
     * and host and cook information
     */
    private void initRestLabel() {
    	
    	/*
    	
        JLabel label = new JLabel();
        restLabel.setLayout(new BorderLayout());
        
        restaurant3.Menu restMenu = new restaurant3.Menu();
        
        String mainIntro = 	"<html><h3><u>Tonight's Staff</u></h3>";
        String mainHeaderHost = "<table><tr><td>Host:</td><td>" + host.getName() + "</td></tr></table>";
        // String mainHeaderWaiter = "<table><tr><td>Lead Waiter:</td><td>" + waiter.getName() + "</td></tr></table>";
        String menuHeader = "<h3><u>Menu</u></h3>";
        String menuDisplay = restMenu.displayMenu();
        String concludeText = "</html>";
        
        String finalDisplay = mainIntro + mainHeaderHost + menuHeader + menuDisplay + concludeText;
        
        label.setText(finalDisplay);
        
        restLabel.setBorder(BorderFactory.createRaisedBevelBorder());
        restLabel.add(label, BorderLayout.CENTER);
        restLabel.add(new JLabel("          "), BorderLayout.EAST);
        restLabel.add(new JLabel("          "), BorderLayout.WEST);
        
        */
        
    }

    /**
     * When a customer or waiter is clicked, this function calls
     * updatedInfoPanel() from the main gui so that person's information
     * will be shown
     *
     * @param type indicates whether the person is a customer or waiter
     * @param name name of person
     */
    public void showInfo(String type, String name) {

        if (type.equals("Customers")) {
            for (int i = 0; i < customers.size(); i++) {
                CustomerRole3 temp = customers.get(i);
                if (temp.getName() == name)
                    gui.updateInfoPanel(temp);
            }
        } else if (type.equals("Waiters")){
        	for (int i = 0; i < waiterList.size(); i++) {
                WaiterRole3 temp = waiterList.get(i);
                if (temp.getName() == name)
                    gui.updateInfoPanel(temp);
            }
        }
        
    }

    /**
     * Adds a customer or waiter to the appropriate list
     *
     * @param type indicates whether the person is a customer or waiter (later)
     * @param name name of person
     */
    public void addPerson(String type, String name, boolean isHungry) {
    	
    	
    	/*
    	if (type.equals("Customers")) {
    		
    		int customerIndex = customers.size();
    		int customerX = 10 + (customerIndex / 4) * 25;
    		int customerY = 15 + (customerIndex % 4) * 25;
    		
    		CustomerAgent c = new CustomerAgent(name, customerX, customerY);	
    		CustomerGui g = new CustomerGui(c, gui, customerX, customerY, customerIndex);
    		
    		gui.animationPanel.addGui(g);
    		c.setHost(host);
    		
    		c.setGui(g);
    		c.setCashier(cashier);
    		customers.add(c);
    		c.startThread();
    		
    		if (isHungry == true) {
    			c.getGui().setHungry();
    		}
    			
    	} else if (type.equals("Waiters")){
    		
    		int waiterIndex = waiterList.size();
    		int waiterX = 250 + (waiterIndex / 4) * 25;
    		int waiterY = 20 + (waiterIndex % 4) * 25;
    		
    		WaiterRole3 w = new WaiterRole3(name, waiterX, waiterY);
    		WaiterGui g = new WaiterGui(w, gui, waiterX, waiterY, waiterIndex);
    		
    		gui.animationPanel.addGui(g);
    		w.setHost(host);
    		w.setCook(cook);
    		
    		w.setGui(g);
    		waiterList.add(w);
    		w.setCashier(cashier);
    		w.startThread();
    		
    		host.addWaiter(w);
    		
    	}
    	
    */	
    	
    }
    
    

}
