package city.gui.Bank;
import javax.swing.*;

import astar.AStarTraversal;
import city.Bank;
import city.CityMap;
import city.House;
import Role.BankCustomerRole;
import Role.BankManagerRole;
import Role.BankTellerRole;


import java.awt.*;
import java.awt.event.*;
import java.util.Vector;

import city.PersonAgent;
import city.gui.PersonGui;

/**
* Panel in frame that contains all the restaurant information,
* including host, cook, waiters, and customers.
*/

public class BankPanel extends JPanel {
	
	PersonAgent person;
	PersonAgent person2;
	PersonAgent person3;
	PersonAgent person4;
	PersonAgent person5;
	PersonAgent person6;
	House house = new House("house1");
	Bank bank = new Bank();
	AStarTraversal aStarTraversal;
    CityMap citymap = new CityMap();
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
    int x = 100;
    
    int waiterposcounter = 30;
    public BankPanel(BankGui gui) {
        
    	this.gui = gui;
    	
    	//PersonAgent person2 = new PersonAgent("steve", aStarTraversal, citymap, house);
    	//PersonGui person2gui = new PersonGui(person2);
    	//person2.setGui(person2gui);
        //person2.startThread();
    	//BankManagerRole bmr = new BankManagerRole(bank);	
		//BankManagerRoleGui g2 = new BankManagerRoleGui(bankmanager, gui);
		//bankmanager.setPerson(person2);
		//gui.animationPanel.addGui(g2);
		//bankmanager.setGui(g2);
        
       // person2.addRole(bankmanager, true);
     
       // PersonAgent person3 = new PersonAgent("john", aStarTraversal, citymap, house);
        //person3.startThread();

        
        //PersonAgent person6 = new PersonAgent("boobbyy", aStarTraversal, citymap, house);
        //PersonGui person6gui = new PersonGui(person6);
        //person6.setGui(person6gui);
        //person6.startThread();
		
    	//BankTellerRole btr = new BankTellerRole(bankmanager);	
		//BankTellerRoleGui g6 = new BankTellerRoleGui(btr, gui);
		//btr.setPerson(person6);
		//person6.addRole(btr, true);
		//gui.animationPanel.addGui(g6);
		//btr.setGui(g6);
		//banktellers.add(btr);
		
		//bankmanager.msgBankTellerArrivedAtBank(btr);
        
        
        
        
        
        
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
    		
    		person5 = new PersonAgent("jimmy", aStarTraversal, citymap, house);
            person5.startThread();
    		
    		BankCustomerRole bcrnew = new BankCustomerRole(10/*,person5*/);	
    		BankCustomerRoleGui gnew = new BankCustomerRoleGui(bcrnew, gui);
    		bcrnew.setPerson(person5);
    		person5.addRole(bcrnew, true);
    		gnew.setArrivedAtBank();
    		gui.animationPanel.addGui(gnew);
    		//c.setHost(host);
    		//c.setCashier(cashier);
    		bcrnew.setGui(gnew);
    		bcrnew.gui.setWaitingPosition(x, 350);
    		x += 30;
    		bankcustomers.add(bcrnew);
    		bcrnew.setManager(bankmanager);
    		bankmanager.msgCustomerArrivedAtBank(bcrnew);
    		
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
    	
    	PersonAgent person6 = new PersonAgent("boobbyy", aStarTraversal, citymap, house);
        person6.startThread();
		
    	BankTellerRole btr = new BankTellerRole(bankmanager);	
		BankTellerRoleGui g6 = new BankTellerRoleGui(btr, gui);
		btr.setPerson(person6);
		//g3.setHomePosition(12, 20 + bankcustomers.size() * 25);
		person6.addRole(btr, true);
		//g3.setArrivedAtBank();
		gui.animationPanel.addGui(g6);
		btr.setGui(g6);
		banktellers.add(btr);
		bankmanager.msgBankTellerArrivedAtBank(btr);
    	
    	
    	
    }
    
    public void depletemarket2supply() {
    	
    	//market2.depletemarketsupply();
    }
    
    
}
