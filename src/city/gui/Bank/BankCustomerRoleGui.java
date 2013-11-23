package city.gui.Bank;

import Role.BankCustomerRole;
import Role.BankManagerRole;

import java.awt.*;

import javax.print.DocFlavor.URL;
import javax.swing.ImageIcon;

public class BankCustomerRoleGui implements Gui{

	private BankCustomerRole role = null;
	private boolean isPresent = false;
	private boolean isHungry = false;

	//private HostAgent host;
	BankGui gui;

	private int xPos, yPos;
	private int xDestination, yDestination;
	private enum Command {noCommand, GoToSeat, LeaveRestaurant, GoToWashDishes};
	private Command command=Command.noCommand;
	

	public static final int xTable = 200;
	public static final int yTable = 250;
	private int xhomepos;
	private int yhomepos;

	public BankCustomerRoleGui(BankCustomerRole setrole, BankGui gui){ //HostAgent m) {
		this.role = setrole;

		
		//current position was -40 -40

		xPos = -20;
		yPos = -20;
		xDestination = -20;
		yDestination = -20;
		//maitreD = m;
		this.gui = gui;

		
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
		
		
		if(xPos == -20 && yPos == -20) {

			//agent.atLobby.release();

		}
		
		if(xPos == 100 && yPos == 300)
		{

			//agent.atWashingDishes.release();

			
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

			if (command==Command.GoToSeat) //agent.msgAnimationFinishedGoToSeat();
			{
				
			}
			else if (command==Command.LeaveRestaurant) {
				//agent.msgAnimationFinishedLeaveRestaurant();
				//isHungry = false;
				//gui.setCustomerEnabled(agent);

			/*
			if (command==Command.GoToSeat) //agent.msgAnimationFinishedGoToSeat();
			else if (command==Command.LeaveRestaurant) {
				//agent.msgAnimationFinishedLeaveRestaurant();
				isHungry = false;
				gui.setCustomerEnabled(agent);

			}
			command=Command.noCommand;
			*/
			}
		}
	}

	public void draw(Graphics2D g) {
		g.setColor(Color.BLUE);
		g.fillRect(xPos, yPos, 20, 20);
		

		
	}

	public boolean isPresent() {
		return isPresent;
	}
	public void setHungry() {
		isHungry = true;

		//agent.gotHungry();

		setPresent(true);
	}
	public boolean isHungry() {
		return isHungry;
	}

	public void setPresent(boolean p) {
		isPresent = p;
	}

	public void DoGoToSeat(int seatnumber, int table) {//later you will map seatnumber to table coordinates.

		//xDestination = xcoordinatesoftables[table - 1];
		//yDestination = ycoordinatesoftables[table - 1];

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

		//agent.msgSetHomePos(x, y);

	}
	

	public void DoExitRestaurant() {
		xDestination = -20;
		yDestination = -20;
		command = Command.LeaveRestaurant;
	}
}
