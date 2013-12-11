package city.gui.Bank;
import java.awt.*;

import javax.swing.ImageIcon;

import city.gui.Gui;
import Role.BankManagerRole;

public class BankManagerRoleGui implements Gui {

    private BankManagerRole role = null;
    private int xPos = 20, yPos = 20;//default waiter position
    private int xDestination = 100, yDestination = 200;//default start position

	private BankGui gui;
	private enum Command {noCommand, gotobankmanageroffice, leavebank, arrived};
	private Command command=Command.noCommand;
	public static int xTable = 200;
    public static int yTable = 250;
    public Image imgofbankmanager = new ImageIcon("images/cashier1.png").getImage();
    public boolean bankrobberhere;
    
    public BankManagerRoleGui(BankManagerRole setrole, BankGui gui) {
        this.role = setrole;
        this.gui = gui;
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
        
    	g.drawImage(imgofbankmanager, xPos, yPos + 20, 40, 40, gui);
    	Graphics2D g2 = (Graphics2D)g;
    	Graphics2D g3 = (Graphics2D)g;
    	if(bankrobberhere == true)
    	{
    		g3.setColor(Color.WHITE);
    		g3.fillRoundRect(xPos - 20, yPos - 12, 240, 20, 40, 40);
    		g2.setFont(new Font("Arial", Font.BOLD, 12));
    		g2.setColor(Color.black);
    		g2.drawString("Please Don't hurt anyone take the money", xPos - 17, yPos + 2);	
    	}

    }

    public boolean isPresent() {
        return true;
    }


    public void goToBankManagerOffice(int setxcoordinate, int setycoordinate) {
      
        xDestination = setxcoordinate;
        yDestination = setycoordinate;
        command = Command.gotobankmanageroffice;
        
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

	@Override
	public void setPresent(boolean t) {
		// TODO Auto-generated method stub
		
	}
}
