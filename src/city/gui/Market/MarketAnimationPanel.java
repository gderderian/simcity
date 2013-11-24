package city.gui.Market;

import javax.swing.*;

import city.gui.BuildingPanel;
import city.gui.CityGui;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.List;
import java.util.ArrayList;

public class MarketAnimationPanel extends BuildingPanel implements ActionListener, MouseListener {

    private final int WINDOWX = 900;
    private final int WINDOWY = 750;
    
    private final int MAIN_TIMER = 15;
    private final int SCREEN_RECT_X_COORD = 0;
    private final int SCREEN_RECT_Y_COORD = 0;

    private List<Gui> guis = new ArrayList<Gui>();
    
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
        
        // Return to city view button
        g2.setColor(Color.ORANGE);
        g2.fillRect(25, 30, 100, 25);
        g2.setColor(Color.RED);
        g2.drawString("Return to City", 30, 45);
        
        for(Gui gui : guis) {
            if (gui.isPresent()) {
                gui.updatePosition();
            }
        }

        for(Gui gui : guis) {
            if (gui.isPresent()) {
                gui.draw(g2);
            }
        }
        
    }

    	/*
    public void addGui(CustomerGui gui) {
        guis.add(gui);
    }

    public void addGui(WaiterGui gui) {
        guis.add(gui);
    }
    
    public void addGui(CookGui gui) {
        guis.add(gui);
    }
    */
    
	public void mouseReleased(MouseEvent e) {
		int x = e.getX();
		int y = e.getY();
		if((x >= 25) && (x <= 125) && (y >= 30) && (y <= 55)) {
			changeBackToCity();
		}
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}
    
}
