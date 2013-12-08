package restaurant1.gui;

import city.gui.Gui;
import restaurant1.Restaurant1CustomerRole;
import restaurant1.Restaurant1HostRole;
import restaurant1.Restaurant1WaiterRole;

import java.awt.*;
import java.awt.image.ImageObserver;

import javax.swing.ImageIcon;
import javax.swing.JLabel;

public class Restaurant1WaiterGui implements Gui {
	
	private static final int XPOS = -30, YPOS = -30;
	private static final int WIDTH = 30, HEIGHT = 30;
	private final int COOK_X = 620, COOK_Y = 320;
	private final int CASHIER_X = 80, CASHIER_Y = 300;
	private final int LOBBY_X = 100, LOBBY_Y = 110;
	
	private int home_x, home_y;
    
	ImageIcon icon1 = new ImageIcon("images/waiter1.png");
	ImageIcon icon2 = new ImageIcon("images/waiter2.png");
	ImageIcon icon3 = new ImageIcon("images/waiter3.png");
	
	private int movementCounter = 0;
	private final int iconSwitch = 10; //Rate at which icons switch during movement
	
	ImageIcon icon = icon1;
	
	private boolean moving = false;
	private boolean carryingFood = false;
	private boolean onBreak = false;
	
	boolean isPresent = true;
	
	private int cycleCount = 0;

    private Restaurant1WaiterRole agent = null;
    
    String currentFood;

    private int xPos = XPOS, yPos = YPOS;//default waiter position
    private int xDestination = XPOS, yDestination = YPOS;//default start position

    public Restaurant1WaiterGui(Restaurant1WaiterRole agent) {
        this.agent = agent;

        // Initial mapping of table locations!
        tableLocations.put(new Integer(1), new Dimension(200, 200));
        tableLocations.put(new Integer(2), new Dimension(450, 200));
        tableLocations.put(new Integer(3), new Dimension(200, 400));
        tableLocations.put(new Integer(4), new Dimension(450, 400));
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
        
        if(xPos == xDestination && yPos == yDestination && moving == true) {
        	moving = false;
        	agent.msgAtDestination();
        }
    }
    
    public void draw(Graphics2D g) {
    	g.drawImage(icon.getImage(), xPos, yPos, 30, 30, null);
        
        if(carryingFood) {
        	// If the waiter is delivering food, this code adds the food to the waiter's animation
        	g.setColor(Color.WHITE);
        	g.fillRect(xPos - 20, yPos + 10, 20, 20);

            g.setFont(new Font("Arial", Font.BOLD, 12));
            g.setColor(Color.BLACK);
            String choiceLetter = currentFood.substring(0,2); // First two letters of current food
            g.drawString(choiceLetter, xPos -16, yPos + 25);
        }
        
        if(onBreak) {
        	g.setColor(Color.WHITE);
        	g.fillOval(xPos - 10, yPos - 15, 50, 20);
        	
        	g.setFont(new Font("Arial", Font.BOLD, 14));
            g.setColor(Color.BLACK);
            
            if(cycleCount <= 25) {
            	g.drawString("zzz", xPos, yPos);
            } else if(cycleCount > 25 && cycleCount <= 50) {
            	g.drawString("zzz.", xPos, yPos);
            } else if(cycleCount > 50 && cycleCount <= 75) {
            	g.drawString("zzz..", xPos, yPos);
            } else if(cycleCount < 100) {
            	g.drawString("zzz...", xPos, yPos);
            }
            
            cycleCount = (cycleCount + 1) % 100;
        }
    }

    public boolean isPresent() {
        return isPresent;
    }
    
	public void setPresent(boolean p) {
		isPresent = p;
	}
    
    public void takeBreak() {
    	
    }

    public void DoBringToTable(Restaurant1CustomerRole c, int table) {
    	xDestination = (int) tableLocations.get(table).getWidth() + WIDTH;
		yDestination = (int) tableLocations.get(table).getHeight() - HEIGHT;
		moving = true;
        
        GiveTableNumberToCustomerGui(c, table);
    }
    
    public void GiveTableNumberToCustomerGui(Restaurant1CustomerRole c, int table) {
    	c.getGui().GivenTableNumber(table);
    }
    
    public void DoGoToTable(int table) {
    	xDestination = (int) tableLocations.get(table).getWidth() + WIDTH;
		yDestination = (int) tableLocations.get(table).getHeight() - HEIGHT;
		moving = true;
    }
    
    public void DoGoToLobby() {
    	xDestination = LOBBY_X;
    	yDestination = LOBBY_Y;
    	moving = true;
    }
    
    public void DoGoToCustomer(Restaurant1CustomerRole c) {
    	xDestination = c.getGui().getGuiX() + 30;
    	yDestination = c.getGui().getGuiY() + 30;
    	moving = true;
    }
    
    public void msgHereAreMyCoords(int x, int y) {
    	xDestination = x + 30;
    	yDestination = y + 30;
    	moving = true;
    }
    
    public void DoGoToBreakZone() {
    	xDestination = home_x - 1;
    	yDestination = home_y + 1;
    	moving = true;
    }
    
    public void DoGoToCook() {
    	xDestination = COOK_X;
    	yDestination = COOK_Y;
    	moving = true;
    }
    
    public void DoGoToCashier() {
    	xDestination = CASHIER_X;
    	yDestination = CASHIER_Y;
    	moving = true;
    }
    
    public void DoGoHome() {
    	xDestination = home_x;
    	yDestination = home_y;
    }
    
    public void msgBreakStarted() {
    	onBreak = true;
    }
    
    public void msgBreakFinished() {
    	onBreak = false;
    }
    
    public void deliveringFood(String food) {
    	currentFood = food;
    	carryingFood = true;
    }
    
    public void foodDelivered() {
    	carryingFood = false;
    }
    
    public void setHome(int x, int y) {
    	home_x = x;
    	home_y = y;
    }
}
