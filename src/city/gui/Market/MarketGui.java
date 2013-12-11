package city.gui.Market;

//import javax.swing.Image;
import javax.swing.*;
import city.Restaurant3.CustomerRole3;
import city.Restaurant3.WaiterRole3;
import city.gui.Restaurant3.*;
import java.awt.*;
import java.awt.event.*;

/**
 * Main GUI class.
 * Contains the main frame and subsequent panels
 */
public class MarketGui extends JFrame implements ActionListener {

	AnimationPanel3 animationPanel = new AnimationPanel3();    private JPanel infoPanel;
    private JLabel infoLabel;
    private JCheckBox stateCB;
    
    private static final int WINDOW_BOUND = 50;
    private static final int INFO_PANEL_ROWS = 1;
    private static final int INFO_PANEL_COLS = 2;
    private static final int INFO_PANEL_X_PADDING = 30;
    private static final int INFO_PANEL_Y_PADDING = 0;
    private static final double HALF = .5;
    private static final double ONE_THIRD = .125;

    private Object currentPerson;
    
	private JPanel controlPanel;
    private JButton toggleTimerButton = new JButton("Pause");

    /**
     * Constructor for RestaurantGui class.
     * Sets up all the gui components.
     */
    public MarketGui() {
    	
        int WINDOWX = 1000;
        int WINDOWY = 550;
    	
    	setBounds(WINDOW_BOUND, WINDOW_BOUND, WINDOWX, WINDOWY);

        setLayout(new BoxLayout((Container) getContentPane(), BoxLayout.X_AXIS));

        JPanel leftPanel = new JPanel();
        leftPanel.setLayout(new BoxLayout(leftPanel, BoxLayout.Y_AXIS));
        
        Dimension leftDim = new Dimension((int) (WINDOWX * HALF), WINDOWY);
        leftPanel.setPreferredSize(leftDim);
        leftPanel.setMinimumSize(leftDim);
        leftPanel.setMaximumSize(leftDim);
        leftPanel.setBorder(BorderFactory.createTitledBorder("Settings & Controls"));
        
        Dimension infoDim = new Dimension(WINDOWX, (int) (WINDOWY * ONE_THIRD)); // Takes up .125 of the height of the window in pixels
        infoPanel = new JPanel();
        infoPanel.setPreferredSize(infoDim);
        infoPanel.setMinimumSize(infoDim);
        infoPanel.setMaximumSize(infoDim);
        infoPanel.setBorder(BorderFactory.createTitledBorder("Information"));

        stateCB = new JCheckBox();
        stateCB.setVisible(false);
        stateCB.addActionListener(this);

        infoPanel.setLayout(new GridLayout(INFO_PANEL_ROWS, INFO_PANEL_COLS, INFO_PANEL_X_PADDING, INFO_PANEL_Y_PADDING));
        
        infoLabel = new JLabel(); 
        infoLabel.setText("<html><p>Enter a customer or waiter name above and click add to begin!</p></html>");
        infoPanel.add(infoLabel);
        infoPanel.add(stateCB);
        leftPanel.add(infoPanel);
        
        add(leftPanel);
        animationPanel.setBorder(BorderFactory.createTitledBorder("Restaurant Animation"));
        add(animationPanel);
        
        controlPanel = new JPanel();
        Dimension controlPanelDim = new Dimension(WINDOWX, (int) (WINDOWY * ONE_THIRD));
        controlPanel.setPreferredSize(controlPanelDim);
        controlPanel.setMinimumSize(controlPanelDim);
        controlPanel.setMaximumSize(controlPanelDim);
        controlPanel.setBorder(BorderFactory.createTitledBorder("Agent Controls"));
        toggleTimerButton.addActionListener(this);
        controlPanel.add(toggleTimerButton);
        leftPanel.add(controlPanel);
        
    }
    
    public void updateInfoPanel(Object person) {
        stateCB.setVisible(true);
        currentPerson = person;
    
    }
    public void actionPerformed(ActionEvent e) {
        
    }
    
    public void setCustomerEnabled(CustomerRole3 c) {
        if (currentPerson instanceof CustomerRole3) {
            CustomerRole3 cust = (CustomerRole3) currentPerson;
            if (c.equals(cust)) {
                stateCB.setEnabled(true);
                stateCB.setSelected(false);
            }
        }
    }
    
    public void setCbEnabled(WaiterRole3 w) {
        if (currentPerson instanceof WaiterRole3) {
        	WaiterRole3 waiter = (WaiterRole3) currentPerson;
            if (w.equals(waiter)) {
                stateCB.setEnabled(true);
                stateCB.setSelected(false);
            }
        }
    }
    
    public static void main(String[] args) {
        RestaurantGui3 gui = new RestaurantGui3();
        gui.setTitle("Market");
        gui.setVisible(true);
        gui.setResizable(false);
        gui.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
}
