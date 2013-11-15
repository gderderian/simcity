package city.gui.restaurant4;

import city.Restaurant4.CashierRole4;
import city.Restaurant4.CustomerRole4;
import city.Restaurant4.HostRole4;
import city.Restaurant4.CookRole4;
import city.Restaurant4.MarketRole4;
import city.Restaurant4.WaiterRole4;

import javax.swing.*;

import java.awt.*;
import java.awt.event.*;
import java.util.Vector;

public class RestaurantPanel4 extends JPanel implements ActionListener {
    //Host, cook, waiters and customers
	private CookRole4 cook;
	private CookGui4 cookGui= new CookGui4(cook);
	private CashierRole4 cashier;
	private MarketRole4 market1;
	private MarketRole4 market2;
	private MarketRole4 market3;
    //private CookGui cookGui = new CookGui(host);
	private HostRole4 host;
    private HostGui4 hostGui = new HostGui4(host);
    
    Vector<CustomerRole4> customers = new Vector<CustomerRole4>();
    Vector<WaiterRole4> waiters = new Vector<WaiterRole4>();

    private JPanel restLabel = new JPanel();
    private JPanel ownerPanel = new JPanel();
    private JButton pause;
    private JLabel idLabel;
    boolean paused= false;

    private RestaurantGui4 gui; //reference to main gui
    
    private static int rows= 1;
    private static int columns= 2;
    private static int space1= 20;
    private static int x= 250;
    private static int y= 230;

    public RestaurantPanel4(RestaurantGui4 gui) {
        host = new HostRole4("Sarah");
        cook= new CookRole4("Heisenberg");
        cashier= new CashierRole4("Cassie the Cashier");
        
        market1= new MarketRole4("Ralphs");
        market2= new MarketRole4("Vons");
        market3= new MarketRole4("Whole Foods");
        
        cook.addMarket(market1);
        cook.addMarket(market2);
        cook.addMarket(market3);
        
        market1.setCashier(cashier);
        market2.setCashier(cashier);
        market3.setCashier(cashier);
        
        this.gui = gui;
        host.setGui(hostGui);
        gui.animationPanel.addGui(hostGui);
        
        cook.setGui(cookGui);
        gui.animationPanel.addGui(cookGui);
        
        host.startThread();
        cook.startThread();
        cashier.startThread();
        
        market1.startThread();
        market2.startThread();
        market3.startThread();
        
        setLayout(new GridLayout(rows, columns, space1, space1));
        
        initRestLabel();
        add(restLabel);
        
        initOwnerPanel();
        add(ownerPanel);
    }

    /**
     * Sets up the restaurant label that includes the menu,
     * and host and cook information
     */
    private void initRestLabel() {
        JLabel label = new JLabel();
        restLabel.setLayout(new BorderLayout());
        label.setText(
                "<html><h3><u>Tonight's Staff</u></h3><table><tr><td>host:</td><td>" + host.getName() + "</td></tr></table><h3><u> Menu</u></h3><table><tr><td>Steak</td><td>$15.99</td></tr><tr><td>Chicken</td><td>$10.99</td></tr><tr><td>Salad</td><td>$5.99</td></tr><tr><td>Pizza</td><td>$8.99</td></tr></table><br></html>");

        restLabel.setBorder(BorderFactory.createRaisedBevelBorder());
        restLabel.add(label, BorderLayout.CENTER);
        restLabel.add(new JLabel("               "), BorderLayout.EAST);
        restLabel.add(new JLabel("               "), BorderLayout.WEST);
    }
    
    public void initOwnerPanel(){
        Dimension idDim= new Dimension(x, y);
 
    	 ownerPanel= new JPanel();
         ownerPanel.setPreferredSize(idDim);
         ownerPanel.setMinimumSize(idDim);
         ownerPanel.setMaximumSize(idDim);
         
         idLabel= new JLabel("Restaurant Owner: Justine Cocchi");
         ImageIcon icon= new ImageIcon("scotland.png");
         JButton picButton= new JButton(icon);
         ownerPanel.add(idLabel);
         ownerPanel.add(picButton);
         
         pause= new JButton("Pause");
         pause.addActionListener(this);
         ownerPanel.add(pause);
    }

    public void actionPerformed(ActionEvent e){
    	if (e.getSource() == pause){
    		if(paused == false){
        		pause.setText("Resume");
        		paused= true;
        		pause();
        	}
        	else if(paused == true){
        		pause.setText("Pause");
        		paused= false;
        		restart();
        	}
        }
    }
    
    /**
     * When a customer or waiter is clicked, this function calls
     * updatedInfoPanel() from the main gui so that person's information
     * will be shown
     *
     * @param type indicates whether the person is a customer or waiter
     * @param name name of person
     */
    public void showInfo(String type, String name, boolean status) {
        if (type.equals("Customers")) {
            for (int i = 0; i < customers.size(); i++) {
                CustomerRole4 temp = customers.get(i);
                if (temp.getName() == name)
                    gui.updateInfoPanel(temp, status);
            }
        }
        
        if (type.equals("Waiters")) {
        	for (int i = 0; i < waiters.size(); i++) {
                WaiterRole4 temp = waiters.get(i);
                if (temp.getName().equals(name))
                	gui.updateInfoPanel(temp, status);
            }
        }
    }
    
    /**
     * Adds a customer or waiter to the appropriate list
     *
     * @param type indicates whether the person is a customer or waiter (later)
     * @param name name of person
     */
    public void addPerson(String type, String name, boolean hunger) {
    	if (type.equals("Customers")) {
    		CustomerRole4 c = new CustomerRole4(name);	
    		CustomerGui4 g = new CustomerGui4(c, gui);
    		
    		gui.animationPanel.addGui(g);// dw
    		c.setHost(host);
    		c.setCashier(cashier);
    		c.setGui(g);
    		customers.add(c);
    		c.startThread();
    	}
    }
    
    public void addWaiter(String name){
    	WaiterRole4 waiter= new WaiterRole4(name, gui);
    	WaiterGui4 waiterGui= new WaiterGui4(waiter);
    	
    	gui.animationPanel.addGui(waiterGui);
    	waiter.setGui(waiterGui);
    	
    	waiter.setCook(cook);
    	waiter.setHost(host);
    	waiter.setCashier(cashier);
    	
    	host.addWaiter(waiter);
    	waiters.add(waiter);
    	
    	waiter.startThread();
    }
    
    public void pause(){
    	cook.pause();
    	host.pause();
    	cashier.pause();
    	market1.pause();
    	market2.pause();
    	market3.pause();
    	for(WaiterRole4 waiter : waiters){
    		waiter.pause();
    	}
    	for(CustomerRole4 customer : customers){
    		customer.pause();
    	}
    }
    
    public void restart(){
    	cook.resume();
    	host.resume();
    	cashier.resume();
    	market1.resume();
    	market2.resume();
    	market3.resume();
    	for(WaiterRole4 waiter : waiters){
    		waiter.resume();
    	}
    	for(CustomerRole4 customer : customers){
    		customer.resume();
    	}
    }
}