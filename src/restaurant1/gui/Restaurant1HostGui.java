package restaurant1.gui;


import restaurant1.Restaurant1CustomerRole;
import restaurant1.Restaurant1HostRole;

import java.awt.*;
import java.util.HashMap;
import java.util.Map;

/* NOTE: This code is not used in Restaurant V2 */

public class Restaurant1HostGui implements Restaurant1Gui {
	
	private static final int XPOS = -20, YPOS = -20;
	private static final int XTABLE = 200, YTABLE = 250;

    private Restaurant1HostRole agent = null;

    private int xPos = XPOS, yPos = YPOS;//default waiter position
    private int xDestination = XPOS, yDestination = YPOS;//default start position

    public static final int xTable = XTABLE;
    public static final int yTable = YTABLE;

    public Restaurant1HostGui(Restaurant1HostRole agent) {
        this.agent = agent;

        // Initial mapping of table locations!
        tableLocations.put(new Integer(1), new Dimension(200, 200));
        tableLocations.put(new Integer(2), new Dimension(450, 200));
        tableLocations.put(new Integer(3), new Dimension(200, 400));
        tableLocations.put(new Integer(4), new Dimension(450, 400));
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
        g.setColor(Color.MAGENTA);
        g.fillRect(xPos, yPos, 20, 20);
    }

    public boolean isPresent() {
        return true;
    }

    public void DoLeaveCustomer() {
    	// Initial coordinates (off-screen)
        xDestination = -30;
        yDestination = -30;
    }
}
