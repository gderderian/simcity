package city.gui.Bank;

import Role.BankCustomerRole;
import Role.BankTellerRole;
import city.gui.Gui;
import city.gui.Bank.BankCustomerRoleGui.Command;

import java.awt.*;

import javax.swing.ImageIcon;

import city.Bank;

public class BankTellerRoleGui implements Gui{

    private BankTellerRole role = null;
    private int xPos = 250, yPos = -20;//default waiter position
    private int xDestination = 0, yDestination = 0;//default start position
	private BankGui gui;
	
	boolean isPresent;
	public boolean bankTellerOccupied;
	public boolean approved;
	public boolean denied;

    int initialxc = 200;
    private int xcoordinatesofstations[] = new int [4];
	private int ycoordinatesofstations[] = new int [4];
	
	private enum Command {noCommand, gotobanktellerstation, leavebank, arrived};
	private Command command=Command.noCommand;

	//ImageIcon icon = new ImageIcon("images/bankteller.png");
	public Image imgofbankteller = new ImageIcon("images/banktellericon.png").getImage();

	ImageIcon icon1 = new ImageIcon("images/waiter1.png");
	ImageIcon icon2 = new ImageIcon("images/waiter2.png");
	ImageIcon icon3 = new ImageIcon("images/waiter3.png");
	
	private int movementCounter = 0;
	private final int iconSwitch = 10; //Rate at which icons switch during movement
	
	ImageIcon icon = icon1;
	
	private boolean moving = false;
	
	private int cycleCount = 0;
	
	
	
	
	
    public BankTellerRoleGui(BankTellerRole setrole, BankGui setgui) {
        this.role = setrole;
        this.gui = setgui;
        bankTellerOccupied = false;
        
       
		for(int i = 0; i < 4; i++)
		{
			xcoordinatesofstations[i] = initialxc;
			ycoordinatesofstations[i] = 100;
			initialxc += 100;	
		}
            
    }

    public void updatePosition() {
    	
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
    	if (xPos == xDestination && yPos == yDestination) {

			if (command==Command.gotobanktellerstation) //agent.msgAnimationFinishedGoToSeat();
			{
				role.atBankStation.release();
				command = Command.arrived;
				System.out.print("I'm at the station!");
			}
    	}
        
        
    }

    public void draw(Graphics2D g) {
    	
    	if(command != Command.arrived)
    	g.drawImage(icon.getImage(), xPos, yPos + 13, 50, 40, null);
    	if(command == Command.arrived)
    	g.drawImage(imgofbankteller, xPos, yPos + 13, 50, 40, gui);
    	
    	Graphics2D g3 = (Graphics2D)g;
    	Graphics2D g4 = (Graphics2D)g;
    	if(bankTellerOccupied == false)
    	{
    		g3.setColor(Color.GREEN);
    		g3.fillRect(xPos - 20, yPos - 12, 38, 20);
    		g4.setFont(new Font("Arial", Font.BOLD, 12));
    		g4.setColor(Color.black);
    		g4.drawString("OPEN", xPos - 17, yPos + 2);
    	}
    	
    	if(approved == true)
    	{
    		g3.setColor(Color.GREEN);
    		g3.fillRect(xPos - 20, yPos - 12, 70, 20);
    		g4.setFont(new Font("Arial", Font.BOLD, 12));
    		g4.setColor(Color.black);
    		g4.drawString("APPROVED", xPos - 17, yPos + 2);
    	}
    	
    	if(denied == true)
    	{
    		g3.setColor(Color.RED);
    		g3.fillRect(xPos - 20, yPos - 12, 44, 20);
    		g4.setFont(new Font("Arial", Font.BOLD, 12));
    		g4.setColor(Color.black);
    		g4.drawString("DENIED", xPos - 17, yPos + 2);
    			
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

    public void goToBankTellerStation(int bankstationnumber) {
    	isPresent = true;
    	xDestination = xcoordinatesofstations[bankstationnumber - 1];
		yDestination = ycoordinatesofstations[bankstationnumber - 1];
		command = Command.gotobanktellerstation;
        
    }
    
    

    public void leaveBank() {
        xDestination = -20;
        yDestination = -20;
        command = Command.leavebank;
    }

    public int getXPos() {
        return xPos;
    }

    public int getYPos() {
        return yPos;
    }

	public void setHomePosition(int i, int j) {
		
		
	}
}
