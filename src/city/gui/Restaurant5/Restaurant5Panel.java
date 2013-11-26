package city.gui.Restaurant5;


import javax.swing.*;

import astar.AStarTraversal;
import city.CityMap;
import city.House;
import city.PersonAgent;
import city.Restaurant5.Restaurant5CookRole;
import city.Restaurant5.Restaurant5CustomerRole;
import city.Restaurant5.Restaurant5HostRole;
import city.Restaurant5.Restaurant5WaiterRole;
import city.gui.BuildingPanel;
import city.gui.Gui;
import city.gui.PersonGui;
import city.gui.Bank.BankManagerRoleGui;

import java.awt.*;
import java.awt.event.*;
import java.util.Vector;

/**
 * Panel in frame that contains all the restaurant information,
 * including host, cook, waiters, and customers.
 */
public class Restaurant5Panel extends BuildingPanel implements MouseListener {

    //Host, cook, waiters and customers
	
	
	
    private Vector<Restaurant5CustomerRole> customers = new Vector<Restaurant5CustomerRole>();
    private Vector<Restaurant5WaiterRole> waiters = new Vector<Restaurant5WaiterRole>();
    private JPanel restLabel = new JPanel();
   // private ListPanel customerPanel = new ListPanel(this, "Customers");
    //private ListPanel waiterPanel = new ListPanel(this, "Waiters");
    private JPanel group = new JPanel();
	private AStarTraversal aStarTraversal;
	private CityMap citymap = new CityMap();
	private House house = new House("house1");
	private Restaurant5Gui gui;

    int waiterposcounter = 30;
    public Restaurant5Panel(Restaurant5Gui gui) {
        
    	this.gui = gui;
  
        setLayout(new GridLayout(1, 2, 20, 20));
        group.setLayout(new GridLayout(1, 2, 10, 10));
        //group.add(customerPanel);
        //group.add(waiterPanel);
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
                "<html><h3><u>Tonight's Host</u></h3><table><tr><td>host:</td><td>" + /*host.getName()*/  "</td></tr></table><h3><u> Menu</u></h3><table><tr><td>Chicken</td><td>$2</td></tr><tr><td>Burrito</td><td>$3</td></tr><tr><td>Pizza</td><td>$4</td></tr><tr><td></td><td></td></tr></table><br></html>");
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

            /*
        	for (int i = 0; i < customers.size(); i++) {
                CustomerAgent temp = customers.get(i);
                if (temp.getName() == name)
                    gui.updateInfoPanel(temp);
            }
            */
        
        }
        else if(type.equals("Waiters")) {
    		/*
        	for (int i = 0; i < waiters.size(); i++) {
                WaiterAgent temp = waiters.get(i);
                if (temp.getName() == name)
                    gui.updateInfoPanel(temp);
            }
            */
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
    		
    		/*
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
    		*/
    	
    	}
    	//creating new waiter agents
    	else if(type.equals(("Waiters"))) {
    		/*
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
    		 
    		 */
    		
    	
    	}
    	//creating new cook agent
    	else if(type.equals(("Cooks"))) {
    		/*
    		CookAgent c = new CookAgent(name);
    		CookGui cg = new CookGui(c,gui);
    		//gui.animationPanel.addGui(cg);
    		c.setGui(cg);
    		c.startThread();
    		*/	
    	
    	}
    }
    
    //back-end implementation of the pause button
    public void pauseagents() {
    	/*
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
    	*/
    }
    
    //back-end implementation of the restart button
    public void restartagents() {
    	/*
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
    	*/
    }
    
    public void waitergoonbreak() {
 
    	//host.msgWaiterWantBreak(waiters.get(1));
    
    }
    
    public void waitercomebackfrombreak() {
    	
    	//host.msgWaiterComeBackFromBreak(waiters.get(1));
    
    }
    
    public void depletecooksupply() {
    	
    	
    }
    
    public void depletemarket1supply() {
    	
 
    }
    
    public void depletemarket2supply() {
    	
    	
    }

	@Override
	public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void addGui(Gui g) {
		// TODO Auto-generated method stub
		
	}
    
    
}
