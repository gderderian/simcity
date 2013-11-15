package city.gui;

import java.awt.Color;
import java.awt.Graphics2D;

import city.gui.restaurant2.Restaurant2AnimationPanel;

public class PersonGui implements Gui{
	private int xDest;
	private int yDest;
	private int yPos;
	private int xPos;
	
	Restaurant2AnimationPanel restaurant2panel;
	
	PersonGui(){
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
	}
	
	public void addAnimationPanel(Restaurant2AnimationPanel p){
		restaurant2panel = p;
	}
	
	@Override
	public void draw(Graphics2D g) {
        g.setColor(Color.ORANGE);
        g.fillRect(xPos, yPos, 20, 20);
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
