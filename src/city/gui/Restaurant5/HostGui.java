package city.gui.Restaurant5;

import city.gui.Gui;
import tomtesting.interfaces.*;

import java.awt.*;

public class HostGui implements Gui {

    private Restaurant5Host agent = null;
    private boolean returningtolobby = false;

    private int xPos = -20, yPos = -20;//default waiter position
    private int xDestination = -20, yDestination = -20;//default start position

    public static int xTable = 200;
    public static int yTable = 250;

    public HostGui(Restaurant5Host agent) {
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
        g.setColor(Color.MAGENTA);
        g.fillRect(xPos, yPos, 20, 20);
    }

    public boolean isPresent() {
        return true;
    }

    public void DoBringToTable(Restaurant5Customer customer, int tablexcoordinate, int tableycoordinate) {
      
        xDestination = tablexcoordinate + 20;
        yDestination = tableycoordinate - 20;
        xTable = tablexcoordinate;
        yTable = tableycoordinate;
        
    }

    public void DoLeaveCustomer() {
        xDestination = -20;
        yDestination = -20;
        returningtolobby = true;
    }

    public int getXPos() {
        return xPos;
    }

    public int getYPos() {
        return yPos;
    }
}
