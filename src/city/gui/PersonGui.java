package city.gui;

import java.awt.Color;
import java.awt.Graphics2D;

import javax.swing.ImageIcon;

import city.PersonAgent;
import city.gui.restaurant2.Restaurant2AnimationPanel;

public class PersonGui implements Gui {
	private int xDest;
	private int yDest;
	private int yPos;
	private int xPos;
	
	PersonAgent agent;
	
	ImageIcon icon = new ImageIcon("images/basic_person.png");
	
	boolean moving = false; //Keeps track of whether gui is moving or staying in one place.
	
	AnimationPanel animPanel;
	
	Restaurant2AnimationPanel restaurant2panel;
	
	PersonGui(PersonAgent p){
		agent = p;
		
		xPos = 30;
		yPos = 30;
		xDest = 210;
		yDest = 210;
	}
	
	

	@Override
	public void updatePosition() {
        if (xPos < xDest)
            xPos++;
        else if (xPos > xDest)
            xPos--;

        if (yPos < yDest)
            yPos++;
        else if (yPos > yDest)
            yPos--;
        
        if(xPos == xDest && yPos == yDest && moving) {
        	agent.msgAtDestination();
        	moving = false;
        }
	}
	
	public void addAnimationPanel(Restaurant2AnimationPanel p){
		restaurant2panel = p;
	}
	
	public void setMainAnimationPanel(AnimationPanel p) {
		animPanel = p;
	}
	
	public void moveTo(int x, int y) {
		xDest = x;
		yDest = y;
		
		moving = true;
	}
	
	@Override
	public void draw(Graphics2D g) {
        g.drawImage(icon.getImage(), xPos, yPos, animPanel);
	}

	@Override
	public boolean isPresent() {
		return true;
	}
	
	public void goToRestaurant(int restaurantNum){
		//TODO: finish this function
		
		if(restaurantNum == 2){
			restaurant2panel.addGui(this);
		}
	}
	
	
}

//Person wont show up on screen once I click on the restaurant
