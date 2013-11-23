package restaurant1.gui;


import restaurant1.Restaurant1CashierRole;
import restaurant1.Restaurant1CookRole;
import restaurant1.Restaurant1CustomerRole;
import restaurant1.Restaurant1HostRole;

import java.awt.*;

/* NOTE: This code is not used in Restaurant V2 */

public class Restaurant1CashierGui implements Restaurant1Gui {
	
	private static final int XPOS = 50, YPOS = 300;

    private Restaurant1CashierRole agent = null;

    private int xPos = XPOS, yPos = YPOS;//default waiter position
    private int xDestination = XPOS, yDestination = YPOS;//default start position

    public Restaurant1CashierGui(Restaurant1CashierRole agent) {
        this.agent = agent;
    }

    public void updatePosition() {
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
        g.setColor(Color.YELLOW);
        g.fillRect(xPos, yPos, 30, 30); // Size/position of gui
        
        // This draws "$$$" on the customer gui
        Font font = new Font("Arial", Font.BOLD, 20);
        g.setFont(font);
        g.setColor(Color.BLACK);
        g.drawString("$", xPos + 8, yPos + 22);
    }

    public boolean isPresent() {
        return true;
    }
}
