package city.gui.House;

import java.awt.Color;
import java.awt.Graphics2D;

import javax.swing.ImageIcon;

import city.gui.Gui;
import Role.LandlordRole;

public class LandlordGui implements Gui{
        LandlordRole landlord;
        
        private int xPos = 100, yPos = 100;//default waiter position
    private int xDestination = 100, yDestination = 100;//default start position
    private int dimensions= 20;

        private boolean goingToStove= false;
        private boolean goingToOven= false;
        private boolean goingToMicrowave= false;
        private boolean exitOnce= false;
    
    private static final int xStove = 525;
    private static final int xOven = 580;
    private static final int xMicrowave = 635;
    private static final int yAppliance = 60;
    
    ImageIcon icon1 = new ImageIcon("images/waiter1.png");
	ImageIcon icon2 = new ImageIcon("images/waiter2.png");
	ImageIcon icon3 = new ImageIcon("images/waiter3.png");
	
	private int movementCounter = 0;
	private final int iconSwitch = 10; //Rate at which icons switch during movement
	
	ImageIcon icon = icon1;

    public LandlordGui(LandlordRole landlord){
        this.landlord = landlord;
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
        } else if (xPos <= -30 && yPos <= 30 && exitOnce){
        	landlord.msgAnimationExited(); 
        	exitOnce= false;
        }
    } 

    public void draw(Graphics2D g) {
    	g.drawImage(icon.getImage(), xPos, yPos, 30, 30, null);
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
    
    public void goToExit(){
    	if(!exitOnce){
    		xDestination= -30;
    		yDestination= 30;
    		exitOnce= true;
    	}
    }

	@Override
	public void setPresent(boolean t) {
		// TODO Auto-generated method stub
		
	}
}
