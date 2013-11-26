package city.gui.House;

import java.awt.Color;
import java.awt.Graphics2D;

import javax.swing.ImageIcon;

import city.PersonAgent;
import city.gui.AnimationPanel;
import city.gui.Gui;

public class HomeOwnerGui implements Gui {
	PersonAgent person;
	
	private int xPos = 100, yPos = 100;//default waiter position
    private int xDestination = 100, yDestination = 100;//default start position
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
    
    private int movementCounter = 0;
	private final int iconSwitch = 10; //Rate at which icons switch during movement
    
    ImageIcon icon = new ImageIcon("images/person_up1.png");
    
    ImageIcon up1 = new ImageIcon("images/person_up1.png");
	ImageIcon up2 = new ImageIcon("images/person_up2.png");
	ImageIcon up3 = new ImageIcon("images/person_up3.png");
	ImageIcon down1 = new ImageIcon("images/person_down1.png");
	ImageIcon down2 = new ImageIcon("images/person_down2.png");
	ImageIcon down3 = new ImageIcon("images/person_down3.png");
	ImageIcon flat1 = new ImageIcon("images/person_flat1.png");
	ImageIcon flat2 = new ImageIcon("images/person_flat2.png");
	ImageIcon flat3 = new ImageIcon("images/person_flat3.png");
    
    HouseAnimationPanel animPanel;
    
    public HomeOwnerGui(PersonAgent p){
    	person= p;
    }
    
    
    public void updatePosition() {
    	movementCounter = (movementCounter + 1) % (4 * iconSwitch);
        if (xPos < xDestination) {
            xPos++;
            if(movementCounter < iconSwitch)
        		icon = flat1;
        	else if(movementCounter < iconSwitch * 2)
        		icon = flat2;
        	else if(movementCounter < iconSwitch * 3)
        		icon = flat3;
        	else
        		icon = flat2;
        }
        
        else if (xPos > xDestination) {
            xPos--;
        	if(movementCounter < iconSwitch && icon != flat1)
        		icon = flat1;
        	else if(movementCounter < iconSwitch * 2 && icon != flat2)
        		icon = flat2;
        	else if(movementCounter < iconSwitch * 3 && icon != flat3)
        		icon = flat3;
        	else if(icon != flat2)
        		icon = flat2;
        }
        
        if (yPos < yDestination) {
            yPos++;
        	if(movementCounter < iconSwitch && icon != down1)
        		icon = down1;
        	else if(movementCounter < iconSwitch * 2 && icon != down2)
        		icon = down2;
        	else if(movementCounter < iconSwitch * 3 && icon != down3)
        		icon = down3;
        	else if(icon != down2)
        		icon = down2;
        }
        else if (yPos > yDestination) {
            yPos--;
        	if(movementCounter < iconSwitch && icon != up1)
        		icon = up1;
        	else if(movementCounter < iconSwitch * 2 && icon != up2)
        		icon = up2;
        	else if(movementCounter < iconSwitch * 3 && icon != up3)
        		icon = up3;
        	else if(icon != up2)
        		icon = up2;
        }

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

    public void setMainAnimationPanel(HouseAnimationPanel p) {
		animPanel = p;
	}
    
    public void draw(Graphics2D g) {
    	g.drawImage(up1.getImage(), xPos, yPos, 30, 44, animPanel);
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


	@Override
	public void setPresent(boolean t) {
		// TODO Auto-generated method stub
		
	}
}
