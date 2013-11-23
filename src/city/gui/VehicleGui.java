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
	
	ImageIcon movingRight = new ImageIcon("images/" + type + "_right.png");
	ImageIcon movingLeft = new ImageIcon("images/" + type + "_left.png");
	ImageIcon movingUp = new ImageIcon("images/" + type + "_up.png");
	ImageIcon movingDown = new ImageIcon("images/" + type + "_down.png");
	
	ImageIcon icon;
	
	boolean moving = false; //Keeps track of whether gui is moving or staying in one place.
	
	AnimationPanel animPanel;
	
	Restaurant2AnimationPanel restaurant2panel;
	
	public VehicleGui(Vehicle v, String type){
		this.v = v;
		this.type = type;
		xPos = 730;
		yPos = 720;
		xDest = xPos;
		yDest = yPos;
	}

	@Override
	public void updatePosition() {
        if (xPos < xDest) {
            xPos++;
        	icon = movingRight;
        } else if (xPos > xDest) {
            xPos--;
        	icon = movingLeft;
        }

        if (yPos < yDest) {
            yPos++;
            icon = movingUp;
        } else if (yPos > yDest) {
            yPos--;
            icon = movingDown;
        }
        
        if(xPos == xDest && yPos == yDest && moving) {
        	v.msgGuiFinished();
        	moving = false;
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
		return true;
	}

}
