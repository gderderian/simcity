package city.gui.restaurant2;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JPanel;
import javax.swing.Timer;

import astar.AStarTraversal;
import city.PersonAgent;
import city.Restaurant2.Restaurant2;
import city.Restaurant2.Restaurant2CashierRole;
import city.Restaurant2.Restaurant2CookRole;
import city.Restaurant2.Restaurant2HostRole;
import city.Restaurant2.Restaurant2WaiterRole;
import city.gui.BuildingPanel;
import city.gui.CityGui;
import city.gui.Gui;
import city.gui.PersonGui;

public class Restaurant2AnimationPanel extends BuildingPanel implements MouseListener {
	
	private final int WINDOWX = 900;
    private final int WINDOWY = 750;
    private final int TABLEDIM = 50;
    private final int TABLE1Y = WINDOWY/10;
    private final int TABLEX = WINDOWX/2 - TABLEDIM/2;
    private final int KITCHENX = WINDOWX - 50;	//based off of the refrigerator
    private final int KITCHENY = WINDOWY/2 - 35;	//based off of the refrigerator
    private final int STOVEX = KITCHENX - 75;
    private final int STOVEY = KITCHENY + 60;
    
    Restaurant2 restaurant;
    
    //People for testing
    
    PersonAgent personCook = new PersonAgent("Cook");
    PersonAgent personHost = new PersonAgent("Host");
    PersonAgent personCashier = new PersonAgent("Cashier");
    PersonAgent personWaiter = new PersonAgent("Waiter");
    Restaurant2CookRole Cook;
    Restaurant2CashierRole Cashier;
    Restaurant2WaiterRole Waiter;
    Restaurant2HostRole Host;
    
    //
    Restaurant2CookGui cookGui;
    Restaurant2CustomerGui customerGui;
    Restaurant2WaiterGui waiterGui;
	
	public Restaurant2AnimationPanel(Restaurant2 r){
		System.out.println("Animation panel created");
    	setSize(WINDOWX, WINDOWY);
    	setPreferredSize(new Dimension(WINDOWX, WINDOWY));
    	setMaximumSize(new Dimension(WINDOWX, WINDOWY));
    	setMinimumSize(new Dimension(WINDOWX, WINDOWY));
        setVisible(false);
        
        addMouseListener(this);
        
        restaurant = r;
        
        
        Cook = new Restaurant2CookRole("CookRole", personCook);
        cookGui = new Restaurant2CookGui(Cook);
        Cook.setGui(cookGui);
        Cashier = new Restaurant2CashierRole("CashierRole", personCashier);
        Waiter = new Restaurant2WaiterRole("WaiterRole", personWaiter);
        waiterGui = new Restaurant2WaiterGui(Waiter, personWaiter.getName(), cityGui, 1);
        Waiter.setGui(waiterGui);
        Host = new Restaurant2HostRole("HostRole", personHost);
        
        restaurant.setHost(Host);
        
        personCook.addRole(Cook, true);
        personCook.startThread();
        personHost.addRole(Host, true);
        personHost.startThread();
        personCashier.addRole(Cashier, true);
        personCashier.startThread();
        personWaiter.addRole(Waiter, true);
        personWaiter.startThread();
        Host.addWaiters(Waiter);
        
        guis.add(cookGui);
        guis.add(waiterGui);
        
	}

    private List<Gui> guis = new ArrayList<Gui>();

	public void actionPerformed(ActionEvent e) {
		repaint();  //Will have paintComponent called
	}

    public void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D)g;
        
        System.out.println(guis.size());

        //Clear the screen by painting a rectangle the size of the frame
        g2.setColor(getBackground());
        g2.fillRect(0, 0, getWidth(), getHeight());
        
        //Here is the table
        int tables = 4;
        
        for(int i = 0; i < tables; i++){
            g2.setColor(Color.LIGHT_GRAY);
            if(i < 2){
                g2.fillRect(TABLEX, (2*i+1)*TABLE1Y, TABLEDIM, TABLEDIM);
            }
            else{
                g2.fillRect(TABLEX, (2*i+2)*TABLE1Y, TABLEDIM, TABLEDIM);
            }
        }
        
        //Kitchen refrigerator
        g2.fillRect(KITCHENX, KITCHENY, TABLEDIM/2, TABLEDIM);
        
        //Kitchen stove
        g2.setColor(Color.BLACK);
        g2.fillRect(STOVEX, STOVEY, TABLEDIM*2, TABLEDIM/2);
        g2.setColor(Color.LIGHT_GRAY);
        for(int i = 0; i < 4; i++){
            g2.fillOval(STOVEX + 4 + (i*25), STOVEY + 3, TABLEDIM/3, TABLEDIM/3);
        }
        g2.setColor(Color.BLACK);
        for(int i = 0; i < 4; i++){
            g2.fillOval(STOVEX + 6 + (i*25), STOVEY + 5, TABLEDIM/4, TABLEDIM/4);
        }
        g2.setColor(Color.LIGHT_GRAY);
        for(int i = 0; i < 4; i++){
            g2.fillOval(STOVEX + 8 + (i*25), STOVEY + 7, TABLEDIM/6, TABLEDIM/6);
        }
        
        //Kitchen pickup counter
        g2.setColor(Color.DARK_GRAY);
        g2.fillRect(STOVEX, KITCHENY - 15, TABLEDIM/2, TABLEDIM);
        g2.fillRect(STOVEX, KITCHENY -30, TABLEDIM*2, TABLEDIM/2);
        
        //Kitchen Sign
        g2.drawString("KITCHEN", KITCHENX - 50, KITCHENY - 40);
        
        //BACK BUTTON
        g2.setColor(Color.LIGHT_GRAY);
        g2.fillRect(30, 30, 60, 20);
        g2.setColor(Color.BLACK);
        g2.drawString("CITY VIEW", 30, 45);
        
        
        for(Gui gui : guis) {
            if (gui.isPresent()) {
                gui.updatePosition();
            }
        }

        for(Gui gui : guis) {
            if (gui.isPresent()) {
                gui.draw(g2);
            }
        }
    }
    
    public void addGui(PersonGui gui) {
        guis.add(gui);
        System.out.println("Person added to rest2 gui");
        System.out.println("After add: " + guis.size());
    }
    
    public void addGui(Restaurant2CustomerGui g){
    	guis.add(g);
    }
/*
    public void addGui(Restaurant2WaiterGui gui) {
        guis.add(gui);
    }
    
    public void addGui(Restaurant2CookGui gui){
    	guis.add(gui);
    }

*/
	public void mouseClicked(MouseEvent e) {
		//do nothing
	}

	public void mouseEntered(MouseEvent e) {
		//do nothing
	}

	public void mouseExited(MouseEvent e) {
		//do nothing
	}

	public void mousePressed(MouseEvent e) {
		//do nothing
	}

	public void mouseReleased(MouseEvent e) {
		int x = e.getX();
		int y = e.getY();
		if((x >= 30) && (x <= 90) && (y >= 30) && (y <= 50)){
			changeBackToCity();
		}
	}

}
