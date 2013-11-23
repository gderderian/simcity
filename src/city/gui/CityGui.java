package city.gui;

import interfaces.Restaurant2Waiter;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import astar.AStarTraversal;

import city.gui.restaurant4.AnimationPanel4;

import city.PersonAgent;
import city.gui.restaurant2.Restaurant2AnimationPanel;

import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
/**
 * Main GUI class.
 * Contains the main frame and subsequent panels
 */
public class CityGui extends JFrame implements ActionListener, ChangeListener {
  
    AnimationPanel animationPanel = new AnimationPanel();
    
    ControlPanel controlPanel = new ControlPanel();
    
        Restaurant2AnimationPanel restaurant2 = new Restaurant2AnimationPanel();
        AnimationPanel4 restaurant4 = new AnimationPanel4();
        //PersonAgent testPerson = new PersonAgent("test");
        //PersonGui testPersonGui = new PersonGui();
    
    private JPanel infoPanel;
        
    private final int WINDOWX = 1300;
    private final int WINDOWY = 750;
    private final int ANIMATIONX = 900;
    private final int WINDOW_X_COORD = 50;
    private final int WINDOW_Y_COORD = 50;
    
    ArrayList<Gui> guis = new ArrayList<Gui>();
    ArrayList<PersonAgent> people = new ArrayList<PersonAgent>();
    
    /**
     * Constructor for RestaurantGui class.
     * Sets up all the gui components.
     */
    public CityGui() {            
            controlPanel.setCityGui(this);
   
        //testPerson.startThread();
        //testPerson.setGui(testPersonGui);
        //testPersonGui.addAnimationPanel(restaurant2);
        //guis.add(testPersonGui);
        //cityPanel.addGui(testPersonGui);
                        
            setBounds(WINDOW_X_COORD, WINDOW_Y_COORD, WINDOWX, WINDOWY);

            setLayout(new BorderLayout());
            
            animationPanel.setCityGui(this);
            //cityPanel.setBackground(Color.LIGHT_GRAY); //To see where it is for now
            restaurant2.setBackground(new Color(150, 20, 60));
            restaurant2.setCityGui(this);
            
            restaurant4.setCityGui(this);

        Dimension animationDim = new Dimension(ANIMATIONX, WINDOWY);
        //cityPanel.setPreferredSize(animationDim);
        restaurant2.setPreferredSize(animationDim);
        //add(cityPanel, BorderLayout.EAST);
        animationPanel.setPreferredSize(animationDim);
    	
    	add(animationPanel, BorderLayout.EAST);

        Dimension panelDim = new Dimension(WINDOWX - ANIMATIONX, WINDOWY);
        infoPanel = new JPanel();
        infoPanel.setPreferredSize(panelDim);
        infoPanel.setMinimumSize(panelDim);
        infoPanel.setMaximumSize(panelDim);
        infoPanel.setBorder(BorderFactory.createTitledBorder("Information"));
        
        infoPanel.setLayout(new FlowLayout());
        
        //add(infoPanel, BorderLayout.WEST);
        add(controlPanel, BorderLayout.WEST);

    }
    
    public void addGui(Gui g){
            guis.add(g);
    }
    
    public void addPerson(PersonAgent p){
            people.add(p);
    }

    public void actionPerformed(ActionEvent e) {
            //if(e.getSource() == 
    }
    /**
     * Main routine to get gui started
     */
    public static void main(String[] args) {
        CityGui gui = new CityGui();
        gui.setTitle("SimCity201");
        gui.setVisible(true);
        gui.setResizable(true);
        gui.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

        public void stateChanged(ChangeEvent e) {
                //if(e.getSource() ==
                //(slider)
        }
        
        public void changeView(String building){
                if(building.equals("Restaurant1")){
                        animationPanel.setVisible(false);
                add(restaurant2, BorderLayout.EAST);
                        restaurant2.setVisible(true);
                }
                if(building.equals("City")){
                        restaurant2.setVisible(false);
                        animationPanel.setVisible(true);
                }       
        }
        
        public void addPerson(String name, AStarTraversal aStarTraversal, Role job){
                PersonAgent newPerson = new PersonAgent(name, aStarTraversal);
                if(job != null){
                	newPerson.addFirstJob(job);
                	//Add location to this
                }
                people.add(newPerson);
                PersonGui g = new PersonGui(newPerson);
                newPerson.setGui(g);
                guis.add(g);
                animationPanel.addGui(g);
                g.addAnimationPanel(restaurant2);
                
                newPerson.startThread();
        }

        public void enableComeBack(Restaurant2Waiter agent) {
                // TODO Auto-generated method stub
                
        }

        public void setEnabled(Restaurant2Waiter agent) {
                // TODO Auto-generated method stub
                
        }        
}