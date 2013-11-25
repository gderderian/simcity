package city.gui.restaurant4;

import city.gui.Gui;
import city.Restaurant4.CustomerRole4;
import city.Restaurant4.HostRole4;

import java.awt.*;

public class HostGui4 implements Gui {

    //private HostAgent agent = null;

    private int xPos = -20, yPos = -20;//default waiter position
    private int xDestination = -20, yDestination = -20;//default start position
    private int hostDimensions= 20;
    private int movement= 20;
    private int exitDest= -20;

    public static int xTable = 100;
    public static int xTableNew;
    public static final int yTable = 250;
    
    boolean isPresent = true;

    public HostGui4(HostRole4 agent) {
        //this.agent = agent;
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

        if (xPos == xDestination && yPos == yDestination
        		& (xDestination == xTableNew + movement) & (yDestination == yTable - movement)) {
           System.out.println("xTable: " + xTableNew);
        }
    }

    public void draw(Graphics2D g) {
        g.setColor(Color.MAGENTA);
        g.fillRect(xPos, yPos, hostDimensions, hostDimensions);
    }

    public boolean isPresent() {
        return isPresent;
    }
    
	public void setPresent(boolean t) {
		if(t)
			isPresent = true;
		else
			isPresent = false;
	}

    public void DoBringToTable(CustomerRole4 customer, int tableNumber) {
        xTableNew= xTable * tableNumber;
        System.out.println("Table Number DoBringToTable: " + tableNumber);
    	xDestination = xTableNew + movement;
        yDestination = yTable - movement;
    }

    public void DoLeaveCustomer() {
        xDestination = exitDest;
        yDestination = exitDest;
    }

    public int getXPos() {
        return xPos;
    }

    public int getYPos() {
        return yPos;
    }
    
    public void setXPos(int x){
    	xPos= x;
    }
    
    public void setYPos(int y){
    	yPos= y;
    }
}
