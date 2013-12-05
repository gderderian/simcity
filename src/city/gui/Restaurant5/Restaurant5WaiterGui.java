package city.gui.Restaurant5;
//this is my real file

import city.gui.Gui;
import tomtesting.interfaces.Restaurant5Customer;
import tomtesting.interfaces.Restaurant5Waiter;
import tomtesting.interfaces.Restaurant5Host;
import tomtesting.interfaces.Restaurant5Cook;
import city.PersonAgent;
//import restaurant.HostAgent;
//import restaurant.WaiterAgent;
//import restaurant.CustomerAgent;
import city.Restaurant5.*;
import city.Restaurant5.Restaurant5WaiterRole.AgentState;

import java.awt.*;

import javax.swing.ImageIcon;

//import restaurant.CookAgent;
public class Restaurant5WaiterGui implements Gui {

	private Restaurant5WaiterRole  agent = null;
	Restaurant5Gui gui; 
	private boolean isPresent = false;
	private boolean onBreak = false;
	PersonAgent person;
	
	//original position was -20 -20
	public int xPos = 20 ,yPos = 20;//default waiter position
	public int xDestination = 20, yDestination = 20;//default start position
	public Image imgofchicken = new ImageIcon("/Users/teryunlee/Documents/restaurant_teryunle/src/restaurant/foodpics/Chicken.jpg").getImage();
	public Image imgofburrito = new ImageIcon("/Users/teryunlee/Documents/restaurant_teryunle/src/restaurant/foodpics/burrito.jpeg").getImage();
	public Image imgofpizza = new ImageIcon("/Users/teryunlee/Documents/restaurant_teryunle/src/restaurant/foodpics/pizza.jpg").getImage();
	private int tableServing;
	int table;
	
	int xhomepos;
	int yhomepos;

	public static int xTable = 200;
	public static int yTable = 250;

	public static int xTable2 = 260;
	public static int yTable2 = 250;

	public static int xTable3 = 320;
	public static int yTable3 = 250;

	public static int xTable4 = 380;
	public static int yTable4 = 250;


	private boolean returningtolobby = false;
	public boolean atcustomertable = false;
	public boolean atkitchen = false;
	public boolean atTable = false;

	private int xcoordinatesoftables[];
	private int ycoordinatesoftables[];

	public Restaurant5WaiterGui(Restaurant5WaiterRole agent, Restaurant5Gui gui, Restaurant5HostRole host) {
		this.agent = agent;
		this.gui = gui;
		xcoordinatesoftables = host.getxcoordinatesTables();
		ycoordinatesoftables = host.getycoordinatesTables();
		//yPos = y;
		//yDestination = y;
	}
	//pass in x and y coordinates
	public void updatePosition() {
	
		if (xPos < xDestination)
			xPos++;
		else if (xPos > xDestination)
			xPos--;

		if (yPos < yDestination)
			yPos++;
		else if (yPos > yDestination)
			yPos--;
		
		if (xPos == xDestination && yPos == yDestination & (xDestination == agent.xCoordinate + 20) & (yDestination == agent.yCoordinate - 20)) 
		{
			agent.msgAtTable();	
		}
		
		//System.out.println("x :" + xPos + "   " + "y: " + yPos);
		

		if(xPos == 20 && yPos == 20 && returningtolobby)
		{
			agent.msgAtLobby();
			returningtolobby = false;
			atkitchen = false;
			atTable = false;
		}
		
		/*
		if(xPos == 20 && yPos == 20) {
			gotohomeposition();
		}
		*/
		
		if (xPos != xDestination || yPos != yDestination)
		{
			atcustomertable = true;
//			System.out.println("atcustomertable " + atcustomertable);
		}
		
		//System.out.println(atcustomertable);
		if(xPos == xDestination && yPos == yDestination && /*atcustomertable &&*/ (xPos != 20 || yPos != 20))
		{
			//System.out.println(("i'm at the table in gui"));
			//atTable.release();
			if(xPos == xcoordinatesoftables[table - 1] + 20 && yPos == ycoordinatesoftables[table - 1] - 20)
			{	
				
				//System.out.println(("i'm at the table in gui 2"));	
			//System.out.print("table value" + table);
			if (atTable == false) {
				//System.out.println(("i'm at the table in gui 3"));	
				agent.msgAtTable();
				atTable = true;
			}
			atcustomertable = false;
			atkitchen = false;
			}
		}
		
		if(xPos == 440 && yPos == 70 && atkitchen == false)
		{
			agent.atKitchen.release();
			atkitchen = true;
		}

	}

	public void draw(Graphics2D g) {
		g.setColor(Color.black);
		g.fillRect(xPos, yPos, 20, 20);
		if(agent.foodout == true) {
			g.drawString("Foodout", xPos - 10, yPos - 10);
		}
		if(agent.state == AgentState.BringingFood && agent.currentfood == "chicken") {
			//g.drawImage(imgofchicken, xPos + 10 , yPos + 10, 20, 20, gui);
			g.drawString("Chicken", xPos + 5, yPos + 34);
		}
		else if(agent.state == AgentState.BringingFood && agent.currentfood == "pizza") {
			//g.drawImage(imgofpizza, xPos + 10, yPos + 10, 20, 20, gui);
			g.drawString("Pizza", xPos + 5, yPos + 34);
		}
		else if(agent.state == AgentState.BringingFood && agent.currentfood == "burrito") {
			//g.drawImage(imgofburrito, xPos + 10, yPos + 10, 20, 20, gui);
			g.drawString("Burrito", xPos + 5, yPos + 34);
		}
		
	}
	
	public void BringCustomerFromWaitingSpot(int xcoordinate, int ycoordinate)
	{
		xDestination = xcoordinate;
		yDestination = ycoordinate; 
	}

	public void DoBringToTable(Restaurant5CustomerRole customer, int table) {
		xDestination = xcoordinatesoftables[table - 1] + 20;
		yDestination = ycoordinatesoftables[table - 1] - 20;
		xTable = xcoordinatesoftables[table - 1];
		yTable = ycoordinatesoftables[table - 1];
		this.table = table;
		//bringingfood = true;
	}

	public void DoGoToTable( int table) {
		xDestination = xcoordinatesoftables[table - 1] + 20;
		yDestination = ycoordinatesoftables[table - 1] - 20;
		this.table = table;

	}

	public Boolean AtTheTable() {
		if(xDestination == xPos && yDestination == yPos)
		{
			return true;
		}
		return false;
	}

	public void gotocustomerwaitingposition(int x, int y)
	{
		xDestination = x;
		yDestination = y;
	}
	
	public void setHomePosition(int x, int y)
	{
		xhomepos = x;
		yhomepos = y;
		//xPos = xhomepos;
		//xDestination = xhomepos;
		//yPos = yhomepos;
		//yDestination = yhomepos;
	}
	
	public void gotohomeposition() {
		
		System.out.print("go to home position");
		xDestination = xhomepos;
		yDestination = yhomepos;
	}
	
	public void beginhomeposition() {
		xDestination = xhomepos;
		yDestination = yhomepos;
		xPos = xhomepos;
		yPos = yhomepos;
	}

	public boolean isPresent() {
		return true;
	}
	
	public void setOnBreak() {
		onBreak = true;
		agent.GoOnBreak();
		setPresent(true);
	}
	
	public void setOffBreak() {
		onBreak = false;
		agent.ComeBackFromBreak();
		setPresent(true);
	}
	
	
	public Boolean onBreak() {
		return onBreak;
	}
	
	public void setPresent(boolean p) {
		isPresent = p;
	}
	
	public void DoLeaveCustomer() {
		xDestination = 20;
		yDestination = 20;
		returningtolobby = true;
	}
	
	public void GoToKitchen() {
		xDestination = 440;
		yDestination = 70;
	}

	public int getXPos() {
		return xPos;
	}

	public int getYPos() {
		return yPos;
	}

}
