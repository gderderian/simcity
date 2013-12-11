package city.gui.Restaurant5;

import city.gui.Gui;
import city.Restaurant5.*;
import city.Restaurant5.Restaurant5CookRole.cookingorder;
import city.Restaurant5.Restaurant5CookRole.cookstate;




/*
import restaurant.CookAgent.cookingorder;
import restaurant.CookAgent.cookingorderstate;
import restaurant.CookAgent.cookstate;
import restaurant.CookAgent.finishedorder;
import restaurant.CookAgent.finishedorderstate;
import restaurant.CustomerAgent;
import restaurant.HostAgent;
import restaurant.CookAgent;
import restaurant.interfaces.Waiter;
*/
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Collections;

import javax.swing.ImageIcon;

import tomtesting.interfaces.Restaurant5Waiter;

public class Restaurant5CookGui implements Gui {

    private Restaurant5CookRole agent = null;
    Restaurant5Gui gui;
    private int xPos = 440, yPos = 50;//default waiter position
    private int xDestination = 440, yDestination = 50;//default start position
    public List<ordergui> orderguis = Collections.synchronizedList(new ArrayList<ordergui>());
    private int[] counterxpos = {345, 395, 445, 495};
    
	ImageIcon icon1 = new ImageIcon("images/cook1.png");
	ImageIcon icon2 = new ImageIcon("images/cook2.png");
	ImageIcon icon3 = new ImageIcon("images/cook3.png");
	
	private int movementCounter = 0;
	private final int iconSwitch = 10; //Rate at which icons switch during movement
	
	ImageIcon icon = icon1;
    
  
    
    
    int counter = 10;
    int spotnumber = 0;
    
    boolean isPresent = true;
    
    //private int[] counterypos = {
    
    public Restaurant5CookGui(Restaurant5CookRole agent, Restaurant5Gui gui) {
        this.agent = agent;
        this.gui = gui;
        //orderguis.add(new ordergui(460, 80));
        //orderguis.add(new ordergui(430, 80));
        //orderguis.add(new ordergui(400, 80));
        //orderguis.add(new ordergui(470, 80));
    }
    
    public void msgAddOrderGui(String order, Restaurant5Waiter waiter) {
    	
    	if(spotnumber == 3)
    	spotnumber = 0;
    	orderguis.add(new ordergui(order, waiter, counterxpos[spotnumber], 80));
    	spotnumber++;
    	
    }
    
    public void msgOrderGuiPending(String order, Restaurant5Waiter waiter) {
    	
    	synchronized(orderguis)
    	{
    		
    	for(ordergui findordergui : orderguis)
    	{
    		//System.out.println("in for loop!!!!!!!!!!");
    		if(findordergui.waiter == waiter && findordergui.order == order)
    		{
    			//System.out.println("found!!!!!!!!!!");
    			findordergui.state = orderguistate.pending;
    		}
    	}
    	
    	}
    	
    }
    
    public void msgfoodpickedup(cookingorder removeorder) {
    	
    	synchronized(orderguis) {
    		
   
    	for(ordergui removeordergui : orderguis)
    	{
    		//System.out.println("in for loop!!!!!!!!!!");
    		if(removeordergui.waiter == removeorder.waiter && removeordergui.order == removeorder.order)
    		{
    			//System.out.println("found!!!!!!!!!!");
    			orderguis.remove(removeordergui);
    			return;
    		}
    	}
    	
    }
    }

    public void updatePosition() {
        //System.out.println(agent.cooking);
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
        else if(agent.cooking == true) {
        	yDestination = 30;
        }
        else if(agent.cooking == false) {
        	yDestination = 50;
        }
        
    }

    public void draw(Graphics2D g) {
    	
    	g.drawImage(icon.getImage(), xPos, yPos - 20, 30, 30, null);
    
    	//g.setColor(Color.black);
        //g.fillRect(xPos, yPos, 20, 20);
        /*
        for( int i = 0; i < 4; i++)
        {
        	g.setColor(Color.red);
        	g.fillRect(counterxpos[i], 80, 25, 25);
        }
        */
        
        if(agent.state == cookstate.cooking)
        {
        	g.setColor(Color.black);
        	yDestination = 30;
        	
        	// concurrent modification happens here
        	
        	
        	
        	
        }
        

    	for(ordergui displayordergui: orderguis) {
    		if(displayordergui.state == orderguistate.cooking)
    		{
    			
    			Graphics2D g2 = (Graphics2D)g;
    			g2.setColor(Color.WHITE);
        		g2.fillRect(displayordergui.xcoordinate - 2, displayordergui.ycoordinate - 74, 46, 20);
        		g.setFont(new Font("Arial", Font.BOLD, 12));
    			g.setColor(Color.BLACK);
    			g.drawString(displayordergui.order, displayordergui.xcoordinate, displayordergui.ycoordinate - 60);
      
    			
    		}
    	}
        
        
       
        
        
        for(ordergui displayordergui : orderguis) {
        		if(displayordergui.state == orderguistate.pending)
        		{
        			
        			Graphics2D g2 = (Graphics2D)g;
        			g2.setColor(Color.WHITE);
            		g2.fillRect(displayordergui.xcoordinate - 2, displayordergui.ycoordinate + 3, 46, 20);
            		g.setFont(new Font("Arial", Font.BOLD, 12));
        			g.setColor(Color.BLACK);
        			g.drawString(displayordergui.order, displayordergui.xcoordinate, displayordergui.ycoordinate + 16);
        		}
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

    public int getXPos() {
        return xPos;
    }

    public int getYPos() {
        return yPos;
    }
    
   
    public enum orderguistate {cooking, pending, pickedup };
    
    public static class ordergui {
    	public String order;
    	int xcoordinate;
    	int ycoordinate;
    	public Restaurant5Waiter waiter;
    	int table;
    	public orderguistate state;
    	public boolean occupied;
    	
    	public ordergui(String order, Restaurant5Waiter waiter, int xcoordinate, int ycoordinate)
    	{
    		this.order = order;
    		this.waiter = waiter;
    		this.xcoordinate = xcoordinate;
    		this.ycoordinate = ycoordinate;
    		this.table = table;
    		this.state = orderguistate.cooking;
    		occupied = false;
    	}
    		
    }
    
}
