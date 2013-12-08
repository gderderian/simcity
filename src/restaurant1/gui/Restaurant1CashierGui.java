package restaurant1.gui;

import city.gui.AnimationPanel;
import city.gui.Gui;
import restaurant1.Restaurant1CashierRole;
import restaurant1.Restaurant1CookRole;
import restaurant1.Restaurant1CustomerRole;
import restaurant1.Restaurant1HostRole;

import java.awt.*;

import javax.swing.ImageIcon;

/* NOTE: This code is not used in Restaurant V2 */

public class Restaurant1CashierGui implements Gui {
	
	private static final int XPOS = 50, YPOS = 300;

    private Restaurant1CashierRole agent = null;
    
    boolean isPresent = true;

    private int xPos = XPOS, yPos = YPOS;//default waiter position
    private int xDestination = XPOS, yDestination = YPOS;//default start position
    
	ImageIcon icon1 = new ImageIcon("images/cashier1.png");
	ImageIcon icon2 = new ImageIcon("images/cashier2.png");
	ImageIcon icon3 = new ImageIcon("images/cashier3.png");
	
	private int movementCounter = 0;
	private final int iconSwitch = 10; //Rate at which icons switch during movement
	
	ImageIcon icon = icon1;

    public Restaurant1CashierGui(Restaurant1CashierRole agent) {
        this.agent = agent;
    }

    public void updatePosition() {
		//Code for switching pictures to create animation
		movementCounter = (movementCounter + 1) % (4 * iconSwitch);

		if(xPos != xDestination || yPos != yDestination) {
            if(movementCounter < iconSwitch)
        		icon = icon1;
        	else if(movementCounter < iconSwitch * 2)
        		icon = icon2;
        	else if(movementCounter < iconSwitch * 3)
        		icon = icon3;
        	else
        		icon = icon2;
    	} else icon = icon2;
		
        if (xPos < xDestination)
            xPos++;
        else if (xPos > xDestination)
            xPos--;

        if (yPos < yDestination)
            yPos++;
        else if (yPos > yDestination)
            yPos--;
    }

    public void draw(Graphics2D g) {
    	g.drawImage(icon.getImage(), xPos, yPos, 30, 30, null);
    }

    public boolean isPresent() {
        return true;
    }

	@Override
	public void setPresent(boolean t) {
		isPresent = t;
	}
}
