package city.gui.Bank;

import Role.BankCustomerRole;
import Role.BankTellerRole;

import java.awt.*;

import city.Bank;

public class BankTellerRoleGui implements Gui {

    private BankTellerRole role = null;
    private boolean returningtolobby = false;
    private int xPos = -20, yPos = -20;//default waiter position
    private int xDestination = -20, yDestination = -20;//default start position
	private BankGui gui;

    int initialxc = 200;
    private int xcoordinatesofstations[] = new int [4];
	private int ycoordinatesofstations[] = new int [4];

    //Bank bank;

    public BankTellerRoleGui(BankTellerRole setrole, BankGui setgui) {
        this.role = setrole;
        this.gui = setgui;
        
       
		for(int i = 0; i < 4; i++)
		{
			xcoordinatesofstations[i] = initialxc;
			ycoordinatesofstations[i] = 100;
			initialxc += 100;	
		}
            
    }

    public void updatePosition() {
        if (xPos < xDestination)
            xPos++;
        else if (xPos > xDestination)
            xPos--;

        if (yPos < yDestination)
            yPos++;
        else if (yPos > yDestination)
            yPos--;
      
    }

    public void draw(Graphics2D g) {
        g.setColor(Color.MAGENTA);
        g.fillRect(xPos, yPos, 20, 20);
    }

    public boolean isPresent() {
        return true;
    }

    public void goToBankTellerStation(int bankstationnumber) {
    	
    	xDestination = xcoordinatesofstations[bankstationnumber - 1];
		yDestination = ycoordinatesofstations[bankstationnumber - 1];
		//command = Command.GoToSeat;
        
    }

    public void DoLeaveCustomer() {
        xDestination = -20;
        yDestination = -20;
        returningtolobby = true;
    }

    public int getXPos() {
        return xPos;
    }

    public int getYPos() {
        return yPos;
    }

	public void setHomePosition(int i, int j) {
		// TODO Auto-generated method stub
		
	}
}
