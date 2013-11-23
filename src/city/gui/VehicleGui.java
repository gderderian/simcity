package city.gui;

import java.awt.Graphics2D;

import javax.swing.ImageIcon;

import city.PersonAgent;
import city.gui.restaurant2.Restaurant2AnimationPanel;
import city.transportation.Vehicle;

public class VehicleGui implements Gui {
	String vehicleType;
	
	private int xDest;
	private int yDest;
	private int yPos;
	private int xPos;
	
	Vehicle v;
	
	ImageIcon movingRight = new ImageIcon("images/vehicle_right.png");
	ImageIcon movingLeft = new ImageIcon("images/vehicle_left.png");
	ImageIcon movingUp = new ImageIcon("images/vehicle_up.png");
	ImageIcon movingDown = new ImageIcon("images/vehicle_down.png");
	
	ImageIcon icon;
	
	boolean moving = false; //Keeps track of whether gui is moving or staying in one place.
	
	AnimationPanel animPanel;
	
	Restaurant2AnimationPanel restaurant2panel;
	
	public VehicleGui(Vehicle v){
		this.v = v;
		
		xPos = 730;
		yPos = 720;
		xDest = xPos;
		yDest = yPos;
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
