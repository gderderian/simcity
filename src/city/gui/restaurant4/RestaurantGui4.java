package city.gui.restaurant4;

import city.Restaurant4.CustomerRole4;
import city.Restaurant4.WaiterRole4;

import javax.swing.*;

import java.awt.*;
import java.awt.event.*;

public class RestaurantGui4 extends JFrame implements ActionListener {
	JPanel mainPanel = new JPanel();

	/* The GUI has two frames, the control frame (in variable gui) 
     * and the animation frame, (in variable animationFrame within gui)
     */
	AnimationPanel4 animationPanel = new AnimationPanel4();

    /* restPanel holds 3 panels
     * 1) the staff listing, menu, and lists of current customers all constructed
     *    in RestaurantPanel()
     * 2) the infoPanel about the clicked Customer (created just below)
     * 3) information about the owner of the retaurant
     */    
    private RestaurantPanel4 restPanel = new RestaurantPanel4(this);

    
    /* infoPanel holds information about the clicked customer, if there is one*/
    private JPanel infoPanel;
    private JLabel infoLabel; //part of infoPanel
    private JCheckBox stateCB;//part of infoLabel
    private JButton pause;
    //private JButton addWaiter;
    private ListPanel4 waiter;
    private ListPanel4 customerPanel;
    boolean paused= false;
    
    /* idPanel holds information about the restaurant owner */
    private JPanel bottomPanel;

    private Object currentPerson;/* Holds the agent that the info is about.
    								Seems like a hack */
    
    private static int WINDOWX = 500 * 2;
    private static int windowX= 500;
    private static int WINDOWY = 700;
    private static int xY= 50;
    private static int newY= xY;
    private static int rows= 1;
    private static int columns= 2;
    private static int space= 0;
    private static int bottomDimY= 250;
    private static double mult1= .6;
    private static double mult2= .25;
    /**
     * Constructor for RestaurantGui class.
     * Sets up all the gui components.
     */
    public RestaurantGui4() {
    	
    	setBounds(xY, xY, WINDOWX, WINDOWY);
    	
    	setLayout(new GridLayout(rows, columns, space, space));

    	animationPanel.setVisible(true);
    	add(animationPanel);
    	
    	newY= (int) (WINDOWY * mult1);
        mainPanel.add(restPanel);
        
        // Now, setup the info panel
        newY= (int) (WINDOWY * mult2);
        Dimension infoDim = new Dimension(windowX, newY);
        infoPanel = new JPanel();
        infoPanel.setPreferredSize(infoDim);
        infoPanel.setMinimumSize(infoDim);
        infoPanel.setMaximumSize(infoDim);
        infoPanel.setBorder(BorderFactory.createTitledBorder("Information"));

        stateCB = new JCheckBox();
        stateCB.setVisible(false);
        stateCB.addActionListener(this);

        infoPanel.setLayout(new GridLayout(rows, columns, space, space));
        
        infoLabel = new JLabel(); 
        infoLabel.setText("<html><pre><i>Click Add to make customers</i></pre></html>");
        infoPanel.add(infoLabel);
        infoPanel.add(stateCB);
        mainPanel.add(infoPanel);
        
        // Set up idPanel
        Dimension bottomDim= new Dimension(windowX, bottomDimY);
        bottomPanel= new JPanel();
        bottomPanel.setLayout(new GridLayout(rows, columns));
        bottomPanel.setPreferredSize(bottomDim);
        bottomPanel.setMinimumSize(bottomDim);
        bottomPanel.setMaximumSize(bottomDim);

        waiter= new ListPanel4(restPanel, "Waiters");
        customerPanel = new ListPanel4(restPanel, "Customers");
        bottomPanel.add(customerPanel);
        bottomPanel.add(waiter);
        
        mainPanel.add(bottomPanel);
        
        // Set up main Panel
        Dimension mainDim= new Dimension(WINDOWX, WINDOWY);
        mainPanel.setPreferredSize(mainDim);
        mainPanel.setMinimumSize(mainDim);
        mainPanel.setMaximumSize(mainDim);
        mainPanel.setVisible(true);
        add(mainPanel);
    }
    /**
     * updateInfoPanel() takes the given customer (or, for v3, Host) object and
     * changes the information panel to hold that person's info.
     *
     * @param person customer (or waiter) object
     */
    public void updateInfoPanel(Object person, boolean status) {
        stateCB.setVisible(true);
        currentPerson = person;

        if (person instanceof CustomerRole4) {
            CustomerRole4 customer = (CustomerRole4) person;
            if(status == true){
            	stateCB.setSelected(true);
            	customer.getGui().setHungry();
            	stateCB.setEnabled(false);
            }
            stateCB.setText("Hungry?");
          //Should checkmark be there? 
            stateCB.setSelected(customer.getGui().isHungry());
          //Is customer hungry? Hack. Should ask customerGui
            stateCB.setEnabled(!customer.getGui().isHungry());
          // Hack. Should ask customerGui
            infoLabel.setText(
               "<html><pre>     Name: " + customer.getName() + " </pre></html>");
        }
        if (person instanceof WaiterRole4) {
        	WaiterRole4 waiter = (WaiterRole4) person;
        	if(status == true){
            	//System.out.println("updateInfoPanel: status = true");
        		stateCB.setSelected(true);
            	stateCB.setEnabled(false);
            }
            stateCB.setText("On Break?");
          //Should checkmark be there? 
            stateCB.setSelected(waiter.isOnBreak());
          //Is customer hungry? Hack. Should ask customerGui
            stateCB.setEnabled(!waiter.isOnBreak());
          // Hack. Should ask customerGui
            infoLabel.setText(
               "<html><pre>     Name: " + waiter.getName() + " </pre></html>");
        }
        infoPanel.validate();
    }
    /**
     * Action listener method that reacts to the checkbox being clicked;
     * If it's the customer's checkbox, it will make him hungry
     * For v3, it will propose a break for the waiter.
     */
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == stateCB) {
            if (currentPerson instanceof CustomerRole4) {
                CustomerRole4 c = (CustomerRole4) currentPerson;
                c.getGui().setHungry();
                stateCB.setEnabled(false);
            }
            if (currentPerson instanceof WaiterRole4) {
            	WaiterRole4 w = (WaiterRole4) currentPerson;
            	w.msgWantBreak();
            	stateCB.setEnabled(false);
            }
        }
        if (e.getSource() == pause){
        	if(paused == false){
        		pause.setText("Resume");
        		paused= true;
        		restPanel.pause();
        	}
        	else if(paused == true){
        		pause.setText("Pause");
        		paused= false;
        		restPanel.restart();
        	}
        }
    }
    /**
     * Message sent from a customer gui to enable that customer's
     * "I'm hungry" checkbox.
     *
     * @param c reference to the customer
     */
    public void setCustomerEnabled(CustomerRole4 c) {
        if (currentPerson instanceof CustomerRole4) {
            CustomerRole4 cust = (CustomerRole4) currentPerson;
            if (c.equals(cust)) {
                stateCB.setEnabled(true);
                stateCB.setSelected(false);
            }
        }
    }
    
    public void setWaiterEnabled(WaiterRole4 w, boolean onBreak){
    	if(currentPerson instanceof WaiterRole4) {
    			System.out.println("Checking the waiter break state");
    			stateCB.setEnabled(!onBreak);
    			stateCB.setSelected(onBreak);
    	}
    }
    
    /**
     * Main routine to get gui started
     */
    public static void main(String[] args) {
        RestaurantGui4 gui = new RestaurantGui4();
        gui.setTitle("csci201 Restaurant");
        gui.setVisible(true);
        gui.setResizable(false);
        gui.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
}

