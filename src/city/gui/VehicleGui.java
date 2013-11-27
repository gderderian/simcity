package city.gui;

import java.awt.Graphics2D;

import javax.swing.ImageIcon;

import city.PersonAgent;
import city.gui.restaurant2.Restaurant2AnimationPanel;
import city.transportation.Vehicle;

public class VehicleGui implements Gui {
	String type;
	
	private int xDest;
	private int yDest;
	private int yPos;
	private int xPos;
	
	Vehicle v;
	
	boolean isPresent = true;
	
	ImageIcon movingRight;
	ImageIcon movingLeft;
	ImageIcon movingUp;
	ImageIcon movingDown;
	
	ImageIcon icon;
	
	boolean moving = false; //Keeps track of whether gui is moving or staying in one place.
	
	AnimationPanel animPanel;
	
	public VehicleGui(Vehicle v){
		this.v = v;
		this.type = v.getType();
		xPos = 660;
		yPos = 660;
		xDest = xPos;
		yDest = yPos;
		
		movingRight = new ImageIcon("images/" + type + "_right.png");
		movingLeft = new ImageIcon("images/" + type + "_left.png");
		movingUp = new ImageIcon("images/" + type + "_up.png");
		movingDown = new ImageIcon("images/" + type + "_down.png");
		
		icon = movingUp;
	}

	@Override
	public void updatePosition() {
        if(xPos == xDest && yPos == yDest && moving) {
        	v.msgGuiFinished();
        	moving = false;
        	return;
        }
        
        if (xPos < xDest) {
            xPos++;
            moving = true;
        	icon = movingRight;
        } else if (xPos > xDest) {
            xPos--;
            moving = true;
        	icon = movingLeft;
        }

        if (yPos < yDest) {
            yPos++;
            moving = true;
            icon = movingDown;
        } else if (yPos > yDest) {
            yPos--;
            moving = true;
            icon = movingUp;
        }
	}
	
	public void setMainAnimationPanel(AnimationPanel p) {
		animPanel = p;
	}
	
	public void moveTo(int x, int y) {
		xDest = x;
		yDest = y;
		
		moving = true;
	}
	
	public void draw(Graphics2D g) {
        g.drawImage(icon.getImage(), xPos, yPos, animPanel);
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

}
