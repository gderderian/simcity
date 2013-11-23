package city.gui.House;

import java.awt.Color;
import java.awt.Graphics2D;

import Role.LandlordRole;

public class LandlordGui {
	LandlordRole landlord;
	
	private int xPos = -20, yPos = -20;//default waiter position
    private int xDestination = -20, yDestination = -20;//default start position
    private int dimensions= 20;
    private int movement= 20;

    public static int xTable = 100;
    public static int xTableNew;
    public static final int yTable = 250;

    public LandlordGui(LandlordRole landlord){
        this.landlord = landlord;
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
        g.fillRect(xPos, yPos, dimensions, dimensions);
    }

    public boolean isPresent() {
        return true;
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
