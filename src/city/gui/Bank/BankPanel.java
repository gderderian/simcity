package city.gui.Bank;
import javax.swing.*;

import astar.AStarTraversal;
import city.Bank;
import Role.BankCustomerRole;
import Role.BankManagerRole;
import Role.BankTellerRole;

import java.awt.*;
import java.awt.event.*;
import java.util.Vector;

import city.PersonAgent;

/**
* Panel in frame that contains all the restaurant information,
* including host, cook, waiters, and customers.
*/

public class BankPanel extends JPanel {
	
	PersonAgent person;
	PersonAgent person2;
	PersonAgent person3;
	PersonAgent person4;
	Bank bank;
    AStarTraversal aStarTraversal;
    //Host, cook, waiters and customers
    private BankManagerRole bankmanager = new BankManagerRole(bank);
    
    private Vector<BankCustomerRole> bankcustomers = new Vector<BankCustomerRole>();
    private Vector<BankTellerRole> banktellers = new Vector<BankTellerRole>();
    private JPanel restLabel = new JPanel();
    private ListPanel bankcustomerPanel = new ListPanel(this, "Customers");
    private ListPanel banktellerPanel = new ListPanel(this, "Waiters");
    private JPanel group = new JPanel();
    private BankGui gui; //reference to main gui
    //private BankManagerRoleGui bankmanagerGui = new BankManagerRoleGui(bankmanager, gui); 
    
    
    int waiterposcounter = 30;
    public BankPanel(BankGui gui) {
        
    	this.gui = gui;
    	
    	BankManagerRole bmr = new BankManagerRole(bank);	
		BankManagerRoleGui g2 = new BankManagerRoleGui(bmr, gui);
		gui.animationPanel.addGui(g2);
		bmr.setGui(g2);
        PersonAgent person2 = new PersonAgent("steve", aStarTraversal, null);
        person2.startThread();
        person2.addRole(bmr, true);
    	
        BankTellerRole btr = new BankTellerRole(bmr);	
		BankTellerRoleGui g3 = new BankTellerRoleGui(btr, gui);
		//g3.setHomePosition(12, 20 + bankcustomers.size() * 25);
		//g3.setArrivedAtBank();
		gui.animationPanel.addGui(g3);
		btr.setGui(g3);
		banktellers.add(btr);
        PersonAgent person3 = new PersonAgent("john", aStarTraversal, null);
        person3.startThread();
        person3.addRole(btr, true);
        
        bmr.msgBankTellerArrivedAtBank(btr);
    
        BankCustomerRole bcr = new BankCustomerRole(10,person);	
		BankCustomerRoleGui g = new BankCustomerRoleGui(bcr, gui);
		g.setHomePosition(12, 20 + bankcustomers.size() * 25);
		g.setArrivedAtBank();
		gui.animationPanel.addGui(g);
		bcr.setGui(g);
		bankcustomers.add(bcr);
        PersonAgent person = new PersonAgent("bob", aStarTraversal, null);
        person.startThread();
        person.addRole(bcr, true);
        
        bmr.msgCustomerArrivedAtBank(bcr);
        
        
        
        
        
        
        
        
        
        
        setLayout(new GridLayout(1, 2, 20, 20));
        group.setLayout(new GridLayout(1, 2, 10, 10));
        group.add(bankcustomerPanel);
        group.add(banktellerPanel);
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
        label.setText("stuff" );
                //"<html><h3><u>Tonight's Host</u></h3><table><tr><td>host:</td><td>" + + "</td></tr></table><h3><u> Menu</u></h3><table><tr><td>Chicken</td><td>$2</td></tr><tr><td>Burrito</td><td>$3</td></tr><tr><td>Pizza</td><td>$4</td></tr><tr><td></td><td></td></tr></table><br></html>");
   
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

        if (type.equals("BankCustomerRole")) {

            for (int i = 0; i < bankcustomers.size(); i++) {
                BankCustomerRole temp = bankcustomers.get(i);
                if (temp.getName() == name)
                   gui.updateInfoPanel(temp);
            }
        
        }
        else if(type.equals("BankTellerRole")) {
    		for (int i = 0; i < banktellers.size(); i++) {
                BankTellerRole temp = banktellers.get(i);
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
    	if (type.equals("BankCustomerRole")) {
    		
    		BankCustomerRole bcr = new BankCustomerRole(10,person);	
    		BankCustomerRoleGui g = new BankCustomerRoleGui(bcr, gui);
    		g.setHomePosition(12, 20 + bankcustomers.size() * 25);
    	    g.setArrivedAtBank();
    	  
    		gui.animationPanel.addGui(g);
    		//c.setHost(host);
    		//c.setCashier(cashier);
    		bcr.setGui(g);
    		bankcustomers.add(bcr);
    		//c.startThread();
    	
    	}
    	//creating new waiter agents
    	else if(type.equals(("BankTellerRole"))) {
    		
    		//WaiterGui waiterGui = new WaiterGui(waiter, host, );
    		BankTellerRole bt = new BankTellerRole(bankmanager);	
    		BankTellerRoleGui g = new BankTellerRoleGui(bt, gui);
    		//wg.xPos += waiterposcounter;
    		g.setHomePosition(20 + banktellers.size() * 25, 20);
    		gui.animationPanel.addGui(g);
    		bt.setGui(g);
    		banktellers.add(bt);
    		bankmanager.msgBankTellerArrivedAtBank(bt);
    		//w.startThread();
    		//waiterposcounter += 15;
    		
    	
    	}
    	//creating new cook agent
    	else if(type.equals(("BankManagerRole"))) {
    		
    		BankManagerRole bm = new BankManagerRole(bank);
    		BankManagerRoleGui g = new BankManagerRoleGui(bm, gui);
    		//gui.animationPanel.addGui(cg);
    		bm.setGui(g);
    		//c.startThread();	
    	
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
    	
    	//cook.msgDepleteCookSupply();
    	
    }
    
    public void depletemarket1supply() {
    	
    	//market1.depletemarketsupply();
    }
    
    public void depletemarket2supply() {
    	
    	//market2.depletemarketsupply();
    }
    
    
}
