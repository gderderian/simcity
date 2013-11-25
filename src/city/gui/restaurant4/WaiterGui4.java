package city.gui.restaurant4;


import city.Restaurant4.CustomerRole4;
import city.Restaurant4.WaiterRole4;

import java.awt.*;

import justinetesting.interfaces.Customer4;


public class WaiterGui4 implements Gui4 {
	
    private WaiterRole4 agent = null;

    private int xPos = 40, yPos = 250;//default waiter position
    private int xDestination = 40, yDestination = 250;//default start position
    private int dimension= 20;
    private int movement= 20;
    private String choice;
    private boolean doGoToEntrance= false;
    private boolean doBringFood= false;

    public int temp= 0;
    public int xTable = 100;
    public int xTableNew;
    public final int yTable = 250;
    public static final int xCook= 325;
    public static final int yCook= 450;
    public final int xHome= 40;
    public int yHome= 100;
    public static final int xEntrance= 60;
    public static final int yEntrance= 60;
    private static final int foodDisplacement= 30;

    public WaiterGui4(WaiterRole4 agent) {
        this.agent = agent;
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

        if (xPos == xDestination && yPos == yDestination
        		& (xDestination == xTableNew + movement) & (yDestination == yTable - movement)) {
            doBringFood= false;
        	agent.msgAtTable();
        }
        if (xPos > xCook && yPos > yCook) {
            agent.msgAtCook();
            return;
        }
        if(xPos <= xEntrance && yPos <=yEntrance && doGoToEntrance){
        	doGoToEntrance= false;
    		agent.msgAtEntrance();
        }
        if(xPos <= xHome && yPos >= yHome){
        	agent.msgAtHome();
        }
    }

    public void draw(Graphics2D g) {
        g.setColor(Color.MAGENTA);
        g.fillRect(xPos, yPos, dimension, dimension);
        
        if(doBringFood){
			g.setColor(Color.BLACK);
			if(choice == "Eggs"){
				g.drawString("Eggs", xPos, yPos + foodDisplacement);
			}
			else if(choice == "Waffels"){
				g.drawString("Waffels", xPos, yPos + foodDisplacement);
			}
			else if(choice == "Pancakes"){
				g.drawString("Pancakes", xPos, yPos + foodDisplacement);
			}
			else if(choice == "Bacon"){
				g.drawString("Bacon", xPos, yPos + foodDisplacement);
			}
		}
    }

    public boolean isPresent() {
        return true;
    }

    public void doGoToTable(Customer4 c, int tableNumber) {
        xTableNew= xTable * tableNumber;
        System.out.println("Table Number DoBringToTable: " + tableNumber);
    	xDestination = xTableNew + movement;
        yDestination = yTable - movement;
    }
    
    public void doGoToCook(){
    	xDestination = xCook + movement;
    	yDestination = yCook + movement;
    }
    
    public void doBringFood(int table, String choice){
    	xTableNew= xTable * table;
    	xDestination = xTableNew + movement;
        yDestination = yTable - movement;
        doBringFood= true;
        this.choice= choice;
    }
    
    public void doGoToEntrance(){
    	xDestination= xEntrance - movement;
    	yDestination= xEntrance - movement;
    	doGoToEntrance= true;
    }
    
    public void doGoBack(){
    	xDestination= xHome - movement;
    	yDestination= yHome + movement;
    }
    
    public void doLeaveCustomer() {
        xDestination = xEntrance -movement;
        yDestination = xEntrance -movement;
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
    
    public void setHomePostion(int num){
    	yHome += (num * (dimension + 5));
    	yDestination += (num * (dimension + 5));
    }
}
