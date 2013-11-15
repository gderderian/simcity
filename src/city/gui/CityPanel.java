package city.gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Semaphore;

import javax.swing.ImageIcon;
import javax.swing.JPanel;
import javax.swing.Timer;

import city.PersonAgent;
import city.gui.restaurant2.Restaurant2AnimationPanel;

public class CityPanel extends JPanel implements MouseListener, ActionListener {

    private static final int WINDOWX = 1300;
    private static final int WINDOWY = 700;
    private static final int TIMER_INTERVAL = 15;

    static int gridX = 45; //# of x-axis tiles
    static int gridY = 35; //# of y-axis tiles

    //Semaphore grid for astar animation
    Semaphore[][] streetGrid = new Semaphore[gridX+1][gridY+1];
    Semaphore[][] sidewalkGrid = new Semaphore[gridX+1][gridY+1];
    
    public CityPanel(CityGui gui) {
    	cityGui = gui;
    	
    	setSize(WINDOWX, WINDOWY);
    	setPreferredSize(new Dimension(WINDOWX, WINDOWY));
    	setMaximumSize(new Dimension(WINDOWX, WINDOWY));
    	setMinimumSize(new Dimension(WINDOWX, WINDOWY));
        setVisible(true);
        
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
      	
      	for(int i = 6; i < 8; i++) { //This loop covers the leftmost side of sidewalk, sans bus stop area.
      		for(int j = 6; j < 16; j++)
      			sidewalkGrid[i][j].release();
      		for(int k = 21; k < 31; k++)
      			sidewalkGrid[i][k].release();
      	}
      	for(int i = 39; i < 41; i++) { //This loop covers the rightmost side of sidewalk, sans bus stop area.
      		for(int j = 6; j < 16; j++)
      			sidewalkGrid[i][j].release();
      		for(int k = 21; k < 36; k++)
      			sidewalkGrid[i][k].release();
      	}
      	for(int i = 6; i < 8; i++) { //This loop covers the top side of sidewalk, sans bus stop area.
      		for(int j = 8; j < 21; j++)
      			sidewalkGrid[j][i].release();
      		for(int k = 26; k < 39; k++)
      			sidewalkGrid[k][i].release();
      	}
      	for(int i = 29; i < 31; i++) { //This loop covers the bottom side of sidewalk, sans bus stop area.
      		for(int j = 8; j < 21; j++)
      			sidewalkGrid[j][i].release();
      		for(int k = 26; k < 34; k++)
      			sidewalkGrid[k][i].release();
      	}     
      	
      	for(int i = 32; i < 34; i++) //This loop covers the extra portion on entrance street sidewalk
      		for(int j = 31; j < 36; j++)
      			sidewalkGrid[i][j].release();
      	
      	for(int i = 21; i < 26; i++) { //Top/bottom bus stop sidewalks.
      		for(int j = 5; j < 7; j++)
      			sidewalkGrid[i][j].release();
      		for(int k = 30; k < 32; k++)
      			sidewalkGrid[i][k].release();
      	}
      	for(int i = 16; i < 21; i++) { //Left/right bus stop sidewalks.
      		for(int j = 5; j < 7; j++)
      			sidewalkGrid[j][i].release();
      		for(int k = 40; k < 42; k++)
      			sidewalkGrid[k][i].release();
      	}      	
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
      	for(int i = 13; i < 15; i++) //Top left crosswalk
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
      			sidewalkGrid[i][j] = streetGrid[i][j];      	
      	
      	/********Finished setting up semaphore grid***********/
        
        addMouseListener(this);
        
 //       testPerson.startThread();
 //       testPerson.setGui(testPersonGui);
 //       testPersonGui.addAnimationPanel(testRest2AnimPanel);
  //      guis.add(testPersonGui);
        
    }
    
    public void setCityGui(CityGui c){
    	cityGui = c;
    }

	public void actionPerformed(ActionEvent e) {
		//Possible actions?
	}

	public void mouseClicked(MouseEvent e) {
		//do nothing
	}

	public void mouseEntered(MouseEvent e) {
		//do nothing
	}

	public void mouseExited(MouseEvent e) {
		//do nothing
	}

	public void mousePressed(MouseEvent e) {
		//System.out.println("Mouse pressed in animation panel");
	}

	public void mouseReleased(MouseEvent e) {
		int x = e.getX();
		int y = e.getY();
		if((x >= 200) && (x <= 230) && (y >= 200) && (y <= 230)){
			System.out.println("YAY YOU CLICKED THE SQUARE");
			cityGui.changeView("Restaurant1");
		}
	}
}
