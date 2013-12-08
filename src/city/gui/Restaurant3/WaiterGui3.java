package city.gui.Restaurant3;

import city.Restaurant3.*;
import city.gui.Gui;
import java.awt.*;

public class WaiterGui3 implements Gui {

    private WaiterRole3 agent = null;

	RestaurantGui3 gui;
    
    private int xPos = 0, yPos = 0;
    public int xDestination = 0, yDestination = 0, host_tableX, host_tableY;
    
    private static final int HOST_SIZE_X = 20;
    private static final int HOST_SIZE_Y = 20;

	boolean isAnimating = false;
	boolean hasDestination = false;
	
	boolean isPresent = true;
	
	String carryingOrderText = "";
	boolean isOnBreak;
	int index = 0;
    
    public WaiterGui3(WaiterRole3 a) {
    	agent = a;
    	carryingOrderText = "";
    	isOnBreak = false;
    }
    
    public WaiterGui3(WaiterRole3 a, RestaurantGui3 g) {
    	agent = a;
    	gui = g;
    	carryingOrderText = "";
    	isOnBreak = false;
    }
    
    public WaiterGui3(WaiterRole3 a, RestaurantGui3 g, int startX, int startY, int indexNum) {
    	agent = a;
    	gui = g;
    	xPos = startX;
    	yPos = startY;
    	xDestination = startX;
    	yDestination = startY;
    	carryingOrderText = "";
    	isOnBreak = false;
    	index = indexNum;
    	hasDestination = false;
    }

	public void setDestination(int newX, int newY){
		xDestination = newX;
		yDestination = newY;
		hasDestination = true;
	}
    
    public void updatePosition() {
        if (xPos < xDestination)
            xPos++;
        else if (xPos > xDestination)
            xPos--;
        if (yPos < yDestination)
            yPos++;
        else if (yPos > yDestination){
            yPos--;
        } else if (xPos == xDestination && yPos == yDestination){
        	if (isAnimating){
        		doneAnimating();
        	}	
        }
    }

    public void draw(Graphics2D g) {
        g.setColor(Color.MAGENTA);
        g.fillRect(xPos, yPos, HOST_SIZE_X, HOST_SIZE_Y);
		if (!carryingOrderText.equals("")){
			g.drawString(carryingOrderText, xPos, yPos);
		}
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
    
	public void beginAnimate(){
		isAnimating = true;
	}

	public void doneAnimating(){
		hasDestination = false;
		isAnimating = false;
		agent.releaseSemaphore();
	}
    
    public void DoBringToTable(CustomerRole3 customer, int tableX, int tableY) {
        xDestination = tableX + 15;
        yDestination = tableY - 15;
        host_tableX = tableX;
        host_tableY = tableY;
    }

    public void DoLeaveCustomer() {
        xDestination = -20;
        yDestination = -20;
    }

    public int getXPos() {
        return xPos;
    }

    public int getYPos() {
        return yPos;
    }
    
    public void setCarryText(String carryText){
    	carryingOrderText = carryText;
    }
    
	public boolean isOnBreak() {
		return isOnBreak;
	}
	
	public void setRequestBreak() {
		agent.requestBreak();
		isOnBreak = true;
	}
	
	public void breakRejected() {
		gui.setCbEnabled(agent);
		isOnBreak = false;
	}
	
	public void returnFromBreak() {
		isOnBreak = false;
		gui.setCbEnabled(agent);
	}
	
	public void breakApproved() {
		isOnBreak = true;
	}
	
	public void requestedBreak() {
		isOnBreak = true;
	}
}
