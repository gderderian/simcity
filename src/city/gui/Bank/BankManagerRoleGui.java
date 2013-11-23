package city.gui.Bank;




import java.awt.*;

import Role.BankManagerRole;

public class BankManagerRoleGui implements Gui {


    private BankManagerRole role = null;

    private BankManagerRole agent = null;

    private boolean returningtolobby = false;

    private int xPos = -20, yPos = -20;//default waiter position
    private int xDestination = -20, yDestination = -20;//default start position

	private BankGui gui;

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


    public void DoBringToTable(int tablexcoordinate, int tableycoordinate) {
      
        xDestination = tablexcoordinate + 20;
        yDestination = tableycoordinate - 20;
        xTable = tablexcoordinate;
        yTable = tableycoordinate;
        
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
}
