package city.gui;

import java.awt.Graphics2D;

import javax.swing.ImageIcon;

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

	boolean crashed = false;
	ImageIcon crashed1 = new ImageIcon("images/crashed1.png");
	ImageIcon crashed2 = new ImageIcon("images/crashed2.png");
	ImageIcon crashed3 = new ImageIcon("images/crashed3.png");
	private int movementCounter = 0;
	private final int iconSwitch = 10; //Rate at which icons switch during movement

	ImageIcon icon;

	boolean moving = false; //Keeps track of whether gui is moving or staying in one place.

	AnimationPanel animPanel;

	public VehicleGui(Vehicle v){
		this.v = v;
		this.type = v.getType();

		if(type.equals("car")) {
			xPos = 450;
			yPos = 390;
		} else if(type.equals("truck")) {
			xPos = 420;
			yPos = 270;
		} else if(type.equals("crash1")) {
			xPos = 600;
			yPos = 660;
		} else if(type.equals("crash2") || type.equals("crash3")) {
			xPos = 870;
			yPos = 450;
		} else {
			xPos = 630;
			yPos = 660;
		}

		xDest = xPos;
		yDest = yPos;

		movingRight = new ImageIcon("images/" + type + "_right.png");
		movingLeft = new ImageIcon("images/" + type + "_left.png");
		movingUp = new ImageIcon("images/" + type + "_up.png");
		movingDown = new ImageIcon("images/" + type + "_down.png");

		if(type.contains("crash")) {
			movingRight = new ImageIcon("images/crash_right.png");
			movingLeft = new ImageIcon("images/crash_left.png");
			movingUp = new ImageIcon("images/crash_up.png");
			movingDown = new ImageIcon("images/crash_down.png");
		}

		if(type.equals("car") || type.equals("truck")) {
			setInvisible();
		}
		icon = movingUp;
		
		if(type.equals("crash1"))
			movementCounter+=20; //To offset animation of crashed cars.
	}

	@Override
	public void updatePosition() {
		if(xPos == xDest && yPos == yDest && moving) {
			v.msgGuiFinished();
			moving = false;
			return;
		}

		if(crashed) {
			movementCounter = (movementCounter + 1) % (3 * iconSwitch);
			if(movementCounter < iconSwitch)
				icon = crashed1;
			else if(movementCounter < iconSwitch * 2)
				icon = crashed2;
			else if(movementCounter < iconSwitch * 3)
				icon = crashed3;
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

	public void setInvisible(){
		isPresent = false;
		//System.out.println("Setting invisible");
	}

	public void setVisible(){
		isPresent = true;
	}

	public void setPresent(boolean t) {
		if(t)
			isPresent = true;
		else
			isPresent = false;
	}

	public void teleport(int x, int y) {
		xPos = x;
		yPos = y;
		xDest = x;
		yDest = y;

	}

	public int getX() {
		return xPos;
	}

	public int getY() {
		return yPos;
	}

	public void crashed() {
		crashed = true;
	}
}
