package city.gui.Bank;

import Role.BankCustomerRole;
import Role.BankTellerRole;
import city.gui.Gui;

import java.awt.*;

import javax.swing.ImageIcon;

import city.Bank;

public class BankTellerRoleGui implements Gui{

    private BankTellerRole role = null;
    private int xPos = 250, yPos = -20;//default waiter position
    private int xDestination = 0, yDestination = 0;//default start position
	private BankGui gui;

    int initialxc = 200;
    private int xcoordinatesofstations[] = new int [4];
	private int ycoordinatesofstations[] = new int [4];
	
	private enum Command {noCommand, gotobanktellerstation, leavebank, arrived};
	private Command command=Command.noCommand;
	
	//ImageIcon icon = new ImageIcon("images/bankteller.png");
	public Image imgofbankteller = new ImageIcon("images/person_flat1.png").getImage();

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
    	g.drawImage(imgofbankteller, xPos, yPos + 20, 50, 40, gui);
    	//g.setColor(Color.MAGENTA);
        //g.fillRect(xPos, yPos, 20, 20);
    }

    public boolean isPresent() {
        return true;
    }

    public void goToBankTellerStation(int bankstationnumber) {
    	
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
