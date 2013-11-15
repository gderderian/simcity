package city.gui.restaurant2;

import interfaces.Restaurant2Customer;
import interfaces.Restaurant2Waiter;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;

import city.gui.CityGui;
import city.gui.Gui;

public class Restaurant2WaiterGui implements Gui{
	
	private Restaurant2Waiter agent = null;

    private int xPos = 0;
    private int yPos = 0;//default waiter position
    private int xDestination = 20, yDestination = 20;//default start position
    
    private final int HOMEX, HOMEY;

    private final int WINDOWX = 600;
    private final int WINDOWY = 600 - 20;
    private final int TABLEDIM = 50;
    private final int TABLESPACING = WINDOWY/10;
    private final int TABLE1Y = TABLESPACING - 20;
    private final int TABLE2Y = TABLE1Y + TABLESPACING*2;
    private final int TABLE3Y = TABLE1Y + TABLESPACING*5;
    private final int TABLE4Y = TABLE1Y + TABLESPACING*7;
    private final int TABLEX = WINDOWX/2 - TABLEDIM/2 + 20;
    private final int KITCHENY = WINDOWY/2 - 20;		//goes to the counter of the kitchen
    private final int KITCHENX = WINDOWX - 145;

    
    public final int XBREAK = 400;
    public final int YBREAK = -10;
    
    public String name;
    
    private String foodChoice;
    
    private CityGui restGui;
    
    boolean onBreak;
    
    int waiterNum;
    
    private boolean deliverFood;
    private boolean breakRequested = false;
    	//this is true if waiter is on break, or if break is simply on request

    public Restaurant2WaiterGui(Restaurant2Waiter agent, String n, CityGui g, int i) {
        this.agent = agent;
        
        name = n;
        deliverFood = false;
        
        waiterNum = i;
        
        if(waiterNum % 4 == 0) HOMEY = 140;
        else if(waiterNum % 4 == 3) HOMEY = 100;
        else if(waiterNum % 4 == 2) HOMEY = 60;
        else if(waiterNum % 4 == 1) HOMEY = 20;
        else HOMEY = 0;
        
        if(waiterNum < 5) HOMEX = 30;
        else if(waiterNum < 9) HOMEX = 60;
        else if(waiterNum < 13) HOMEX = 90;
        else if(waiterNum < 17) HOMEX = 120;
        else HOMEX = 0;
        
        xDestination = HOMEX;
        yDestination = HOMEY;
        
        onBreak = false;
        restGui = g;
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

        if (xPos == xDestination && yPos == yDestination){
        	if ((yPos == (TABLE1Y) || yPos == (TABLE2Y) || yPos == (TABLE3Y) || yPos == (TABLE4Y)) && xPos == (TABLEX)){
        		agent.msgAtDest();}
            if(xPos == KITCHENX && yPos == KITCHENY){
            	agent.msgAtDest();
            }
        }
        if(xPos == HOMEX && yPos == HOMEY){
        	agent.setAtStand(true);
        }
        else if(xPos > 0 || yPos > 0){
        	agent.setAtStand(false);
        }
 
    }

    public void draw(Graphics2D g) {
    	//System.out.println(agent.getName() + ", " + xPos + ", " + yPos + "Dest: " + xDestination + ", " + yDestination);
        g.setColor(Color.MAGENTA);
        g.fillRect(xPos, yPos, 20, 20);
		g.setColor(Color.BLACK);
		g.setFont(new Font("TimesRoman", Font.PLAIN, 16));
        g.drawString(name, xPos, yPos);
        if(deliverFood){
        	g.setColor(Color.DARK_GRAY);
        	g.fillOval(xPos+12, yPos+10, 16, 16);
        	g.setColor(Color.DARK_GRAY);
        	g.drawString(foodChoice, xPos+23, yPos+20);
        }
    }

    public boolean isPresent() {
        return true;
    }

    public void DoGoToTable(Restaurant2Customer customer, int table) {
    	if(table == 1){
            yDestination = TABLE1Y;
    	}
    	else if(table == 2){
            yDestination = TABLE2Y;
    	}
    	else if(table == 3){
            yDestination = TABLE3Y;
    	}
    	else{
    		yDestination = TABLE4Y;
    	}
        xDestination = TABLEX;
    }

    public void DoLeaveCustomer() {
        xDestination = 20;
        yDestination = 20;   
    }

    public int getXPos() {
        return xPos;
    }

    public int getYPos() {
        return yPos;
    }
    
    public void DoGoToKitchen(){
    	xDestination = KITCHENX;
    	yDestination = KITCHENY;
    }
    
    public void DoDeliverFood(String choice, int table){
    	deliverFood = true;
    	foodChoice = choice;
    	if(table == 1){
    		yDestination = TABLE1Y;
    	}
    	else if(table == 2){
    		yDestination = TABLE2Y;
    	}
    	else if(table ==3){
    		yDestination = TABLE3Y;
    	}
    	else if(table == 4){
    		yDestination = TABLE4Y;
    	}
    	xDestination = TABLEX;
    	
    }
    
    public void setDelivering(boolean d){
    	deliverFood = d;
    }
    
	public void requestBreak() {
		breakRequested = true;
		agent.msgBreakRequested();
		System.out.println(name + ": requesting a break");
	}
	public boolean isRequested() {
		return breakRequested;
	}
	
	public void DoTakeBreak(){
		onBreak = true;
		xDestination = XBREAK;
		yDestination = YBREAK;
		restGui.enableComeBack(agent);
		breakRequested = false;
	}
	
	public void setDeniedBreak(){
		restGui.setEnabled(agent);
		breakRequested = false;
	}
	
	public boolean isOnBreak(){
		if(onBreak){
			return true;
		}
		return false;
	}
	
	public void setOnBreak(boolean b){
		onBreak = b;
	}

}
