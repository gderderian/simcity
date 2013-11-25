package city.gui.Bank;




import java.awt.*;


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
        g.setColor(Color.MAGENTA);
        g.fillRect(xPos, yPos, 20, 20);
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
}
