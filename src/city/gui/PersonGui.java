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
	
	boolean isPresent = true;
	
	ImageIcon up1 = new ImageIcon("images/person_up1.png");
	ImageIcon up2 = new ImageIcon("images/person_up2.png");
	ImageIcon up3 = new ImageIcon("images/person_up3.png");
	ImageIcon down1 = new ImageIcon("images/person_down1.png");
	ImageIcon down2 = new ImageIcon("images/person_down2.png");
	ImageIcon down3 = new ImageIcon("images/person_down3.png");
	ImageIcon flat1 = new ImageIcon("images/person_flat1.png");
	ImageIcon flat2 = new ImageIcon("images/person_flat2.png");
	ImageIcon flat3 = new ImageIcon("images/person_flat3.png");
	
	private int movementCounter = 0;
	private final int iconSwitch = 10; //Rate at which icons switch during movement
	
	ImageIcon icon = new ImageIcon("images/person_up1.png");
	
	boolean moving = false; //Keeps track of whether gui is moving or staying in one place.
	
	AnimationPanel animPanel;
	
	Restaurant2AnimationPanel restaurant2panel;
	
	public PersonGui(PersonAgent p){
		agent = p;
		
		xPos = 730;
		yPos = 680;
		xDest = xPos;
		yDest = yPos;
	}
	
	@Override
	public void updatePosition() {
		movementCounter = (movementCounter + 1) % (4 * iconSwitch);
        if (xPos < xDest) {
            xPos++;
            if(movementCounter < iconSwitch)
        		icon = flat1;
        	else if(movementCounter < iconSwitch * 2)
        		icon = flat2;
        	else if(movementCounter < iconSwitch * 3)
        		icon = flat3;
        	else
        		icon = flat2;
        }
        
        else if (xPos > xDest) {
            xPos--;
        	if(movementCounter < iconSwitch && icon != flat1)
        		icon = flat1;
        	else if(movementCounter < iconSwitch * 2 && icon != flat2)
        		icon = flat2;
        	else if(movementCounter < iconSwitch * 3 && icon != flat3)
        		icon = flat3;
        	else if(icon != flat2)
        		icon = flat2;
        }
        
        if (yPos < yDest) {
            yPos++;
        	if(movementCounter < iconSwitch && icon != down1)
        		icon = down1;
        	else if(movementCounter < iconSwitch * 2 && icon != down2)
        		icon = down2;
        	else if(movementCounter < iconSwitch * 3 && icon != down3)
        		icon = down3;
        	else if(icon != down2)
        		icon = down2;
        }
        else if (yPos > yDest) {
            yPos--;
        	if(movementCounter < iconSwitch && icon != up1)
        		icon = up1;
        	else if(movementCounter < iconSwitch * 2 && icon != up2)
        		icon = up2;
        	else if(movementCounter < iconSwitch * 3 && icon != up3)
        		icon = up3;
        	else if(icon != up2)
        		icon = up2;
        }
        
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
	
	public void draw(Graphics2D g) {
        g.drawImage(icon.getImage(), xPos, yPos, animPanel);
	}

	public boolean isPresent() {
		return isPresent;
	}
	
	public void setInvisible(){
		isPresent = false;
		System.out.println("Setting invisible");
	}
	
	public void setVisible(){
		isPresent = true;
	}
	
	
}