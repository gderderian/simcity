package city.gui.restaurant2;

import java.awt.Color;
import java.awt.Graphics2D;

import javax.swing.ImageIcon;

import city.Restaurant2.Restaurant2CookRole;
import city.gui.Gui;

public class Restaurant2CookGui implements Gui{
	
	ImageIcon icon1 = new ImageIcon("images/cook1.png");
	ImageIcon icon2 = new ImageIcon("images/cook2.png");
	ImageIcon icon3 = new ImageIcon("images/cook3.png");
	
	private int movementCounter = 0;
	private final int iconSwitch = 10;
	
	ImageIcon icon = icon1;
	
	private int xDest;
	private int yDest;
	private int yPos;
	private int xPos;
    private final int REFRIGERATORX = 800;
    private final int REFRIGERATORY = 350;
    private final int STOVEX = 690;	//the farthest end of the stove
    private final int STOVEY = 430;
    private final int HOMEX = 770;
    private final int HOMEY = 350;
    private final int COUNTERX = 760;
    private final int COUNTERY = 360;
    
    //ImageIcon icon = new ImageIcon("images/chef.png");	//Need to get this icon to work
	Restaurant2AnimationPanel restaurant2panel;
	
	boolean foodCooking;
	boolean foodDone;
	Restaurant2CookRole cook;
	
	boolean isPresent = true;
	
	public Restaurant2CookGui(Restaurant2CookRole c){
		cook = c;
		xPos = HOMEX;
		yPos = HOMEY;
		xDest = HOMEX;
		yDest = HOMEY;
	}

	public void updatePosition() {
		
		movementCounter = (movementCounter + 1) % (4 * iconSwitch);
		
		if(xPos != xDest || yPos != yDest) {
            if(movementCounter < iconSwitch)
        		icon = icon1;
        	else if(movementCounter < iconSwitch * 2)
        		icon = icon2;
        	else if(movementCounter < iconSwitch * 3)
        		icon = icon3;
        	else
        		icon = icon2;
    	} else icon = icon2;
		
        if (xPos < xDest)
            xPos++;
        else if (xPos > xDest)
            xPos--;

        if (yPos < yDest)
            yPos++;
        else if (yPos > yDest)
            yPos--;
        
        if(xPos == xDest  && yPos == yDest && ((yDest == REFRIGERATORY && xDest == REFRIGERATORX) || (yDest == STOVEY && xDest == STOVEX) || (yDest == COUNTERY && xDest == COUNTERX))){
        	cook.atDest();
        }
	}

	public void draw(Graphics2D g) {
		//g.drawImage(icon.getImage(), xPos, yPos, 25, 30, restaurant2panel);
        g.drawString("CHEF", xPos - 5, yPos - 5);
        g.drawImage(icon.getImage(), xPos, yPos, 30, 30, null);
        
        if(foodCooking){
			g.setColor(Color.PINK);
			g.fillOval(STOVEX - 7, STOVEY + 7, 20, 20);
        }
		if(foodDone){
			g.setColor(Color.WHITE);
			g.fillOval(STOVEX - 10, REFRIGERATORY + 10, 20, 20);
			g.setColor(Color.BLACK);
			g.fillOval(STOVEX - 8, REFRIGERATORY + 12, 16, 16);
		}
	}

	public boolean isPresent() {
		return isPresent;
	}
	
	public void doStartCooking(){	//first move to the refrigerator
		xDest = REFRIGERATORX;
		yDest = REFRIGERATORY;
	}
	
	public void doCookFood(){
		xDest = STOVEX;
		yDest = STOVEY;
	}
	
	public void doPlateFood(){
		xDest = COUNTERX;
		yDest = COUNTERY;
	}
	
	public void setFoodCooking(boolean t){
		if(t){
			foodCooking = true;
		}
		else
			foodCooking = false;
	}
	
	public void setFoodDone(boolean t){
		if(t){
			foodDone = true;
			foodCooking = false;
		}
		else foodDone = false;

	}
	
	public void goHome(){
		xDest = HOMEX;
		yDest = HOMEY;
	}
	
	public void setPresent(boolean t) {
		isPresent = t;
	}


}
