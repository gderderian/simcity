package city.gui.House;

import java.awt.Color;
import java.awt.Graphics2D;

import javax.swing.ImageIcon;

import city.PersonAgent;
import city.gui.AnimationPanel;
import city.gui.Gui;

public class HomeOwnerGui implements Gui {
        PersonAgent person;
        
        private int xPos = 50, yPos = 50;//default position
    private int xDestination = 50, yDestination = 50;//default position
    private int dimensions= 20;
    private int movement= 20;
    
    private static final int xTable = 500;
    private static final int yTable = 200;
    private static final int xFridge = 500;
    private static final int xStove = 525;
    private static final int xOven = 580;
    private static final int xMicrowave = 635;
    private static final int xBed = 150;
    private static final int yBed = 550;
    private static final int yAppliance = 50;
    
    
        private boolean goingToBed= false;
        private boolean goingToFridge= false;
        private boolean goingToStove= false;
        private boolean goingToOven= false;
        private boolean goingToMicrowave= false;
        private boolean goingToTable= false;
        
    	ImageIcon flat1 = new ImageIcon("images/person_flat1.png");
    	ImageIcon flat2 = new ImageIcon("images/person_flat2.png");
    	ImageIcon flat3 = new ImageIcon("images/person_flat3.png");
    	
    	private int movementCounter = 0;
    	private final int iconSwitch = 10; //Rate at which icons switch during movement
    	
    	ImageIcon icon = flat1;
    
    HouseAnimationPanel animPanel;
    
    public HomeOwnerGui(PersonAgent p){
            person= p;
    }
    
    
    public void updatePosition() {
    	//Code for switching pictures to create animation
    	movementCounter = (movementCounter + 1) % (4 * iconSwitch);

    	if(xPos != xDestination || yPos != yDestination) {
    		if(movementCounter < iconSwitch)
    	        icon = flat1;
    	    else if(movementCounter < iconSwitch * 2)
    	        icon = flat2;
    	    else if(movementCounter < iconSwitch * 3)
    	        icon = flat3;
    	    else
    	        icon = flat2;
    	   } else icon = flat2;
    	
    	if (xPos < xDestination)
            xPos++;
        else if (xPos > xDestination)
            xPos--;

        if (yPos < yDestination)
            yPos++;
        else if (yPos > yDestination)
            yPos--;

        //Check if reached any destination yet
        if (xPos >= xTable && yPos >= yTable && goingToTable){
           person.msgAtDestination();
           person.msgAnimationAtTable(); 
           goingToTable= false;
        } else if (xPos >= xFridge && yPos <= yAppliance && goingToFridge){
        	person.msgAnimationAtFridge();
            person.msgAtDestination();
            goingToFridge= false;
        } else if (xPos >= xStove && yPos <= yAppliance && goingToStove){
            person.msgAnimationAtStove();
            person.msgAtDestination();
            goingToStove= false;
        } else if (xPos >= xOven && yPos <= yAppliance && goingToOven){
            person.msgAnimationAtOven();
            person.msgAtDestination();
            goingToOven= false;
        } else if (xPos >= xMicrowave && yPos <= yAppliance && goingToMicrowave){
            person.msgAnimationAtMicrowave();
            person.msgAtDestination();
            goingToMicrowave= false;
        } else if (xPos >= xBed && yPos >= yBed && goingToBed){
                person.msgAnimationAtBed();
                goingToBed= false;
        } else if (xPos <= 0 && yPos <= 0 ){
        	person.msgAtDestination();
        }
        
   }

    public void setMainAnimationPanel(HouseAnimationPanel p) {
                animPanel = p;
        }
    
    public void draw(Graphics2D g) {
            g.drawImage(icon.getImage(), xPos, yPos, 30, 44, animPanel);
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

    public void goToBed(){
            if(!goingToBed){
                    xDestination= xBed;
                    yDestination= yBed;
                    goingToBed= true;
            }
    }
    
    public void goToFridge(){
            if(!goingToFridge){
                    xDestination= xFridge;
                    yDestination= yAppliance;
                    goingToFridge= true;
            }
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
    
    public void goToTable(){
            if(!goingToTable){
                    xDestination= xTable;
                    yDestination= yTable;
                    goingToTable= true;
            }
    }
    
    public void goToExit(){
    	xDestination= 0;
    	yDestination= 0;
    }
    
        @Override
        public void setPresent(boolean t) {
                // TODO Auto-generated method stub
                
        }
}
