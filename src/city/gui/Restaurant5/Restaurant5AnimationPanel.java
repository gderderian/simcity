package city.gui.Restaurant5;

import city.gui.Gui;
import javax.swing.*;

import city.PersonAgent;
import city.Restaurant2.Restaurant2CashierRole;
import city.Restaurant2.Restaurant2CookRole;
import city.Restaurant2.Restaurant2HostRole;
import city.Restaurant2.Restaurant2WaiterRole;
import city.Restaurant5.Restaurant5;
import city.Restaurant5.Restaurant5CashierRole;
import city.Restaurant5.Restaurant5CookRole;
import city.Restaurant5.Restaurant5HostRole;
import city.Restaurant5.Restaurant5WaiterRole;
import city.gui.BuildingPanel;
import city.gui.restaurant2.Restaurant2CookGui;
import city.gui.restaurant2.Restaurant2CustomerGui;
import city.gui.restaurant2.Restaurant2WaiterGui;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.List;
import java.util.ArrayList;

public class Restaurant5AnimationPanel extends BuildingPanel implements ActionListener, MouseListener {

    private final int WINDOWX = 700;//450
    private final int WINDOWY = 350;
    //getting rid of magic numbers
    private static int tablewidth = 200;
    private static int tablelength = 250;
    private static int tablespacing = 50;
    
    Restaurant5 restaurant;
    
    PersonAgent personCook = new PersonAgent("Cook");
    PersonAgent personHost = new PersonAgent("Host");
    PersonAgent personCashier = new PersonAgent("Cashier");
    PersonAgent personWaiter = new PersonAgent("Waiter");
    Restaurant5CookRole Cook;
    Restaurant5CashierRole Cashier;
    Restaurant5WaiterRole Waiter;
    Restaurant5HostRole Host;
    
    Restaurant5CookGui cookGui;
    Restaurant5CustomerGui customerGui;
    Restaurant5WaiterGui waiterGui;
    
   //Restaurant5Gui restaurant5gui = new Restaurant5Gui();
    
    private Image bufferImage;
    private Dimension bufferSize;

    private List<Gui> guis = new ArrayList<Gui>();

    public Restaurant5AnimationPanel(Restaurant5 restaurant) {
    	setSize(WINDOWX, WINDOWY);
        setVisible(true);
        bufferSize = this.getSize();
    	
    	addMouseListener(this);
    	
    	this.restaurant = restaurant;
   
    	Timer timer = new Timer(13, this );
    	timer.start();
    	
    	
    }

	public void actionPerformed(ActionEvent e) {
		repaint();  //Will have paintComponent called
	}

    public void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D)g;
        Graphics2D g3 = (Graphics2D)g;
        Graphics2D g4 = (Graphics2D)g;
        Graphics2D g5 = (Graphics2D)g;
        Graphics2D kitchentable = (Graphics2D)g;
        Graphics2D cookingarea = (Graphics2D)g;
        Graphics2D stove = (Graphics2D)g;
        Graphics2D waitingarea = (Graphics2D)g;
        
        //Clear the screen by painting a rectangle the size of the frame
        g2.setColor(getBackground());
        g2.fillRect(0, 0, this.getWidth(), this.getHeight());
        
        g3.setColor(Color.orange);
        g3.fillRect(260, 250, 50, 50);
       
        g4.setColor(Color.orange);
        g4.fillRect(320, 250, 50, 50);
       
        g5.setColor(Color.orange);
        g5.fillRect(380, 250, 50, 50);
        
        kitchentable.setColor(Color.yellow);
        kitchentable.fillRect(340, 70, 250, 40);
        
        cookingarea.setColor(Color.gray);
        cookingarea.fillRect(340, 7, 250, 20);
        
        waitingarea.setColor(Color.gray);
        waitingarea.fillRect(5, 40, 20, 270);
     
        
        //Here is the table
        //got rid of the magic numbers
        g2.setColor(Color.ORANGE);
        g2.fillRect(tablewidth, tablelength, tablespacing, tablespacing);//200 and 250 need to be table params

/*
 * This gets moved to updatePos below so it will be called even when the panel is not visible
        for(Gui gui : guis) {
            if (gui.isPresent()) {
                gui.updatePosition();
            }
        }
*/
        for(Gui gui : guis) {
            if (gui.isPresent()) {
                gui.draw(g2);
                gui.draw(g3); 
            }
        }
    }

    public void addGui(Gui gui) {
        guis.add(gui);
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
	public void mouseReleased(MouseEvent e) {
		int x = e.getX();
		int y = e.getY();
		if((x >= 30) && (x <= 90) && (y >= 30) && (y <= 50)){
			changeBackToCity();
		}
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void updatePos() {
        for(Gui gui : guis) {
            if (gui.isPresent()) {
                gui.updatePosition();
            }
        }
	}
}
