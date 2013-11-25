package city.gui.restaurant2;

import interfaces.Restaurant2Cook;

import java.awt.Color;
import java.awt.Graphics2D;

import city.Restaurant2.Restaurant2CookRole;
import city.gui.Gui;

public class Restaurant2CookGui implements Gui{
	
	private int xDest;
	private int yDest;
	private int yPos;
	private int xPos;
    private final int WINDOWX = 900;
    private final int WINDOWY = 750 - 20; //the - 20 to account for the border, etc.
    private final int KITCHENX = WINDOWX - 50;	//based off of the refrigerator
    private final int KITCHENY = WINDOWY/2 - 35;	//based off of the refrigerator
    private final int STOVEX = KITCHENX - 65;
    private final int STOVEY = KITCHENY + 50;
    private final int HOMEX = KITCHENX - 35;
    private final int HOMEY = KITCHENY + 18;
    private final int COUNTERX = WINDOWX - 115;
    private final int COUNTERY = WINDOWY/2 - 20;
	
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
        
        if(xPos == xDest  && yPos == yDest && ((yDest == KITCHENY && xDest == KITCHENX) || (yDest == STOVEY && xDest == STOVEX) || (yDest == COUNTERY && xDest == COUNTERX))){
        	cook.atDest();
        }
	}

	public void draw(Graphics2D g) {
        g.drawString("CHEF", xPos - 5, yPos - 5);
        g.setColor(Color.ORANGE);
        g.fillRect(xPos, yPos, 20, 20);
        if(foodCooking){
			g.setColor(Color.PINK);
			g.fillOval(STOVEX - 7, STOVEY + 7, 16, 16);
        }
		if(foodDone){
			g.setColor(Color.WHITE);
			g.fillOval(STOVEX - 10, KITCHENY + 10, 16, 16);
			g.setColor(Color.BLACK);
			g.fillOval(STOVEX - 8, KITCHENY + 12, 13, 13);
		}
	}

	public boolean isPresent() {
		return isPresent;
	}
	
	public void doStartCooking(){	//first move to the refrigerator
		xDest = KITCHENX;
		yDest = KITCHENY;
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
		if(t)
			isPresent = true;
		else
			isPresent = false;
	}


}
