package city.gui.Restaurant5;

import city.gui.Gui;
import city.Restaurant5.*;

import java.awt.*;

import javax.print.DocFlavor.URL;
import javax.swing.ImageIcon;

public class Restaurant5CustomerGui implements Gui{

	private Restaurant5CustomerRole agent = null;
	private boolean isPresent = false;
	private boolean isHungry = false;

	//private HostAgent host;
	Restaurant5Gui gui;

	private int xPos, yPos;
	private int xDestination, yDestination;
	private enum Command {noCommand, GoToSeat, LeaveRestaurant, GoToWashDishes};
	private Command command=Command.noCommand;
	
	 private int xcoordinatesoftables[];
	 private int ycoordinatesoftables[];

	public Image imgofchicken = new ImageIcon("/Users/teryunlee/Documents/restaurant_teryunle/src/restaurant/foodpics/Chicken.jpg").getImage();
	public Image imgofpizza = new ImageIcon("/Users/teryunlee/Documents/restaurant_teryunle/src/restaurant/foodpics/pizza.jpg").getImage();
	public Image imgofburrito = new ImageIcon("/Users/teryunlee/Documents/restaurant_teryunle/src/restaurant/foodpics/burrito.jpeg").getImage();
	public Image imgofquestionmark = new ImageIcon("/Users/teryunlee/Documents/restaurant_teryunle/src/restaurant/foodpics/questionmark.jpg").getImage();
	public Image imgofexclamationmark = new ImageIcon("/Users/teryunlee/Documents/restaurant_teryunle/src/restaurant/foodpics/exclamation.png").getImage();
	public static final int xTable = 200;
	public static final int yTable = 250;
	private int xhomepos;
	private int yhomepos;

	ImageIcon flat1 = new ImageIcon("images/person_flat1.png");
	ImageIcon flat2 = new ImageIcon("images/person_flat2.png");
	ImageIcon flat3 = new ImageIcon("images/person_flat3.png");
	
	private int movementCounter = 0;
	private final int iconSwitch = 10; //Rate at which icons switch during movement
	
	ImageIcon icon = flat1;
	
	
	
	
	
	
	public Restaurant5CustomerGui(Restaurant5CustomerRole c, Restaurant5Gui gui, Restaurant5HostRole host){ //HostAgent m) {
		agent = c;
		
		//current position was -40 -40
		xPos = -20;
		yPos = -20;
		xDestination = -20;
		yDestination = -20;
		//maitreD = m;
		this.gui = gui;
		xcoordinatesoftables = host.getxcoordinatesTables();
        ycoordinatesoftables = host.getycoordinatesTables();
		
		
		
	}


	public void updatePosition() {
		
		/*
		if(xPos == -40 && yPos == -40 && agent.goingtocashier == true){
			agent.atLobby.release();
			agent.goingtocashier = false;
			System.out.print("I'm at lobby");
		}
		*/
		//agent.xcoordinate = xPos;
		//agent.ycoordinate = yPos;
		
		
		//System.out.println("This is the customer coordinates x: " + xPos + " y: " + yPos);
		movementCounter = (movementCounter + 1) % (4 * iconSwitch);
		

		
		if(xPos != xDestination || yPos != yDestination) {
            if(movementCounter < iconSwitch)
        		icon = flat1;
        	else if(movementCounter < iconSwitch * 2)
        		icon = flat2;
        	else if(movementCounter < iconSwitch * 3)
        		icon = flat3;
        	else
        		icon = flat2;
		} else icon = flat2;
		
		
		
		
		
		
		
		if(xPos == -20 && yPos == -20) {
			agent.atLobby.release();
		}
		
		if(xPos == 100 && yPos == 300)
		{
			agent.atWashingDishes.release();
			
		}
		if (xPos < xDestination)
			xPos++;
		else if (xPos > xDestination)
			xPos--;

		if (yPos < yDestination)
			yPos++;
		else if (yPos > yDestination)
			yPos--;

		if (xPos == xDestination && yPos == yDestination) {
			if (command==Command.GoToSeat) 
				agent.msgAnimationFinishedGoToSeat();
			else if (command==Command.LeaveRestaurant) {
				agent.msgAnimationFinishedLeaveRestaurant();
				isHungry = false;
				gui.setCustomerEnabled(agent);
			}
			command=Command.noCommand;
		}
	}

	public void draw(Graphics2D g) {
		//g.setColor(Color.BLUE);
		//g.fillRect(xPos, yPos, 20, 20);
		
		g.drawImage(icon.getImage(), xPos, yPos, 30, 30, null);
		
		g.setColor(Color.BLUE);
		if(agent.eating == true && agent.choice == "chicken") {
			//g.drawImage(imgofchicken, xPos, yPos + 20, 20, 20, gui);
			g.drawString("Eating..", xPos, yPos - 10);
			g.drawString("Chicken", xPos, yPos + 34);	
		}
		else if(agent.eating == true && agent.choice == "pizza") {
			//g.drawImage(imgofpizza, xPos, yPos + 20, 20, 20, gui);
			g.drawString("Eating..", xPos, yPos - 10);
			g.drawString("Pizza", xPos, yPos + 34);
		}
		else if(agent.eating == true && agent.choice == "burrito") {
			//g.drawImage(imgofburrito, xPos, yPos + 20, 20, 20, gui);
			g.drawString("Eating..", xPos, yPos - 10);
			g.drawString("Burrito", xPos, yPos + 34);
		}
		else if(agent.readytoorder == true) {
			//g.drawImage(imgofexclamationmark, xPos , yPos -20, 20, 20, gui);
			g.drawString("Ready!", xPos, yPos - 10);
		}
		else if(agent.readyforcheck == true) {
			g.drawString("Check!", xPos, yPos - 10);
		}
		
		else if(agent.ordered == true && agent.choice == "chicken") {
			g.drawString("Chicken", xPos, yPos - 10);
			//g.drawImage(imgofchicken, xPos + 20, yPos -20, 20, 20, gui);
			//g.drawImage(imgofquestionmark, xPos , yPos -20, 20, 20, gui);
		}
		else if(agent.ordered == true && agent.choice == "pizza") {
			g.drawString("Pizza", xPos, yPos - 10);
			//g.drawImage(imgofpizza, xPos + 20, yPos -20, 20, 20, gui);
			//g.drawImage(imgofquestionmark, xPos , yPos -20, 20, 20, gui);
		}
		else if(agent.ordered == true && agent.choice == "burrito") {
			g.drawString("Burrito", xPos, yPos - 10);
			//g.drawImage(imgofburrito, xPos + 20, yPos -20, 20, 20, gui);
			//g.drawImage(imgofquestionmark, xPos, yPos -20, 20, 20, gui);
		}
		
		
	}

	public boolean isPresent() {
		return isPresent;
	}
	public void setHungry() {
		isHungry = true;
		agent.gotHungry();
		setPresent(true);
	}
	public boolean isHungry() {
		return isHungry;
	}

	public void setPresent(boolean p) {
		isPresent = p;
	}

	public void DoGoToSeat(int seatnumber, int table) {//later you will map seatnumber to table coordinates.
		xDestination = xcoordinatesoftables[table - 1];
		yDestination = ycoordinatesoftables[table - 1];
		//System.out.println("XDestination: " + xDestination);
		//System.out.println("yDestination: " + yDestination);
		command = Command.GoToSeat;
	}
	
	public void DoGoToWait(int xcoordinateofwaitingspot, int ycoordinateofwaitingspot) {
		xDestination = xcoordinateofwaitingspot;
		yDestination = ycoordinateofwaitingspot;
		
	}
	
	public void DoGoToWashDishes(int x, int y) {
		xDestination = x;
		yDestination = y;
		command = Command.GoToWashDishes;
	}
	
	public void gotohomeposition() {
		xDestination = xhomepos;
		yDestination = yhomepos;
	}
	
	public void setHomePosition(int x, int y)
	{
		xhomepos = x;
		yhomepos = y;
		agent.msgSetHomePos(x, y);
	}
	

	public void DoExitRestaurant() {
		xDestination = -20;
		yDestination = -20;
		command = Command.LeaveRestaurant;
	}
}
