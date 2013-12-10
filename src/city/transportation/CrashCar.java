package city.transportation;

import java.awt.Graphics2D;

import javax.swing.ImageIcon;

import astar.AStarTraversal;

import city.gui.AnimationPanel;
import city.gui.Gui;

public class CrashCar implements Gui {

	AStarTraversal aStar; //This is so that the car can find open grid spots

	private int xDest;
	private int yDest;
	private int yPos;
	private int xPos;

	boolean isPresent = true;

	ImageIcon movingRight;
	ImageIcon movingLeft;
	ImageIcon movingUp;
	ImageIcon movingDown;

	ImageIcon icon;

	boolean moving = false; //Keeps track of whether gui is moving or staying in one place.

	AnimationPanel animPanel;

	public CrashCar(AStarTraversal a){
		aStar = a;

		xPos = 450;
		yPos = 390;

		xDest = xPos;
		yDest = yPos;

		movingRight = new ImageIcon("images/crash_right.png");
		movingLeft = new ImageIcon("images/crash_left.png");
		movingUp = new ImageIcon("images/crash_up.png");
		movingDown = new ImageIcon("images/crash_down.png");

		setInvisible();

		icon = movingDown;
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
}
