package city.gui.Market;

import javax.swing.*;

import city.gui.CityGui;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.List;
import java.util.ArrayList;

public class MarketAnimationPanel extends JPanel implements ActionListener, MouseListener {

    private final int WINDOWX = 900;
    private final int WINDOWY = 750;
    
    private final int MAIN_TIMER = 15;
    private final int TABLE_X = 50;
    private final int TABLE_Y = 50;
    private final int SCREEN_RECT_X_COORD = 0;
    private final int SCREEN_RECT_Y_COORD = 0;

    private List<Gui> guis = new ArrayList<Gui>();
    
	Timer timer = new Timer(MAIN_TIMER, this);
	private boolean timerIsRunning = false;
    
	CityGui cg;
	
    public MarketAnimationPanel(CityGui newCityGui) {
    	setSize(WINDOWX, WINDOWY);
        setVisible(true);
    	timer.start();
    	timerIsRunning = true;
    	cg = newCityGui;
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
        
        // Customer Waiting Area
        g2.setColor(Color.WHITE);
        g2.fillRect(5, 15, 105, 100);
        g2.setColor(Color.BLUE);
        g2.drawString("Customer Area", 5, 127);
        
        // Waiter Waiting Area
        g2.setColor(Color.WHITE);
        g2.fillRect(245, 15, 105, 105);
        g2.setColor(Color.BLUE);
        g2.drawString("Waiter Area", 245, 132);
        
        // Kitchen Area
        g2.setColor(Color.WHITE);
        g2.fillRect(5, 390, 490, 135);
        g2.setColor(Color.BLUE);
        g2.drawString("Kitchen Area", 5, 385);
        
        // Finished Food Plating Area
        g2.setColor(Color.BLACK);
        g2.fillRect(200, 390, 75, 30);
        
        // Stove Area
        g2.setColor(Color.BLACK);
        g2.fillRect(200, 495, 75, 30);
        
        // Refrigerator
        g2.setColor(Color.ORANGE);
        g2.fillRect(350, 435, 45, 45);

        // Table 1
        g2.setColor(Color.ORANGE);
        g2.fillRect(150, 150, TABLE_X, TABLE_Y); // Table location set by host

        // Table 2
        g2.setColor(Color.ORANGE);
        g2.fillRect(150, 275, TABLE_X, TABLE_Y); // Table location set by host
        
        // Table 3
        g2.setColor(Color.ORANGE);
        g2.fillRect(275, 150, TABLE_X, TABLE_Y); // Table location set by host
        
        // Table 4
        g2.setColor(Color.ORANGE);
        g2.fillRect(275, 275, TABLE_X, TABLE_Y); // Table location set by host
        
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
		if((x >= 20) && (x <= 64) && (y >= 20) && (y <= 44)) {
			cg.changeView("City");
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
