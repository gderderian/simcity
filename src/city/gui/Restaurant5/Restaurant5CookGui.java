package city.gui.Restaurant5;


import restaurant.CookAgent.cookingorder;
import restaurant.CookAgent.cookingorderstate;
import restaurant.CookAgent.cookstate;
import restaurant.CookAgent.finishedorder;
import restaurant.CookAgent.finishedorderstate;
import restaurant.CustomerAgent;
import restaurant.HostAgent;
import restaurant.CookAgent;
import restaurant.interfaces.Waiter;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Collections;

public class Restaurant5CookGui implements Gui {

    private CookAgent agent = null;
    RestaurantGui gui;
    private int xPos = 440, yPos = 50;//default waiter position
    private int xDestination = 440, yDestination = 50;//default start position
    public List<ordergui> orderguis = Collections.synchronizedList(new ArrayList<ordergui>());
    private int[] counterxpos = {345, 395, 445, 495};
    int counter = 10;
    int spotnumber = 0;
    
    //private int[] counterypos = {
    
    public Restaurant5CookGui(CookAgent agent, RestaurantGui gui) {
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
        g.setColor(Color.black);
        g.fillRect(xPos, yPos, 20, 20);
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
    			g.drawString(displayordergui.order, displayordergui.xcoordinate, displayordergui.ycoordinate - 60);
    		}
    	}
        
        
        
        /*
        if(agent.state == cookstate.plating)
        {
        	yDestination = 50;
        	//System.out.print("i'm plting");
        	for(cookingorder displaycookingorder: agent.cookingorders) {
        		if(displaycookingorder.state == cookingorderstate.waiting)
        		{
        			//System.out.print("i'm plating");
        			g.drawString(displaycookingorder.order,xPos, yPos + 1);
        			break;
        			
        		}
        	}	
        	
        }
        */
        
        /*
        for(cookingorder displaycookingorder: agent.cookingorders) {
    		if(displaycookingorder.state == cookingorderstate.waiting)
    		{
    			orderguis.add( new ordergui(displaycookingorder.order, displaycookingorder.waiter, 442, 100) );
    			
    		}
    	}
    	*/
        /*
        for(ordergui displayordergui: orderguis) {
        	g.setColor(Color.black);
        	g.drawString(displayordergui.order, displayordergui.xcoordinate, displayordergui.ycoordinate);
        	
        }
        */
        
        
        
        
        /*
        if(agent.donecooking == true)
        {
        	//System.out.print("i'm done cooking man");
        	int counter = 0;
        	if(agent.currentorder.order.equals("chicken"))
        	{
        		g.drawString("Chicken",counterxpos[counter],80);
        	}
        	
        }
        */
        
        /*
        for(cookingorder displaycookingorder: agent.cookingorders) 
        {
    		if(displaycookingorder.state == cookingorderstate.waiting)
    		{
    			//g.drawString("pickme up" + displaycookingorder.order,xPos, yPos + 20);		
    		}
    	}
    	*/
        
        
        
        for(ordergui displayordergui : orderguis) {
        		if(displayordergui.state == orderguistate.pending)
        		{
        		g.setColor(Color.black);
        		g.drawString(displayordergui.order, displayordergui.xcoordinate, displayordergui.ycoordinate + 10);	
       
        		}
        }
        /*
        for(finishedorder displayfinishedorder: agent.finishedorders) {
        	if(displayfinishedorder.state == finishedorderstate.pending)
        	{
        		
        		g.drawString(displayfinishedorder.order, xPos, yPos + counter);
        		//counter += 10;
        	}
        	
        }
        */
        
        
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
