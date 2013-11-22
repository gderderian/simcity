package city.gui.Bank;


import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
/**
 * Main GUI class.
 * Contains the main frame and subsequent panels
 */
public class BankGui extends JFrame implements ActionListener {
    /* The GUI has two frames, the control frame (in variable gui) 
     * and the animation frame, (in variable animationFrame within gui)
     */
	//JFrame animationFrame = new JFrame("Restaurant Animation");
	BankAnimationPanel animationPanel = new BankAnimationPanel();
	
    /* restPanel holds 2 panels
     * 1) the staff listing, menu, and lists of current customers all constructed
     *    in RestaurantPanel()
     * 2) the infoPanel about the clicked Customer (created just below)
     */    
	
    private BankPanel restPanel = new BankPanel(this);
    
    /* infoPanel holds information about the clicked customer, if there is one*/
    private JPanel infoPanel;
    private JLabel infoLabel; //part of infoPanel
    private JCheckBox stateCB;//part of infoLabel
    private JButton pausebutton;
    private JButton waiterbreakbutton;
    private JButton waitercomebackfrombreakbutton;
    private boolean waiteronbreak = false;
    private JButton depletecooksupply;
    private JButton depletemarket1supply;
    private JButton depletemarket2supply;
    private Object currentPerson;/* Holds the agent that the info is about.
    							Seems like a hack */
    //getting rid of magic numbers here
    boolean restart = false;
    
    private static int Xbounds = 50;
    private static int Ybounds = 50;
    /**
     * Constructor for RestaurantGui class.
     * Sets up all the gui components.
     */
    public BankGui() {
        int WINDOWX = 650;
        int WINDOWY = 600;

    	setLayout(new BorderLayout());
    	

    	
        Dimension restDim = new Dimension(400, (int) (400));
        restPanel.setPreferredSize(restDim);
        
        // Now, setup the info panel
        Dimension infoDim = new Dimension(400, (int) (200));
        infoPanel = new JPanel();
        infoPanel.setPreferredSize(infoDim);
        infoPanel.setBorder(BorderFactory.createTitledBorder("Information"));

        stateCB = new JCheckBox();
        stateCB.setVisible(false);
        stateCB.addActionListener(this);
        
        
        
        infoPanel.setLayout(new GridLayout(2, 3, 10, 0));
        infoLabel = new JLabel();
        
        waiterbreakbutton = new JButton("waiter go on break");
        waiterbreakbutton.addActionListener(this);
        
        waitercomebackfrombreakbutton = new JButton("waiter come back");
        waitercomebackfrombreakbutton.addActionListener(this);
        
        depletecooksupply = new JButton("deplete cook supply");
        depletecooksupply.addActionListener(this);
        
        depletemarket1supply = new JButton("deplete market 1 supply");
        depletemarket1supply.addActionListener(this);
        
        depletemarket2supply = new JButton("deplete market 2 supply");
        depletemarket2supply.addActionListener(this);
        
        pausebutton = new JButton("Pause");
        pausebutton.addActionListener(this);
        infoLabel.setText("<html><pre><i>Click Add to make customers</i></pre></html>");
        infoPanel.add(infoLabel);
        infoPanel.add(stateCB);
        infoPanel.add(pausebutton);
        //infoPanel.add(waiterbreakbutton);
        //infoPanel.add(waitercomebackfrombreakbutton);
        infoPanel.add(depletecooksupply);
        infoPanel.add(depletemarket1supply);
        infoPanel.add(depletemarket2supply);
    	JPanel controlPanel = new JPanel();
    	controlPanel.setLayout(new BoxLayout(controlPanel, BoxLayout.Y_AXIS));
    	controlPanel.setPreferredSize(new Dimension(600, (int) 800));
    	controlPanel.add(restPanel);
    	controlPanel.add(infoPanel);
    	animationPanel.setPreferredSize(new Dimension(800, (int) 800));
        add(controlPanel, BorderLayout.WEST);
        add(animationPanel, BorderLayout.CENTER);
        
    }
    /**
     * updateInfoPanel() takes the given customer (or, for v3, Host) object and
     * changes the information panel to hold that person's info.
     *
     * @param person customer (or waiter) object
     */
    public void updateInfoPanel(Object person) {
        stateCB.setVisible(true);
        currentPerson = person;

        if (person instanceof CustomerAgent) {
            CustomerAgent customer = (CustomerAgent) person;
            stateCB.setText("Hungry?");
          //Should checkmark be there? 
            stateCB.setSelected(customer.getGui().isHungry());
          //Is customer hungry? Hack. Should ask customerGui
            stateCB.setEnabled(!customer.getGui().isHungry());
          // Hack. Should ask customerGui
            infoLabel.setText(
               "<html><pre>     Name: " + customer.getName() + " </pre></html>");
            
        }
        else if (person instanceof WaiterAgent) {
            WaiterAgent waiter = (WaiterAgent) person;
            stateCB.setText("Go on break");
          //Should checkmark be there? 
            stateCB.setSelected(waiter.getGui().onBreak());
          //Is customer hungry? Hack. Should ask customerGui
            //stateCB.setEnabled(!waiter.getGui().onBreak());
            stateCB.setEnabled(true);
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
            if (currentPerson instanceof CustomerAgent) {
                CustomerAgent c = (CustomerAgent) currentPerson;
                c.getGui().setHungry();
                stateCB.setEnabled(false);
            }
            
            else if(currentPerson instanceof WaiterAgent) {
            	WaiterAgent w = (WaiterAgent) currentPerson;
            	if(waiteronbreak == false)
            	{
            		System.out.println("waiter on break");
            		w.getGui().setOnBreak();
            		waiteronbreak = true;
            		stateCB.setEnabled(true);
            		//stateCB.setSelected(false);
            	}
            	else if(waiteronbreak == true)
            	{
            		System.out.println("waiter off break");
            		waiteronbreak = false;
            		w.getGui().setOffBreak();
            		stateCB.setEnabled(true);
            		//stateCB.setSelected(false);	
            	}
            	//stateCB.setEnabled(false);
            }
        }
        if(e.getSource() == pausebutton) { 	  
        	if(restart == false) {
        		System.out.println("pause button called");
        		  restPanel.pauseagents();
        		  restart = true;
        	  }
        	  else if(restart == true)
        	  {
        		restPanel.restartagents();
        		restart = false;
        	  }
        }
        
    
        
        if(e.getSource() == depletecooksupply)
        {
        	System.out.println("deplete cook's supply");
        	restPanel.depletecooksupply();
        }
        
        if(e.getSource() == depletemarket1supply)
        {
        	System.out.println("deplete market 1 supply");
        	restPanel.depletemarket1supply();
        }
        
        if(e.getSource() == depletemarket2supply)
        {
        	System.out.println("deplete market 2 supply");
        	restPanel.depletemarket2supply();
        }
        
       
        
    }
    /**
     * Message sent from a customer gui to enable that customer's
     * "I'm hungry" checkbox.
     *
     * @param c reference to the customer
     */
    public void setCustomerEnabled(CustomerAgent c) {
        if (currentPerson instanceof CustomerAgent) {
            CustomerAgent cust = (CustomerAgent) currentPerson;
            if (c.equals(cust)) {
                stateCB.setEnabled(true);
                stateCB.setSelected(false);
            }
        }
    }
    /**
     * Main routine to get gui started
     */
    public static void main(String[] args) {
        RestaurantGui gui = new RestaurantGui();
        gui.setTitle("csci201 Restaurant");
        gui.setVisible(true);
        gui.setResizable(false);
        gui.setSize(1200,600); //1200
        gui.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
}
