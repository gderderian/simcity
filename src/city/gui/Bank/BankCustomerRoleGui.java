package city.gui.Bank;

import Role.BankCustomerRole;
import Role.BankManagerRole;

import java.awt.*;

import javax.print.DocFlavor.URL;
import javax.swing.ImageIcon;

import city.Bank;
import city.gui.Gui;

public class BankCustomerRoleGui implements Gui{

	private BankCustomerRole role = null;
	private boolean isPresent = false;
	private boolean arrivedAtBank = false;

	//private HostAgent host;
	BankGui gui;

	private int xPos, yPos;
	private int xDestination, yDestination;
	public enum Command {noCommand, gotobanktellerstation, leavebank, arrived};
	private Command command=Command.noCommand;
	

	private int xhomepos;
	private int yhomepos;
	private int xcoordinatesofstations[] = new int [4];
	private int ycoordinatesofstations[] = new int [4];
	public Image imgofbankcustomer = new ImageIcon("images/waiter1.png").getImage();
	public Image imgofaccount = new ImageIcon("images/account.png").getImage();
	public Image imgofmoney = new ImageIcon("images/newmoney.png").getImage();
	public boolean openaccount;
	public boolean deposit;
	public boolean withdraw;
	public boolean rewithdraw;
	public boolean loan;
	public boolean money;
	public boolean paybackloan;
	
	ImageIcon flat1 = new ImageIcon("images/person_flat1.png");
	ImageIcon flat2 = new ImageIcon("images/person_flat2.png");
	ImageIcon flat3 = new ImageIcon("images/person_flat3.png");
	
	private int movementCounter = 0;
	private final int iconSwitch = 10; //Rate at which icons switch during movement
	
	ImageIcon icon = flat1;

	
	
	
	
	public BankCustomerRoleGui(BankCustomerRole setrole, BankGui gui){ //HostAgent m) {
		this.role = setrole;		

		xPos = 0;
		yPos = 300;
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
				System.out.print("I'm at the station!");
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
		//g.drawImage(imgofbankcustomer,xPos, yPos + 90, 50, 40, gui);
		
		g.drawImage(icon.getImage(), xPos, yPos + 90, 50, 40, gui);
		Graphics2D g2 = (Graphics2D)g;
		Graphics2D g3 = (Graphics2D)g;
		if(openaccount == true)
		{
		
			g.drawImage(imgofaccount,xPos - 16, yPos + 70, 35, 35, gui);
			//g3.setFont(new Font("Arial", Font.BOLD, 12));
    		//g3.setColor(Color.black);
    		//g3.drawString("OPEN ACCOUNT", xPos - 15, yPos + 80);	
			
		}
		else if(money == true)
		{
			g.drawImage(imgofmoney,xPos - 16, yPos + 85, 35, 35, gui);
				
		}
		else if(deposit == true)
		{
			g.drawImage(imgofmoney,xPos - 16, yPos + 85, 35, 35, gui);
			g3.setColor(Color.WHITE);
    		g3.fillRoundRect(xPos - 22, yPos + 65, 70, 20, 30, 30);	
			g3.setFont(new Font("Arial", Font.BOLD, 12));
			g3.setColor(Color.black);
    		g3.drawString("DEPOSIT", xPos - 15, yPos + 80);	
			//g3.setFont(new Font("Arial", Font.BOLD, 12));
    		//g3.setColor(Color.black);
    		//g3.drawString("DEPOSIT", xPos - 15, yPos + 80);
		}
		
		else if(withdraw == true)
		{
			g3.setColor(Color.WHITE);
    		g3.fillRoundRect(xPos - 22, yPos + 65, 80, 20, 30, 30);	
			g3.setFont(new Font("Arial", Font.BOLD, 12));
			g3.setColor(Color.black);
    		g3.drawString("WITHDRAW?", xPos - 15, yPos + 80);	
		}
		else if(rewithdraw == true)
		{
			g3.setColor(Color.WHITE);
    		g3.fillRoundRect(xPos - 22, yPos + 65, 90, 20, 30, 30);	
			g3.setFont(new Font("Arial", Font.BOLD, 12));
			g3.setColor(Color.black);
    		g3.drawString("REWITHDRAW?", xPos - 15, yPos + 80);	
			
		}
		
		else if(loan == true)
		{
			g3.setColor(Color.WHITE);
    		g3.fillRoundRect(xPos - 22, yPos + 65, 60, 20, 30, 30);	
			g3.setFont(new Font("Arial", Font.BOLD, 12));
			g3.setColor(Color.black);
    		g3.drawString("LOAN?", xPos - 15, yPos + 80);	
			
		}
		else if(paybackloan == true)
		{
			g.drawImage(imgofmoney,xPos - 16, yPos + 70, 35, 35, gui);
			g3.setColor(Color.WHITE);
    		g3.fillRoundRect(xPos - 22, yPos + 65, 100, 20, 30, 30);	
			g3.setFont(new Font("Arial", Font.BOLD, 12));
			g3.setColor(Color.black);
    		g3.drawString("PAYBACKLOAN", xPos - 15, yPos + 80);	
		}
		
		
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
		xDestination = 0;
		yDestination = 300;
		command = Command.leavebank;
		
	}

	
	public void setWaitingPosition(int x, int y)
	{
		//xPos = x;
		//yPos = y;
		xDestination = x;
		yDestination = y;
		//agent.msgSetHomePos(x, y);

	}
	
}
