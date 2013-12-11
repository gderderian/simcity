package city.gui.Market;

import javax.swing.*;

import city.gui.BuildingPanel;
import city.gui.CityGui;
import city.gui.Gui;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Collections;
import java.util.List;
import java.util.ArrayList;

public class MarketAnimationPanel extends BuildingPanel implements ActionListener, MouseListener {

    private final int WINDOWX = 900;
    private final int WINDOWY = 750;
    
    private final int MAIN_TIMER = 10;
    private final int SCREEN_RECT_X_COORD = 0;
    private final int SCREEN_RECT_Y_COORD = 0;

    private List<Gui> guis = Collections.synchronizedList(new ArrayList<Gui>());
    
	Timer timer = new Timer(MAIN_TIMER, this);
	private boolean timerIsRunning = false;
	
    public MarketAnimationPanel(CityGui newCityGui) {
    	setSize(WINDOWX, WINDOWY);
        setVisible(true);
    	timer.start();
    	timerIsRunning = true;
    	addMouseListener(this);
    }

    public void toggleTimer() {
    	if (timerIsRunning == true){
    		timer.stop();
    		timerIsRunning = false;
    	} else {
        	timer.start();
        	timerIsRunning = true;
    	}	
    }
    
	public void actionPerformed(ActionEvent e) {
		repaint();
	}

    public void paintComponent(Graphics g) {
    	
        Graphics2D g2 = (Graphics2D)g;

        //Clear the screen by painting a rectangle the size of the frame
        g2.setColor(getBackground());
        g2.fillRect(SCREEN_RECT_X_COORD, SCREEN_RECT_Y_COORD, WINDOWX, WINDOWY);
        
        // Arriving Customer Waiting Area
        g2.setColor(Color.WHITE);
        g2.fillRect(5, 600, 200, 100);
        g2.setColor(Color.BLUE);
        g2.drawString("Customer Waiting Area", 5, 595);
        
        // Finished Food Plating Area
        g2.setColor(Color.RED);
        g2.fillRect(850, 0, 200, 900);
        
        // Stove Area
        g2.setColor(Color.RED);
        g2.fillRect(250, 675, 800, 200);

        // Counter
        g2.setColor(Color.ORANGE);
        g2.fillRect(200, 100, 55, 350); // Table location set by host
        
        
        /* This gets moved to the updatePos function below so its called even when the panel isnt visible
        for(Gui gui : guis) {
            if (gui.isPresent()) {
                gui.updatePosition();
            }
        }
*/
        synchronized(guis){
	        for(Gui gui : guis) {
	            if (gui.isPresent()) {
	                gui.draw(g2);
	            }
	        }
        }
        
    }

   
    public void addGui(Gui gui) {
    	synchronized(guis){
    		guis.add(gui);	
    	}
    }

	@Override
	public void mouseClicked(MouseEvent e) {
		// Nothing needed to do, required to implement to prevent Java errors
	}

	public void mousePressed(MouseEvent e) {
		// Nothing needed to do, required to implement to prevent Java errors
	}

	public void mouseEntered(MouseEvent e) {
		// Nothing needed to do, required to implement to prevent Java errors
	}

	public void mouseExited(MouseEvent e) {
		// Nothing needed to do, required to implement to prevent Java errors
	}

	public void mouseReleased(MouseEvent e) {
		// Nothing needed to do, required to implement to prevent Java errors
	}

	public void updatePos() {
		synchronized(guis){
	        for(Gui gui : guis) {
	            if (gui.isPresent()) {
	                gui.updatePosition();
	                gui.updatePosition();
	            }
	        }
		}
	}
    
}
