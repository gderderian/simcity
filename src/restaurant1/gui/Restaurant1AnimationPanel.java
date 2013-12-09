package restaurant1.gui;

import city.gui.Gui;
import javax.swing.*;

import restaurant1.Restaurant1;
import restaurant1.Restaurant1CashierRole;
import restaurant1.Restaurant1CookRole;
import restaurant1.Restaurant1HostRole;
import restaurant1.Restaurant1WaiterRole;

import city.PersonAgent;
import city.Restaurant2.Restaurant2CashierRole;
import city.Restaurant2.Restaurant2CookRole;
import city.Restaurant2.Restaurant2HostRole;
import city.Restaurant2.Restaurant2WaiterRole;
import city.gui.BuildingPanel;
import city.gui.CityGui;
import city.gui.restaurant2.Restaurant2CookGui;
import city.gui.restaurant2.Restaurant2CustomerGui;
import city.gui.restaurant2.Restaurant2WaiterGui;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Collections;
import java.util.List;
import java.util.ArrayList;

public class Restaurant1AnimationPanel extends BuildingPanel implements ActionListener, MouseListener {

	private static final int WINDOWX = 900;
	private static final int WINDOWY = 750;
	private static final int TIMER_INTERVAL = 15;

	private List<Gui> guis = Collections.synchronizedList(new ArrayList<Gui>());

	Restaurant1 restaurant;

	/*PersonAgent personCook = new PersonAgent("Cook");
    PersonAgent personHost = new PersonAgent("Host");
    PersonAgent personCashier = new PersonAgent("Cashier");
    PersonAgent personWaiter = new PersonAgent("Waiter");
    Restaurant1CookRole Cook;
    Restaurant1CashierRole Cashier;
    Restaurant1WaiterRole Waiter;
    Restaurant1HostRole Host;

    Restaurant1CookGui cookGui;
    Restaurant1CustomerGui customerGui;
    Restaurant1WaiterGui waiterGui;
    Restaurant1CashierGui cashierGui;*/

	private Timer timer;

	//Additions for SimCity    
	public Restaurant1AnimationPanel(Restaurant1 rest) {
		restaurant = rest;

		setSize(WINDOWX, WINDOWY);
		setPreferredSize(new Dimension(WINDOWX, WINDOWY));
		setMaximumSize(new Dimension(WINDOWX, WINDOWY));
		setMinimumSize(new Dimension(WINDOWX, WINDOWY));
		setVisible(true);

		addMouseListener(this);

		timer = new Timer(TIMER_INTERVAL, this);
		timer.start();
	}

	public void actionPerformed(ActionEvent e) {
		repaint();  //Will have paintComponent called
	}

	public void paintComponent(Graphics g) {
		super.paintComponent(g);

		Graphics2D g2 = (Graphics2D)g;
		g2.setColor(new Color(190, 190, 190)); // Background color = gray
		g2.fillRect(0, 0, WINDOWX, WINDOWY );

		// Drawing a pattern on the ground
		g2.setColor(Color.BLACK);
		for(int i = 0; i < 1000; i++) {
			g2.drawLine(20 * i, 0, 0, 20 * i);
		}
		for(int i = -60; i < 60; i++) {
			g2.drawLine(0, 20 * i, 1000, 20 * i + 800);
		}
		// End of floor pattern code

		// Drawing tables at the correct locations
		g2.setColor(new Color(105, 75, 35)); // RGB values for brown
		g2.fillRect(200, 200, 100, 60);
		g2.fillRect(450, 200, 100, 60);
		g2.fillRect(200, 400, 100, 60);
		g2.fillRect(450, 400, 100, 60);

		// Table outlines
		g2.setColor(Color.BLACK);
		g2.drawRect(200, 200, 100, 60);
		g2.drawRect(450, 200, 100, 60);
		g2.drawRect(200, 400, 100, 60);
		g2.drawRect(450, 400, 100, 60);

		// Drawing the cooking area
		g.setColor(Color.GRAY);
		g.fillRect(750, 250, 40, 200);

		g.setColor(Color.BLACK);
		g.fillRect(755, 255, 30, 190);

		// Drawing the food waiting area
		g.setColor(Color.DARK_GRAY);
		g.fillRect(650, 250, 40, 200);

		// Drawing the refrigerator
		g.setColor(Color.WHITE);
		g.fillRect(690, 470, 60, 50);
		g.setColor(Color.BLACK);
		g.drawRect(689, 469, 61, 51);
		g.drawString("Fridge", 700, 495);

		/*
		 * This gets moved to updatePos below so that it will be called even when the panel is not visible
		for(Gui gui : guis) {
            if (gui.isPresent()) {
                gui.updatePosition();
            }
        }
		 */
		for(Gui gui : guis) {
			gui.draw(g2);
		}

		// This draws a rectangular "Restaurant V2" sign
		g2.setColor(Color.BLACK);
		g2.fillRect(250,  5, 250, 40); // Size and position of restaurant sign border
		g2.setColor(Color.WHITE);
		g2.fillRect(252, 7, 246, 36); // Size and position of restaurant sign

		g2.setColor(Color.BLACK);
		g2.setFont(new Font("Verdana", Font.BOLD, 24));
		g2.drawString("  Restaurant 1!", 265, 35); // Position of text on restaurant sign

	}

	public void addGui(Gui gui) {
		guis.add(gui);
	}

	public int getTimerInterval() {
		return TIMER_INTERVAL;
	}

	public void setSpeed(int s) {
		timer.stop();
		timer = new Timer(s, this);
		timer.start();
	}

	public void mouseClicked(MouseEvent arg0) {
		//Nothing		
	}

	public void mouseEntered(MouseEvent arg0) {
		//Nothing		
	}

	public void mouseExited(MouseEvent arg0) {
		//Nothing		
	}

	public void mousePressed(MouseEvent arg0) {
		//Nothing		
	}

	public void mouseReleased(MouseEvent e) {
		//Nothing
	}

	@Override
	public void updatePos() {
		synchronized(guis) {
			for(Gui gui : guis) {
				if (gui.isPresent()) {
					gui.updatePosition();
					gui.updatePosition(); //Speed things up!!!
				}
			}
		}
	}
}
