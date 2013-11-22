package city.gui.Bank;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Vector;

/**
* Panel in frame that contains all the restaurant information,
* including host, cook, waiters, and customers.
*/

public class BankPanel extends JPanel {
	
    //Host, cook, waiters and customers
    private HostAgent host = new HostAgent("Sarah");
    private HostGui hostGui = new HostGui(host); 
    private CookAgent cook = new CookAgent("Cook");
    private CashierAgent cashier = new CashierAgent("bobby");
    private MarketAgent market1 = new MarketAgent("market1");
    private MarketAgent market2 = new MarketAgent("market2");
    private MarketAgent market3 = new MarketAgent("market3");
    private Vector<CustomerAgent> customers = new Vector<CustomerAgent>();
    private Vector<WaiterAgent> waiters = new Vector<WaiterAgent>();
    private JPanel restLabel = new JPanel();
    private ListPanel customerPanel = new ListPanel(this, "Customers");
    private ListPanel waiterPanel = new ListPanel(this, "Waiters");
    private JPanel group = new JPanel();
    private RestaurantGui gui; //reference to main gui
    private CookGui cookGui = new CookGui(cook, gui);
    int waiterposcounter = 30;
    public BankPanel(RestaurantGui gui) {
        
    	this.gui = gui;
        host.setGui(hostGui);
        gui.animationPanel.addGui(hostGui);
        host.startThread();    
        //this.addPerson("Cooks", "cook bily", false);
        cook.setGui(cookGui);
        gui.animationPanel.addGui(cookGui);
        cook.addMarket(market1);
        cook.addMarket(market2);
        cook.addMarket(market3);
        cook.addCashier(cashier);
        //cook.addMarket(backupchickenmarket);
        
        cook.startThread();
        cashier.startThread();
        market1.startThread();
        market2.startThread();
        market3.startThread();
  
        setLayout(new GridLayout(1, 2, 20, 20));
        group.setLayout(new GridLayout(1, 2, 10, 10));
        group.add(customerPanel);
        group.add(waiterPanel);
        initRestLabel();
        add(restLabel);
        add(group);
    }

    /**
     * Sets up the restaurant label that includes the menu,
     * and host and cook information
     */
    private void initRestLabel() {
    	
        JLabel label = new JLabel();
        restLabel.setLayout(new BorderLayout());
        label.setText(
                "<html><h3><u>Tonight's Host</u></h3><table><tr><td>host:</td><td>" + host.getName() + "</td></tr></table><h3><u> Menu</u></h3><table><tr><td>Chicken</td><td>$2</td></tr><tr><td>Burrito</td><td>$3</td></tr><tr><td>Pizza</td><td>$4</td></tr><tr><td></td><td></td></tr></table><br></html>");
        restLabel.setBorder(BorderFactory.createRaisedBevelBorder());
        restLabel.add(label, BorderLayout.CENTER);
        restLabel.add(new JLabel("               "), BorderLayout.EAST);
        restLabel.add(new JLabel("               "), BorderLayout.WEST);
    
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
                CustomerAgent temp = customers.get(i);
                if (temp.getName() == name)
                    gui.updateInfoPanel(temp);
            }
        
        }
        else if(type.equals("Waiters")) {
    		for (int i = 0; i < waiters.size(); i++) {
                WaiterAgent temp = waiters.get(i);
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
    public void addwaiters() {
   	 this.addPerson("Waiters", "new waiter ", false);
    }
    
    public void addPerson(String type, String name, boolean ishungry) {
    	//creating new customer agents
    	if (type.equals("Customers")) {
    		
    		CustomerAgent c = new CustomerAgent(name);	
    		CustomerGui g = new CustomerGui(c, gui, host);
    		g.setHomePosition(12, 20 + customers.size() * 25);
    		
    		if(ishungry == true)
    	    {
    	    	g.setHungry();
    	    }
    		gui.animationPanel.addGui(g);
    		c.setHost(host);
    		c.setCashier(cashier);
    		c.setGui(g);
    		customers.add(c);
    		c.startThread();
    	
    	}
    	//creating new waiter agents
    	else if(type.equals(("Waiters"))) {
    		
    		//WaiterGui waiterGui = new WaiterGui(waiter, host, );
    		WaiterAgent w = new WaiterAgent(name,host,cook,cashier);	
    		WaiterGui wg = new WaiterGui(w, gui,host);
    	
    		//wg.xPos += waiterposcounter;
    		wg.setHomePosition(45, 20 + waiters.size() * 25);
    		gui.animationPanel.addGui(wg);
    		w.setGui(wg);
    		waiters.add(w);
    		host.addwaiter(w);
    		w.startThread();
    		//waiterposcounter += 15;
    		
    	
    	}
    	//creating new cook agent
    	else if(type.equals(("Cooks"))) {
    		
    		CookAgent c = new CookAgent(name);
    		CookGui cg = new CookGui(c,gui);
    		//gui.animationPanel.addGui(cg);
    		c.setGui(cg);
    		c.startThread();	
    	
    	}
    }
    
    //back-end implementation of the pause button
    public void pauseagents() {
    	host.pause();
    	cook.pause();
    	 cashier.pause();
         market1.pause();
         market2.pause();
         market3.pause();

    	for(CustomerAgent pausecustomer: customers) {
    		pausecustomer.pause();
    	}
    	for(WaiterAgent pausewaiter: waiters) {
    		pausewaiter.pause();
    	}
    }
    
    //back-end implementation of the restart button
    public void restartagents() {
    	host.restart();
    	cook.restart();
    	 cashier.restart();
         market1.restart();
         market2.restart();
         market3.restart();
    	
    	for(CustomerAgent pausecustomer: customers) {
    		pausecustomer.restart();
    	}
    	for(WaiterAgent pausewaiter: waiters) {
    		pausewaiter.restart();
    	}
    }
    
    public void waitergoonbreak() {
 
    	host.msgWaiterWantBreak(waiters.get(1));
    
    }
    
    public void waitercomebackfrombreak() {
    	
    	host.msgWaiterComeBackFromBreak(waiters.get(1));
    
    }
    
    public void depletecooksupply() {
    	
    	cook.msgDepleteCookSupply();
    	
    }
    
    public void depletemarket1supply() {
    	
    	market1.depletemarketsupply();
    }
    
    public void depletemarket2supply() {
    	
    	market2.depletemarketsupply();
    }
    
    
}
