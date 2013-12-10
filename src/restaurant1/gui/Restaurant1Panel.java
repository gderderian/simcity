package restaurant1.gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

import city.gui.ControlPanel;

/**
 * Panel in frame that contains all the restaurant information,
 * including host, cook, waiters, and customers.
 */
public class Restaurant1Panel extends JPanel implements ActionListener {

    private ControlPanel cp;
    
    private JLabel title;
    
    private JButton normalWaiter;
    private JButton sharedDataWaiter;
    
    private JButton depleteInventory;
    
    public Restaurant1Panel(ControlPanel cp) {
    	this.cp = cp;

    	setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
    	
        title = new JLabel("Restaurant 1");
        title.setAlignmentX(CENTER_ALIGNMENT);
        add(title);
        
    	add(Box.createVerticalStrut(10));
    	
    	normalWaiter = new JButton("Add normal waiter");
    	sharedDataWaiter = new JButton("Add shared data waiter");
    	
    	depleteInventory = new JButton("Deplete inventory");
    	
    	normalWaiter.addActionListener(this);
    	normalWaiter.setAlignmentX(CENTER_ALIGNMENT);
    	sharedDataWaiter.addActionListener(this);
    	sharedDataWaiter.setAlignmentX(CENTER_ALIGNMENT);
    	depleteInventory.addActionListener(this);
    	depleteInventory.setAlignmentX(CENTER_ALIGNMENT);
    	
    	add(normalWaiter);
    	add(Box.createVerticalStrut(20));
    	add(sharedDataWaiter);
    	add(Box.createVerticalStrut(20));
    	add(depleteInventory);
    	add(Box.createVerticalStrut(20));
    	
    	
    }

	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getSource() == normalWaiter) {
			addNormalWaiter();
		} else if(e.getSource() == sharedDataWaiter) {
			addSharedDataWaiter();
		} else if(e.getSource() == depleteInventory) {
			depleteInventory();
		}
		
	}

	private void depleteInventory() {
		// TODO Auto-generated method stub
		
	}

	private void addSharedDataWaiter() {
		// TODO Auto-generated method stub
		
	}

	private void addNormalWaiter() {
		// TODO Auto-generated method stub
		
	}

    /**
     * Sets up the restaurant label that includes the menu,
     * and host and cook information
     */
    /**
     * Adds a customer or waiter to the appropriate list
     *
     * @param type indicates whether the person is a customer or waiter (later)
     * @param name name of person
     */
    /*public void addPerson(String type, String name, boolean isHungry) {

    	if (type.equals("Customers")) {
    		Restaurant1CustomerRole c = new Restaurant1CustomerRole(name);
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
    		Restaurant1WaiterRole w = new Restaurant1WaiterRole(name);
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
    
    
}