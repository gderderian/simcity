package city.gui.House;

import java.awt.Color;
import java.awt.Graphics2D;

import Role.LandlordRole;

public class LandlordGui {
        LandlordRole landlord;
        
        private int xPos = 100, yPos = 100;//default waiter position
    private int xDestination = 100, yDestination = 100;//default start position
    private int dimensions= 20;

        private boolean goingToStove= false;
        private boolean goingToOven= false;
        private boolean goingToMicrowave= false;
    
    private static final int xStove = 525;
    private static final int xOven = 580;
    private static final int xMicrowave = 635;
    private static final int yAppliance = 30;

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

        //Check if reached any destination yet
        if (xPos >= xStove && yPos <= yAppliance && goingToStove){
            landlord.msgAnimationAtStove();
            goingToStove= false;
        } else if (xPos >= xOven && yPos <= yAppliance && goingToOven){
            landlord.msgAnimationAtOven();
            goingToOven= false;
        } else if (xPos >= xMicrowave && yPos <= yAppliance && goingToMicrowave){
            landlord.msgAnimationAtMicrowave();
            goingToMicrowave= false;
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

    public void goToOven(){
            if(!goingToOven){
                    xDestination= xOven;
                    yDestination= yAppliance;
                    goingToOven= true;
            }
    }

    public void goToMicrowave(){
            if(!goingToMicrowave){
                    xDestination= xMicrowave;
                    yDestination= yAppliance;
                    goingToMicrowave= true;
            }
    }

    public void goToStove(){
            if(!goingToStove){
                    xDestination= xStove;
                    yDestination= yAppliance;
                    goingToStove= true;
            }
    }
}
