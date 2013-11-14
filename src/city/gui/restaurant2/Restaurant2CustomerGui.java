package city.gui.restaurant2;

import hollytesting.interfaces.Restaurant2Customer;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.util.HashMap;
import java.util.Map;

import city.Restaurant2.Restaurant2CustomerRole;
import city.gui.CityGui;
import city.gui.Gui;

public class Restaurant2CustomerGui implements Gui{
	
	private Restaurant2CustomerRole agent = null;
	private boolean isPresent = false;
	private boolean isHungry = false;
	private boolean doneEating = false;

	//private HostAgent host;
	CityGui gui;

	private int xPos, yPos;
	private int xDestination, yDestination;
	private final int HOMEX, HOMEY;
	private enum Command {noCommand, GoToSeat, goToCashier, LeaveRestaurant, goToWaiter};
	private Command command=Command.noCommand;
	
	private String name;
	private String foodChoice;
	
	int customerNum;

    private final int WINDOWX = 600;
    private final int WINDOWY = 600 - 20;
    private final int TABLEDIM = 50;
    private final int TABLE1Y = WINDOWY/10;
    private final int TABLEX = WINDOWX/2 - TABLEDIM/2;
	public Map<Integer, Integer> TABLEY = new HashMap<Integer, Integer>();
	
    private final int KITCHENY = WINDOWY/2 - 20;		//goes to the counter of the kitchen
    private final int KITCHENX = WINDOWX - 145;
	
	private enum State {notOrdered, foodOrdered, HasFood, finishedFood, foodGone, leaving};
	private State state = State.notOrdered;
	
	boolean foodDone = false;

	public Restaurant2CustomerGui(Restaurant2CustomerRole c, CityGui gui, String n, int i){
		agent = c;
		xPos = 0;
		yPos = WINDOWY/2;
		this.gui = gui;
		
		name = n;
		
		customerNum = i;
		
		TABLEY.put(1, TABLE1Y);
		TABLEY.put(2, TABLE1Y + 2*TABLE1Y);
		TABLEY.put(3, TABLE1Y + 5*TABLE1Y);
		TABLEY.put(4, TABLE1Y + 7*TABLE1Y);
		
		
        if(customerNum % 4 == 0) HOMEY = WINDOWY/2 + 140;
        else if(customerNum % 4 == 3) HOMEY = WINDOWY/2 + 100;
        else if(customerNum % 4 == 2) HOMEY = WINDOWY/2 + 60;
        else if(customerNum % 4 == 1) HOMEY = WINDOWY/2 + 20;
        else HOMEY = 0;
        
        if(customerNum < 5) HOMEX = 30;
        else if(customerNum < 9) HOMEX = 60;
        else if(customerNum < 13) HOMEX = 90;
        else if(customerNum < 17) HOMEX = 120;
        else HOMEX = 0;
        
        xDestination = HOMEX;
        yDestination = HOMEY;
		
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

		if (xPos == xDestination && yPos == yDestination) {
			if(command == Command.goToWaiter){
				agent.msgAtDestination();
			}
			if (command==Command.GoToSeat) agent.msgAnimationFinishedGoToSeat();
			else if (command==Command.LeaveRestaurant) {
				agent.msgAnimationFinishedLeaveRestaurant();
				System.out.println("about to call gui.setCustomerEnabled(agent);");
				isHungry = false;
				//gui.setEnabled(agent);		TODO: FIX THIS!
			}
			else if(command == Command.goToCashier){
				agent.msgAtDestination();			
			}
			command=Command.noCommand;
		}
	}

	public void draw(Graphics2D g) {
		g.setColor(Color.GREEN);
		g.fillRect(xPos, yPos, 20, 20);
		g.setColor(Color.BLACK);
		g.setFont(new Font("TimesRoman", Font.PLAIN, 16));
		g.drawString(name, xPos, yPos);
		if(state == State.foodOrdered){
			g.setColor(Color.WHITE);
			g.fillOval(xPos+20, yPos, 16, 16);
			g.setColor(Color.BLACK);
			g.drawString(foodChoice + "?", xPos+20, yPos+26);
			
		}
		if(state == State.HasFood){
			g.setColor(Color.WHITE);
			g.fillOval(xPos+20, yPos, 16, 16);
			g.setColor(Color.BLACK);
			g.fillOval(xPos+20, yPos, 10, 10);
			g.setColor(Color.BLACK);
			g.drawString(foodChoice, xPos+20, yPos+26);
		}
		if(state == State.finishedFood){
			g.setColor(Color.WHITE);
			g.fillOval(xPos+20, yPos, 16, 16);
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

	public void DoGoToWaiter(int waiternum){
		xDestination = 20;
		yDestination = 30*waiternum;
		command = Command.goToWaiter;
	}
	
	public void DoGoToSeat(int seatnumber) {//later you will map seatnumber to table coordinates.
		xDestination = TABLEX;
		yDestination = TABLEY.get(seatnumber);
		command = Command.GoToSeat;
	}

	public void DoExitRestaurant() {
		xDestination = -40;
		yDestination = -40;
		command = Command.LeaveRestaurant;
		state = State.leaving;
	}
	
	public void DoGoToCashier(){
		state = State.foodGone;
		command = Command.goToCashier;
		xDestination = 30;
		yDestination = 30;
	}
	
	public void setFoodOrdered(String choice){
		foodChoice = choice;
		state = State.foodOrdered;
	}
	
	public void setHasFood(){
		state = State.HasFood;
	}
	
	public void setDoneEating(){
		state = State.finishedFood;
	}
	
	//TODO: FIX THIS
	public void setEnabled(){
		//gui.setEnabled(agent);
	}
	


}
