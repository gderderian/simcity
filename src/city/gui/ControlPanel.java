package city.gui;

import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Semaphore;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;

import city.CityMap;
import astar.AStarTraversal;

public class ControlPanel extends JPanel implements ActionListener{
	
	public JScrollPane pane = new JScrollPane(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
    private JPanel view = new JPanel();
    private List<JButton> list = new ArrayList<JButton>();
    private JButton addPersonB = new JButton("Add");
    private JTabbedPane controlPane = new JTabbedPane();
    private JPanel worldControls = new JPanel();
    
    private int WINDOWX = 370;
    private int WINDOWY = 750;
    
    private Dimension scrollDim = new Dimension(WINDOWX, WINDOWY/4);
    private Dimension panelDim = new Dimension(WINDOWX, WINDOWY);

    private JTextField nameField;
    private JTextField jobField;
    private JPanel personControls = new JPanel();
    public JCheckBox isHungry;
    public JCheckBox takeBreak;
    
    /** Universal city map **/
    CityMap cityMap = new CityMap();
    
    //Size of astar semaphore grid
    static int gridX = 21; //# of x-axis tiles
    static int gridY = 18; //# of y-axis tiles

    //Semaphore grid for astar animation
    Semaphore[][] streetGrid = new Semaphore[gridX+1][gridY+1];
    Semaphore[][] sidewalkGrid = new Semaphore[gridX+1][gridY+1];
    
    CityGui cityGui;

    /**
     * Constructor for ListPanel.  Sets up all the gui
     *
     * @param rp   reference to the restaurant panel
     * @param type indicates if this is for customers or waiters
     */
    public ControlPanel() {

        view.setLayout(new FlowLayout());
        setLayout(new BoxLayout((Container) this, BoxLayout.PAGE_AXIS));

        setBorder(BorderFactory.createLineBorder(Color.black, 5));
        
        addPersonSection();
        
        controlPane.setPreferredSize(panelDim);
        //worldControls.setMaximumSize(panelDim);
        worldControls.setPreferredSize(panelDim);
        controlPane.addTab("People", personControls);
        controlPane.addTab("World", worldControls);
        add(controlPane);
        
        List<String> stopLocations0 = new ArrayList<String>();
        List<String> stopLocations1 = new ArrayList<String>();
        List<String> stopLocations2 = new ArrayList<String>();
        List<String> stopLocations3 = new ArrayList<String>();
        
        //Add all nearby locations here
        stopLocations0.add("building0");
        stopLocations1.add("building1");
        stopLocations2.add("building2");
        stopLocations3.add("building3");
        
        cityMap.addStopDestinations(0, stopLocations0);
        cityMap.addStopDestinations(0, stopLocations0);
        cityMap.addStopDestinations(0, stopLocations0);
        cityMap.addStopDestinations(0, stopLocations0);
        
        
        
        /*********Setting up semaphore grid***********/
      	for (int i = 0; i <= gridX; i++) {
    	    for (int j = 0; j <= gridY; j++) {
    	    	streetGrid[i][j] = new Semaphore(0,true);
    	    	sidewalkGrid[i][j] = new Semaphore(0,true);
    	    }
      	}
      	
      	//Releasing all roads and sidewalks so guis can move around on them.
      	//First, the roads
      	for(int i = 4; i < 20; i++) {
      		for(int j = 4; j < 8; j++)
      			streetGrid[i][j].release();
      		for(int j = 13; j < 17; j++)
      			streetGrid[i][j].release();
      	}
      	
      	//Release sidewalk semaphores
      	for(int i = 1; i < 21; i++) { //Top and bottom
      		for(int j = 1; j < 3; j++)
      			sidewalkGrid[i][j].release();
      		for(int j = 16; j < 18; j++)
      			sidewalkGrid[i][j].release();
      	}

      	for(int i = 3; i < 16; i++) { //Left and right
      		for(int j = 1; j < 3; j++)
      			sidewalkGrid[j][i].release();
      		for(int j = 19; j < 21; j++)
      			sidewalkGrid[j][i].release();
      	}
      	
      	for(int i = 7; i < 15; i++)
      		for(int j = 10; j < 12; j++)
      			sidewalkGrid[i][j].release();
      	
      	//End of sidewalk grid releasing
      	
      	//Adding in crosswalks (shared semaphores between street grid and sidewalk grid)
      	for(int i = 15; i < 19; i++) //Bottom right crosswalk
      		for(int j = 16; j < 18; j++)
      			sidewalkGrid[i][j] = streetGrid[i][j];
      	for(int i = 13; i < 15; i++) //Crosswalk to island
      		for(int j = 12; j < 16; j++)
      			sidewalkGrid[i][j] = streetGrid[i][j];
      	
      	/********Finished setting up semaphore grid***********/
    }
    
    public void setCityGui(CityGui c){
    	cityGui = c;
    }
    
    private void addPersonSection(){
    	personControls.add(new JLabel("<html><br><u>Add People</u><br></html>"));
    	
    	personControls.setPreferredSize(panelDim);
        
        pane.setMinimumSize(scrollDim);
        pane.setMaximumSize(scrollDim);
        pane.setPreferredSize(scrollDim);
        
        //set layout of control panel
        FlowLayout flow = new FlowLayout();
        personControls.setLayout(new BoxLayout(personControls, BoxLayout.PAGE_AXIS));
        
        //Adding enter name section
        personControls.add(new JLabel("Name:"));
        nameField = new JTextField();
        nameField.setColumns(16);
        personControls.add(nameField, flow);
        
        //Adding enter job section
        personControls.add(new JLabel("Job: "));
        jobField = new JTextField();
        jobField.setColumns(16);
        personControls.add(jobField, flow);
        
        isHungry = new JCheckBox("Hungry?");
        isHungry.setEnabled(false);
        isHungry.addActionListener(this);
        //enterNames.add(isHungry);
        
        nameField.addKeyListener(new KeyListener(){

			@Override
			public void keyPressed(KeyEvent arg0) {
				if(nameField.getText().length() > 0){
					isHungry.setEnabled(true);
				}
				else{
					isHungry.setEnabled(false);
				}
			}

			@Override
			public void keyReleased(KeyEvent arg0) {
				if(nameField.getText().length() > 0){
					isHungry.setEnabled(true);
				}
				else{
					isHungry.setEnabled(false);
				}
			}

			@Override
			public void keyTyped(KeyEvent arg0) {
				if(nameField.getText().length() > 0){
					isHungry.setEnabled(true);
				}
				else{
					isHungry.setEnabled(false);
				}
			}
        	
        });

        addPersonB.addActionListener(this);
        
        personControls.add(addPersonB, flow);

        view.setLayout(new BoxLayout((Container) view, BoxLayout.Y_AXIS));
        this.add(personControls);
        pane.setViewportView(view);
        //add(pane);
    	
    }
    

    /**
     * Method from the ActionListener interface.
     * Handles the event of the add button being pressed
     */
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == addPersonB) {
        	// Chapter 2.19 describes showInputDialog()
        	if(!nameField.getText().equals("")){
        		String job = null;
        		if(!jobField.getText().equals("")){
        			job = jobField.getText();
        		}
                addPerson(nameField.getText(), job);
            	nameField.setText("");
            	isHungry.setSelected(false);
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
    public void addPerson(String name, String job) {
        if (name != null) {
            JButton button = new JButton(name);
            button.setBackground(Color.white);
            
            AStarTraversal aStarTraversal = new AStarTraversal(sidewalkGrid);
            
            cityGui.addPerson(name, aStarTraversal, job);

            Dimension paneSize = pane.getSize();
            Dimension buttonSize = new Dimension((paneSize.width - 20),
                    (int) (paneSize.height / 7));
            button.setPreferredSize(buttonSize);
            button.setMinimumSize(buttonSize);
            button.setMaximumSize(buttonSize);
            button.addActionListener(this);
            list.add(button);
            view.add(button);
            isHungry.setEnabled(false);
            validate();
        }
    }
	
}