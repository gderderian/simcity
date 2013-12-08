package restaurant1.gui;

import city.gui.Gui;
import restaurant1.Restaurant1CookRole;

import java.awt.*;
import java.util.*;
import java.util.List;

import javax.swing.ImageIcon;

/* NOTE: This code is not used in Restaurant V2 */

public class Restaurant1CookGui implements Gui {
	
	private static final int XPOS = 700, YPOS = 350;
	private final int FRIDGE_Y = 440;
	private final int GRILL_X = 720, GRILL_Y = 320;
	private final int COUNTER_X = 680, COUNTER_Y = 340;
	
	private boolean moving;
	
	boolean isPresent = true;
	
    private Restaurant1CookRole agent = null;

    private int xPos = XPOS, yPos = YPOS;//default waiter position
    private int xDestination = XPOS, yDestination = YPOS;//default start position
    
	ImageIcon icon1 = new ImageIcon("images/cook1.png");
	ImageIcon icon2 = new ImageIcon("images/cook2.png");
	ImageIcon icon3 = new ImageIcon("images/cook3.png");
	
	private int movementCounter = 0;
	private final int iconSwitch = 10; //Rate at which icons switch during movement
	
	ImageIcon icon = icon1;
	
    
    private List<MyOrder> orders = Collections.synchronizedList(new ArrayList<MyOrder>());
    int cookingLocation = 0, waitingLocation = 0;
    
    public Restaurant1CookGui(Restaurant1CookRole agent) {
        this.agent = agent;
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
        
        // Drawing the cooking area
        g.setColor(Color.GRAY);
        g.fillRect(750, 250, 40, 200);
        
        g.setColor(Color.BLACK);
        g.fillRect(755, 255, 30, 190);
        
        // Drawing the waiting area
        g.setColor(Color.DARK_GRAY);
        g.fillRect(650, 250, 40, 200);
        
        // Drawing the refrigerator
        g.setColor(Color.WHITE);
        g.fillRect(690, 470, 60, 50);
        g.setColor(Color.BLACK);
        g.drawRect(689, 469, 61, 51);
        g.drawString("Fridge", 700, 495);
        
        synchronized(orders) {
        	for(MyOrder o : orders) {
        		if(o.s == orderState.cooking) {
        			drawOrderCooking(o, g);
        		} else if (o.s == orderState.carried) {
        			drawOrderCarried(o, g);
        		} else if (o.s == orderState.waiting) {
        			drawOrderWaiting(o, g);
        		}
        	}
        }
    }

    public boolean isPresent() {
        return isPresent;
    }
    
	public void setPresent(boolean p) {
		isPresent = p;
	}
    
    public void drawOrderCooking(MyOrder o, Graphics2D g) {
    	int xLoc = 760;
    	int yLoc = 260 + o.location * 30;
    	
    	g.setColor(Color.WHITE);
    	g.fillRect(xLoc, yLoc, 20, 20);

        g.setFont(new Font("Arial", Font.BOLD, 12));
        g.setColor(Color.BLACK);
        String choiceLetter = o.type.substring(0,2); // First two letters of current food
        g.drawString(choiceLetter, xLoc + 4, yLoc + 14);
    }
    
    public void drawOrderCarried(MyOrder o, Graphics2D g) {
    	int xLoc = xPos + 5;
    	int yLoc = yPos + 25;
    	
    	g.setColor(Color.WHITE);
    	g.fillRect(xLoc, yLoc, 20, 20);

        g.setFont(new Font("Arial", Font.BOLD, 12));
        g.setColor(Color.BLACK);
        String choiceLetter = o.type.substring(0,2); // First two letters of current food
        g.drawString(choiceLetter, xLoc + 4, yLoc + 14);
    }
    
    public void drawOrderWaiting(MyOrder o, Graphics2D g) {
    	int xLoc = 655;
    	int yLoc = 260 + o.location * 30;
    	
    	g.setColor(Color.WHITE);
    	g.fillRect(xLoc, yLoc, 20, 20);

        g.setFont(new Font("Arial", Font.BOLD, 12));
        g.setColor(Color.BLACK);
        String choiceLetter = o.type.substring(0,2); // First two letters of current food
        g.drawString(choiceLetter, xLoc + 4, yLoc + 14);
    }
    
    public void msgNewOrder(String type, int orderNumber) {
    	synchronized(orders) {
    		orders.add(new MyOrder(type, orderNumber, cookingLocation));
    		cookingLocation = (cookingLocation + 1) % 5;
    	}
    }
    
    public void msgOrderCooking(int number) {
    	synchronized(orders) {
    		for(MyOrder o : orders) {
    			if(o.orderNumber == number) {
    				o.s = orderState.cooking;
    			}
    		}
    	}
    }
    
    public void msgOrderWaiting(int number) {
    	synchronized(orders) {
    		for(MyOrder o : orders) {
    			if(o.orderNumber == number) {
    				o.s = orderState.waiting;
    				o.setLoc(waitingLocation);
    				waitingLocation = (waitingLocation + 1) % 5;
    			}
    		}
    	}
    }
    
    public void msgOrderBeingCarried(int number) {
    	synchronized(orders) {
    		for(MyOrder o : orders) {
    			if(o.orderNumber == number) {
    				o.s = orderState.carried;
    			}
    		}
    	}
    }
    
    public void msgOrderPickedUp(int number) {
    	synchronized(orders) {
    		for(int i = 0; i < orders.size(); i++) {
    			if(orders.get(i).orderNumber == number) {
    				orders.remove(i);
    			}
    		}
    	}
    }
    
    public void DoGoToHome() {
    	xDestination = XPOS;
    	yDestination = YPOS;
    }
    
    public void DoGoToFridge() {
    	xDestination = XPOS;
    	yDestination = FRIDGE_Y;
    	moving = true;
    }
    
    public void DoGoToGrill() {
    	xDestination = GRILL_X;
    	yDestination = GRILL_Y;
    	moving = true;
    }
    
    public void DoGoToCounter() {
    	xDestination = COUNTER_X;
    	yDestination = COUNTER_Y;    	
    	moving = true;
    }
    
    class MyOrder {
    	String type;
    	orderState s = orderState.carried;
    	int orderNumber;
    	int location;
    	
    	MyOrder(String type, int orderNumber, int location) {
    		this.type = type;
    		this.orderNumber = orderNumber;
    		this.location = location;
    	}
    	
    	void setLoc(int x) {
    		location = x;
    	}
    }
    
    private enum orderState { carried, cooking, waiting };
}
