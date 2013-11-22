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
import javax.swing.JTextField;

import city.CityMap;

import astar.AStarTraversal;

public class ControlPanel extends JPanel implements ActionListener{
	
	public JScrollPane pane = new JScrollPane(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
    private JPanel view = new JPanel();
    private List<JButton> list = new ArrayList<JButton>();
    private JButton addPersonB = new JButton("Add");
    
    private int WINDOWX = 370;
    private int WINDOWY = 750;
    
    private Dimension scrollDim = new Dimension(WINDOWX, WINDOWY/4);

    private JTextField nameField;
    private JPanel enterNames = new JPanel();
    public JCheckBox isHungry;
    public JCheckBox takeBreak;
    
    /** Universal city map **/
    CityMap cityMap = new CityMap();
    
    //Size of astar semaphore grid
    static int gridX = 45; //# of x-axis tiles
    static int gridY = 35; //# of y-axis tiles

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
        
        pane.setMinimumSize(scrollDim);
        pane.setMaximumSize(scrollDim);
        pane.setPreferredSize(scrollDim);

        setBorder(BorderFactory.createLineBorder(Color.black, 5));
        
        addPersonSection();
                
        
        /*********Setting up semaphore grid***********/
      	for (int i = 0; i <= gridX; i++) {
    	    for (int j = 0; j <= gridY; j++) {
    	    	streetGrid[i][j] = new Semaphore(0,true);
    	    	sidewalkGrid[i][j] = new Semaphore(0,true);
    	    }
      	}
      	
      	//Releasing all roads and sidewalks so guis can move around on them.
      	//First, the roads
      	for(int i = 8; i < 39; i++) 
      		for(int j = 8; j < 13; j++)
      			streetGrid[i][j].release();
      	for(int i = 8; i < 39; i++) 
      		for(int j = 24; j < 29; j++)
      			streetGrid[i][j].release();
      	for(int i = 8; i < 13; i++) 
      		for(int j = 13; j < 24; j++)
      			streetGrid[i][j].release();
      	for(int i = 34; i < 39; i++) 
      		for(int j = 13; j < 24; j++)
      			streetGrid[i][j].release();
      	for(int i = 34; i < 39; i++) 
      		for(int j = 39; j < 36; j++)
      			streetGrid[i][j].release();
      	//End of street grid releasing
      	
      	for(int i = 6; i < 8; i++) //This loop covers the leftmost side of sidewalk.
      		for(int j = 6; j < 31; j++)
      			sidewalkGrid[i][j].release();
      	for(int i = 39; i < 41; i++) //This loop covers the rightmost side of sidewalk.
      		for(int j = 6; j < 36; j++)
      			sidewalkGrid[i][j].release();
      	for(int i = 8; i < 39; i++) //This loop covers the top side of sidewalk.
      		for(int j = 6; j < 8; j++)
      			sidewalkGrid[i][j].release();
      	for(int i = 29; i < 31; i++) //This loop covers the bottom side of sidewalk.
      		for(int j = 8; j < 34; j++)
      			sidewalkGrid[i][j].release();      	
      	for(int i = 32; i < 34; i++) //This loop covers the extra portion on entrance street sidewalk
      		for(int j = 31; j < 36; j++)
      			sidewalkGrid[i][j].release();

      	for(int i = 13; i < 24; i++) { //Inner sidewalk left and right.
      		for(int j = 13; j < 15; j++)
      			sidewalkGrid[j][i].release();
      		for(int k = 32; k < 34; k++)
      			sidewalkGrid[k][i].release();
      	}      
      	for(int i = 15; i < 31; i++) { //Inner sidewalk top and bottom.
      		for(int j = 13; j < 15; j++)
      			sidewalkGrid[i][j].release();
      		for(int k = 22; k < 24; k++)
      			sidewalkGrid[i][k].release();
      	}
      	//End of sidewalk grid releasing
      	
      	//Adding in crosswalks (shared semaphores between street grid and sidewalk grid)
      	/*for(int i = 13; i < 15; i++) //Top left crosswalk
      		for(int j = 8; j < 13; j++)
      			sidewalkGrid[i][j] = streetGrid[i][j];
      	for(int i = 34; i < 39; i++) //Top right crosswalk
      		for(int j = 13; j < 15; j++)
      			sidewalkGrid[i][j] = streetGrid[i][j];
      	for(int i = 8; i < 13; i++) //Bottom left crosswalk
      		for(int j = 22; j < 24; j++)
      			sidewalkGrid[i][j] = streetGrid[i][j];
      	for(int i = 32; i < 34; i++) //Bottom right crosswalk
      		for(int j = 24; j < 29; j++)
      			sidewalkGrid[i][j] = streetGrid[i][j];   */
      	
      	/********Finished setting up semaphore grid***********/
    }
    
    public void setCityGui(CityGui c){
    	cityGui = c;
    }
    
    private void addPersonSection(){
    	add(new JLabel("<html><br><u>Add People</u><br></html>"));
        
        FlowLayout flow = new FlowLayout();
        
        enterNames.add(new JLabel("Name:"), flow);
        
        nameField = new JTextField();
        nameField.setColumns(16);
        enterNames.add(nameField, flow);
        
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
       
        enterNames.add(addPersonB, flow);

        view.setLayout(new BoxLayout((Container) view, BoxLayout.Y_AXIS));
        this.add(enterNames);
        pane.setViewportView(view);
        add(pane);
    	
    }
    

    /**
     * Method from the ActionListener interface.
     * Handles the event of the add button being pressed
     */
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == addPersonB) {
        	// Chapter 2.19 describes showInputDialog()
        	if(!nameField.getText().equals("")){
                addPerson(nameField.getText());
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
    public void addPerson(String name) {
        if (name != null) {
            JButton button = new JButton(name);
            button.setBackground(Color.white);
            
            AStarTraversal aStarTraversal = new AStarTraversal(sidewalkGrid);
            
            cityGui.addPerson(name, aStarTraversal);

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