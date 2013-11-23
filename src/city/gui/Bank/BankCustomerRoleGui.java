package city.gui.Bank;

import Role.BankCustomerRole;
import Role.BankManagerRole;

import java.awt.*;

import javax.print.DocFlavor.URL;
import javax.swing.ImageIcon;

import city.Bank;

public class BankCustomerRoleGui implements Gui{

	private BankCustomerRole role = null;
	private boolean isPresent = false;
	private boolean arrivedAtBank = false;

	//private HostAgent host;
	BankGui gui;

	private int xPos, yPos;
	private int xDestination, yDestination;
	private enum Command {noCommand, gotobanktellerstation, leavebank, arrived};
	private Command command=Command.noCommand;
	

	public static final int xTable = 200;
	public static final int yTable = 250;
	private int xhomepos;
	private int yhomepos;
	private int xcoordinatesofstations[] = new int [4];
	private int ycoordinatesofstations[] = new int [4];

	public BankCustomerRoleGui(BankCustomerRole setrole, BankGui gui){ //HostAgent m) {
		this.role = setrole;		

		xPos = 20;
		yPos = 20;
		xDestination = 40;
		yDestination = 40;
		//maitreD = m;
		this.gui = gui;
		int initialxc = 200;
		for(int i = 0; i < 4; i++)
		{
			xcoordinatesofstations[i] = initialxc;
			ycoordinatesofstations[i] = 100;
			initialxc += 100;	
		}

		
	}

	public void updatePosition() 
	{
		
		/*
		if(xPos == -40 && yPos == -40 && agent.goingtocashier == true){
			agent.atLobby.release();
			agent.goingtocashier = false;
			System.out.print("I'm at lobby");
		}
		*/
		//agent.xcoordinate = xPos;
		//agent.ycoordinate = yPos;
		
		
		if (xPos < xDestination)
			xPos++;
		else if (xPos > xDestination)
			xPos--;

		if (yPos < yDestination)
			yPos++;
		else if (yPos > yDestination)
			yPos--;

		if (xPos == xDestination && yPos == yDestination) {

			if (command==Command.gotobanktellerstation) //agent.msgAnimationFinishedGoToSeat();
			{
				role.atBankStation.release();
				command = Command.arrived;
			}
			else if (command==Command.leavebank)
			{
				role.atBankLobby.release();
				command = Command.arrived;
				//agent.msgAnimationFinishedLeaveRestaurant();
				//isHungry = false;
				//gui.setCustomerEnabled(agent);
			}
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

	public void draw(Graphics2D g) {
		g.setColor(Color.BLUE);
		g.fillRect(xPos, yPos, 20, 20);
	}

	public boolean isPresent() {
		return isPresent;
	}
	
	public void setArrivedAtBank() {
		arrivedAtBank = true;
		setPresent(true);
	}
	public boolean isAtBank() {
		return arrivedAtBank;
	}
	
	public void setPresent(boolean p) {
		isPresent = p;
	}

	public void goToBankTellerStation(int bankstationnumber) {//later you will map seatnumber to table coordinates.
		
		xDestination = xcoordinatesofstations[bankstationnumber - 1];
		yDestination = ycoordinatesofstations[bankstationnumber - 1];
		command = Command.gotobanktellerstation;
	}
	
	public void leaveBank() {
		xDestination = -20;
		yDestination = -20;
		command = Command.leavebank;
		
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
	
}
