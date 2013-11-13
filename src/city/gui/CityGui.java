package city.gui;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import java.awt.*;
import java.awt.event.*;
/**
 * Main GUI class.
 * Contains the main frame and subsequent panels
 */
public class CityGui extends JFrame implements ActionListener, ChangeListener {
    /* The GUI has two frames, the control frame (in variable gui) 
     * and the animation frame, (in variable animationFrame within gui)
     */
	//JFrame animationFrame = new JFrame("Restaurant Animation");
	//AnimationPanel animationPanel = new AnimationPanel();
	
	
	
    /* restPanel holds 3 panels
     * 1) the staff listing, menu, and lists of current customers all constructed
     *    in RestaurantPanel()
     * 2) the Animation Panel
     * 3) the infoPanel about the clicked Customer (created just below)
     */    
    AnimationPanel animationPanel = new AnimationPanel();
    private CityPanel cityPanel = new CityPanel(this);
    
    private JPanel infoPanel;
    
    private final int WINDOWX = 1300;
    private final int WINDOWY = 700;
    private final int WINDOW_X_COORD = 50;
    private final int WINDOW_Y_COORD = 50;
    /**
     * Constructor for RestaurantGui class.
     * Sets up all the gui components.
     */
    public CityGui() {
    	
    	setBounds(WINDOW_X_COORD, WINDOW_Y_COORD, WINDOWX, WINDOWY);

    	setLayout(new BorderLayout());
        
        Dimension animationDim = new Dimension(800, 800);
        animationPanel.setPreferredSize(animationDim);
        add(animationPanel, BorderLayout.EAST);

        Dimension panelDim = new Dimension(600, 840);
        infoPanel = new JPanel();
        infoPanel.setPreferredSize(panelDim);
        infoPanel.setMinimumSize(panelDim);
        infoPanel.setMaximumSize(panelDim);
        infoPanel.setBorder(BorderFactory.createTitledBorder("Information"));
        
        infoPanel.setLayout(new FlowLayout());
        
        
        add(infoPanel, BorderLayout.WEST);
        
        // Now, setup the info panel
        /*Dimension infoDim = new Dimension(WINDOWX/3, (int) (WINDOWY * .15));
        infoPanel = new JPanel();
        infoPanel.setPreferredSize(infoDim);
        infoPanel.setMinimumSize(infoDim);
        infoPanel.setMaximumSize(infoDim);
        infoPanel.setBorder(BorderFactory.createTitledBorder("Information"));
        
        infoPanel.setLayout(new FlowLayout());*/
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
        gui.setResizable(false);
        gui.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

	public void stateChanged(ChangeEvent e) {
		//if(e.getSource() ==
		//(slider)
	}
}
