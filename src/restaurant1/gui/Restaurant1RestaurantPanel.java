package restaurant1.gui;

import restaurant1.Restaurant1CashierRole;
import restaurant1.Restaurant1CookRole;
import restaurant1.Restaurant1CustomerRole;
import restaurant1.Restaurant1HostRole;
import restaurant1.Restaurant1WaiterRole;

import javax.swing.*;

import city.PersonAgent;

import agent.Agent;

import java.awt.*;
import java.awt.event.*;
import java.util.Vector;

/**
 * Panel in frame that contains all the restaurant information,
 * including host, cook, waiters, and customers.
 */
public class Restaurant1RestaurantPanel extends JPanel {

    private Vector<Agent> agents = new Vector<Agent>();
    
    //Added for SimCity - creating a staff of person agents
    PersonAgent hostPerson = new PersonAgent("host");
    PersonAgent cookPerson = new PersonAgent("cook");
    PersonAgent cashierPerson = new PersonAgent("cashier");
    
    //Host, cook, waiters and customers
    private Restaurant1HostRole host = new Restaurant1HostRole("Wilczynski", hostPerson);
    private Restaurant1HostGui hostGui = new Restaurant1HostGui(host);
    
    private Restaurant1CookRole cook = new Restaurant1CookRole("Rami", cookPerson);
    private Restaurant1CookGui cookGui = new Restaurant1CookGui(cook);
    
    private Restaurant1CashierRole cashier = new Restaurant1CashierRole("Crowley", cashierPerson);
    private Restaurant1CashierGui cashierGui = new Restaurant1CashierGui(cashier);

    private Vector<Restaurant1WaiterRole> waiters = new Vector<Restaurant1WaiterRole>();
    private Vector<Restaurant1CustomerRole> customers = new Vector<Restaurant1CustomerRole>();
    

    private JPanel restLabel = new JPanel();
    private Restaurant1ListPanel customerPanel = new Restaurant1ListPanel(this, "Customers");
    private Restaurant1ListPanel waiterPanel = new Restaurant1ListPanel(this, "Waiters");
    private JPanel group = new JPanel();

    private Restaurant1RestaurantGui gui; //reference to main gui

    public Restaurant1RestaurantPanel(Restaurant1RestaurantGui gui) {
        this.gui = gui;
        host.setGui(hostGui);
        cook.setGui(cookGui);

        gui.animationPanel.addGui(cookGui);
        gui.animationPanel.addGui(cashierGui);
        hostPerson.startThread();
        cookPerson.startThread();
        cashierPerson.startThread();
       
        agents.add(hostPerson);
        agents.add(cookPerson);
        agents.add(cashierPerson);
        
        setLayout(new BorderLayout(0, 0));
        group.setLayout(new GridLayout(1, 3, 1, 1));

        group.add(customerPanel);
        group.add(waiterPanel);

        initRestLabel();
        add(restLabel, BorderLayout.WEST);
        add(group, BorderLayout.CENTER);
    }

    /**
     * Sets up the restaurant label that includes the menu,
     * and host and cook information
     */
    private void initRestLabel() {
        JLabel label = new JLabel();
        restLabel.setLayout(new BorderLayout());
        label.setText(
                "<html><h3><u>Tonight's Staff</u></h3><table><tr><td>Host:</td><td>" + host.getName() + 
                "</td></tr></table><table><tr><td>Cook:</td><td>" + cook.getName() + 
                "</td></tr></table><table><tr><td>Cashier:</td><td>" + cashier.getName() + 
                "</td></tr></table><h3><u> Menu</u></h3><table><tr><td>Steak</td><td>$15.99</td></tr>" +
                "<tr><td>Fish</td><td>$13.99</td></tr><tr><td>Chicken</td><td>$10.99</td></tr></table><br></html>");

        restLabel.setBorder(BorderFactory.createRaisedBevelBorder());
        restLabel.add(label, BorderLayout.CENTER);
        restLabel.add(new JLabel("    "), BorderLayout.EAST);
        restLabel.add(new JLabel("    "), BorderLayout.WEST);
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
                Restaurant1CustomerRole temp = customers.get(i);
                if (temp.getName() == name)
                    gui.updateInfoPanel(temp);
            }
        }
        
        if(type.equals("Waiters")) {
        	for (int i = 0; i < waiters.size(); i++) {
                Restaurant1WaiterRole temp = waiters.get(i);
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
    /*public void addPerson(String type, String name, boolean isHungry) {

    	if (type.equals("Customers")) {
    		Restaurant1CustomerRole c = new Restaurant1CustomerRole(name, new PersonAgent(name));
    		Restaurant1CustomerGui g = new Restaurant1CustomerGui(c, gui);
    		if(isHungry) {
    			g.setHungry();
    		}

    		gui.animationPanel.addGui(g);
    		c.setHost(host);
    		c.setGui(g);
    		agents.add(c);
    		customers.add(c);
    		c.startThread();
    	}
    	if (type.equals("Waiters")) {
    		Restaurant1WaiterRole w = new Restaurant1WaiterRole(name, new PersonAgent(name));
    		Restaurant1WaiterGui g = new Restaurant1WaiterGui(w);
    		
    		gui.animationPanel.addGui(g);
    		w.setHost(host);
    		w.setCook(cook);
    		w.setCashier(cashier);
    		w.setGui(g);
    		host.addWaiter(w);
    		agents.add(w);
    		waiters.add(w);
    		if(waiters.size() > 13) {
    			g.setHome((waiters.size() - 13) * 40 + 200, 100);
    		}
    		else {
    			g.setHome(waiters.size() * 40 + 200, 60);
    		}
    		w.startThread();
    	}
    }*/
    
    public void pause() {
    	for(Agent a : agents) {
    		a.pause();
    	}
    }
    
    public void resume() {
    	for(Agent a : agents) {
    		a.resume();
    	}
    }
}
