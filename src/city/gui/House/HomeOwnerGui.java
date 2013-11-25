package city.gui.House;

import java.awt.Color;
import java.awt.Graphics2D;

import city.PersonAgent;

public class HomeOwnerGui {
	PersonAgent person;
	
	private int xPos = -20, yPos = -20;//default waiter position
    private int xDestination = -20, yDestination = -20;//default start position
    private int dimensions= 20;
    private int movement= 20;
    
    private static final int xTable = 500;
    private static final int yTable = 200;
    private static final int xFridge = 400;
    private static final int xStove = 525;
    private static final int xOven = 580;
    private static final int xMicrowave = 635;
    private static final int xBed = 100;
    private static final int yBed = 500;
    private static final int yAppliance = 30;
    
    
    public void updatePosition() {
        if (xPos < xDestination)
            xPos++;
        else if (xPos > xDestination)
            xPos--;

        if (yPos < yDestination)
            yPos++;
        else if (yPos > yDestination)
            yPos--;

        //Check if reached any destination yet
        if (xPos == xDestination && yPos == yDestination
        		& (xDestination == xTable + movement) & (yDestination == yTable - movement)) {
           person.msgAnimationAtTable(); 
        } else if (xPos == xDestination && yPos == yDestination
        		& (xDestination == xFridge + movement) & (yDestination == yAppliance - movement)) {
            person.msgAnimationAtFridge();
        } else if (xPos == xDestination && yPos == yDestination
        		& (xDestination == xStove + movement) & (yDestination == yAppliance - movement)) {
            person.msgAnimationAtStove();
        } else if (xPos == xDestination && yPos == yDestination
        		& (xDestination == xOven + movement) & (yDestination == yAppliance - movement)) {
            person.msgAnimationAtOven();
        } else if (xPos == xDestination && yPos == yDestination
        		& (xDestination == xMicrowave + movement) & (yDestination == yAppliance - movement)) {
            person.msgAnimationAtMicrowave();
        } else if (xPos == xDestination && yPos == yDestination
        		& (xDestination == xBed + movement) & (yDestination == yBed - movement)) {
            person.msgAnimationAtBed();
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
