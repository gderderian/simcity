package city.transportation;

import java.awt.Graphics2D;
import java.util.Vector;
import java.util.concurrent.Semaphore;

import javax.swing.ImageIcon;

import astar.AStarTraversal;
import astar.Position;

import city.gui.AnimationPanel;
import city.gui.Gui;

public class CrashCar implements Gui {

	AStarTraversal aStar; //This is so that the car can find open grid spots
	Position currentPosition;
	Semaphore[][] grid;

	boolean collision = false;
	
	Vector<Gui> targets;
	
	private int xDest;
	private int yDest;
	private int yPos;
	private int xPos;

	boolean isPresent = true;

	ImageIcon movingRight;
	ImageIcon movingLeft;
	ImageIcon movingUp;
	ImageIcon movingDown;
	
	ImageIcon crash1;
	ImageIcon crash2;
	ImageIcon crash3;

	ImageIcon icon;

	AnimationPanel animPanel;

	public CrashCar(AStarTraversal a, Vector<Gui> guis){
		aStar = a;
		grid = a.getGrid();
		
		targets = guis;

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
		
		drive();
	}

	@Override
	public void updatePosition() {		
		if(collision) {
			icon = crash1;
		}

		if (xPos < xDest) {
			xPos++;
			icon = movingRight;
		} else if (xPos > xDest) {
			xPos--;
			icon = movingLeft;
		}

		if (yPos < yDest) {
			yPos++;
			icon = movingDown;
		} else if (yPos > yDest) {
			yPos--;
			icon = movingUp;
		}
		
		if(xPos == xDest && yPos == yDest && !collision) {
			drive();
		}
	}

	private void drive() {
		int x, y;
		
	}

	public void setMainAnimationPanel(AnimationPanel p) {
		animPanel = p;
	}

	public void moveTo(int x, int y) {
		xDest = x;
		yDest = y;
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
