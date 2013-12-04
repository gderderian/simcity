package city.gui.restaurant2;

import java.awt.Color;
import java.awt.Graphics2D;

import city.Restaurant2.Restaurant2CookRole;
import city.gui.Gui;

public class Restaurant2CookGui implements Gui{
	
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
        g.drawString("CHEF", xPos - 5, yPos - 5);
        g.setColor(Color.ORANGE);
        g.fillRect(xPos, yPos, 25, 25);
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
