package city.gui.restaurant4;

import javax.swing.*;

import city.Restaurant4.WaiterRole4;
import sun.management.Agent;

import java.awt.*;
import java.awt.event.*;
import java.util.List;
import java.util.ArrayList;

/**
 * Subpanel of restaurantPanel.
 * This holds the scroll panes for the customers and, later, for waiters
 */
public class ListPanel4 extends JPanel implements ActionListener {

    private static int X_MAX= 200;
    private static int Y_MAX= 20;
    private static int PANE_Y_MAX= 150;
    private static int cDividePane= 5;
    private static int wDividePane= 6;
    private static int width= 20;
	
	public JScrollPane cPane =
            new JScrollPane(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
                    JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
    private JPanel cView = new JPanel();
    public JScrollPane wPane =
            new JScrollPane(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
                    JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
    private JPanel wView = new JPanel();
    private List<JButton> list = new ArrayList<JButton>();
    private JButton addPersonB = new JButton("Add Customer");
    private JButton addWaiter= new JButton("Add Waiter");
    private JLabel nameLabel= new JLabel("Name: ");
    private JTextField cNameField = new JTextField();
    private JTextField wNameField = new JTextField();
    private JCheckBox hungry= new JCheckBox("Hungry?");

    private RestaurantPanel4 restPanel;
    private String type;
    private boolean hunger= false;
    private boolean onBreak= false;

    /**
     * Constructor for ListPanel.  Sets up all the gui
     *
     * @param rp   reference to the restaurant panel
     * @param type indicates if this is for customers or waiters
     */
    public ListPanel4(RestaurantPanel4 rp, String type) {
        restPanel = rp;
        this.type = type;

        if(type == "Customers"){
        	setLayout(new BoxLayout((Container) this, BoxLayout.Y_AXIS));
        	add(new JLabel("<html><pre> <u>" + type + "</u><br></pre></html>"));
        	add(nameLabel);
        	Dimension size = new Dimension(X_MAX, Y_MAX);
        	cNameField.setMaximumSize(size);
        	cNameField.addKeyListener(new KeyListener() {
        		@Override
        		public void keyReleased(KeyEvent e){
        			try{
        				String text = new String();
        				text= cNameField.getText();
        				if(text.length() > 0){
        					hungry.setEnabled(true);
        				}
        				else{
        					hungry.setEnabled(false);
        					hungry.setSelected(false);
        				}
        			} catch(Exception exept) {
        				hungry.setEnabled(false);
        				return;
        			}
        		}
        	
        		@Override
        		public void keyTyped(KeyEvent e){};
        	
        		@Override
        		public void keyPressed(KeyEvent e){};
        	});
        	add(cNameField);
        	hungry.addActionListener(this);
        	hungry.setEnabled(false);
        	add(hungry);
        	addPersonB.addActionListener(this);
        	add(addPersonB);
        
        	cView.setLayout(new BoxLayout((Container) cView, BoxLayout.Y_AXIS));
        	cPane.setViewportView(cView);
        	add(cPane);
        }
        else if(type == "Waiters"){
            setLayout(new BoxLayout((Container) this, BoxLayout.Y_AXIS));
            add(new JLabel("<html><pre> <u>" + type + "</u><br></pre></html>"));
            add(nameLabel);
            Dimension size = new Dimension(X_MAX, Y_MAX);
            wNameField.setMaximumSize(size);
            add(wNameField);
            addWaiter.addActionListener(this);
            add(addWaiter);
            
            Dimension paneSize= new Dimension(X_MAX, PANE_Y_MAX);
            wView.setLayout(new BoxLayout((Container) wView, BoxLayout.Y_AXIS));
            wPane.setPreferredSize(paneSize);
            wPane.setMaximumSize(paneSize);
            wPane.setMinimumSize(paneSize);
            wPane.setViewportView(wView);
            add(wPane);
        }
    }

    /**
     * Method from the ActionListener interface.
     * Handles the event of the add button being pressed
     */
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == hungry){
        	hunger= true;
        }
    	if (e.getSource() == addPersonB) {
        	addPerson();
        }
    	if (e.getSource() == addWaiter){
    		addWaiter();
    	}
        else {
        	for (JButton temp:list){
                if ( e.getSource().equals(temp) )
                    restPanel.showInfo(type, temp.getText(), false);
            }
        }
    }

    
    
    /**
     * If the add button is pressed, this function creates
     * a spot for it in the scroll pane, and tells the restaurant panel
     * to add a new person.
     *
     * @param name name of new person
     */
    //public void addPerson(String name) {
    public void addPerson(){
        /*String name = cNameField.getText();
    	if (name != null) {
    	    JButton button = new JButton(name);
            button.setBackground(Color.white);

            Dimension paneSize = cPane.getSize();
            int newWidth= paneSize.width - width;
            int newHeight= (int) (paneSize.height / cDividePane);
            Dimension buttonSize = new Dimension(newWidth, newHeight);
            button.setPreferredSize(buttonSize);
            button.setMinimumSize(buttonSize);
            button.setMaximumSize(buttonSize);
            button.addActionListener(this);
            list.add(button);
            cView.add(button);
            restPanel.addPerson(type, name, hunger);//puts customer on list
            restPanel.showInfo(type, name, hunger);//puts hungry button on panel
            hunger= false;//update hunger boolean
            hungry.setSelected(false);
            validate();
        }*/
    }
    
    public void addWaiter(){
        /*String name = wNameField.getText();
        boolean okay= true;
        for(WaiterRole4 w : restPanel.waiters){
        	if(w.getName().equals(name)){
        		okay= false;
        	}
        }
    	if (okay) {
    	    JButton button = new JButton(name);
            button.setBackground(Color.white);

            Dimension paneSize = wPane.getSize();
            int newWidth= paneSize.width - width;
            int newHeight= (int) (paneSize.height / wDividePane);
            Dimension buttonSize = new Dimension(newWidth, newHeight);
            button.setPreferredSize(buttonSize);
            button.setMinimumSize(buttonSize);
            button.setMaximumSize(buttonSize);
            button.addActionListener(this);
            list.add(button);
            wView.add(button);
            restPanel.addWaiter(wNameField.getText());
            restPanel.showInfo(type, name, onBreak);
            validate();
        }*/
    }
}
