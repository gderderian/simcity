package city.gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JPanel;
import javax.swing.Timer;

import restaurant1.gui.Restaurant1AnimationPanel;

import city.PersonAgent;
import city.gui.restaurant2.Restaurant2AnimationPanel;

public class AnimationPanel extends JPanel implements MouseListener, ActionListener {

    private static final int WINDOWX = 1300;
    private static final int WINDOWY = 700;
    private static final int TIMER_INTERVAL = 15;

	ImageIcon background = new ImageIcon("images/background.png");	
	//Restaurant1AnimationPanel rest1AnimPanel = new Restaurant1AnimationPanel();
	
	CityGui cityGui;
	private List<Gui> guis = new ArrayList<Gui>();
    private Timer timer;
    
    public AnimationPanel() {
    	setSize(WINDOWX, WINDOWY);
    	setPreferredSize(new Dimension(WINDOWX, WINDOWY));
    	setMaximumSize(new Dimension(WINDOWX, WINDOWY));
    	setMinimumSize(new Dimension(WINDOWX, WINDOWY));
        setVisible(true);
    	
        timer = new Timer(TIMER_INTERVAL, this);
        timer.start();
        
        addMouseListener(this);
    }
    
    public void setCityGui(CityGui c){
    	cityGui = c;
    }

	public void actionPerformed(ActionEvent e) {
		repaint();  //Will have paintComponent called
	}
	
	public void addGui(PersonGui gui) {
		guis.add(gui);
	}
	
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		
		g.drawImage(background.getImage(), 0, 0, this);
		
		Graphics2D g2 = (Graphics2D)g;
		
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
		//System.out.println("Mouse pressed in animation panel");
	}

	//Sample code for clicking on animation panel
	public void mouseReleased(MouseEvent e) {
		int x = e.getX();
		int y = e.getY();
		
		// Restaurants (5)
		if((x >= 720) && (x <= 780) && (y >= 0) && (y <= 60)) {
			cityGui.changeView("Restaurant1");
		} else if((x >= 60) && (x <= 120) && (y >= 120) && (y <= 180)){
			cityGui.changeView("Restaurant2");
		} else if((x >= 390) && (x <= 450) && (y >= 635) && (y <= 695)){
			cityGui.changeView("Restaurant4");
		}
			
		// Markets (3)
		if((x >= 240) && (x <= 300) && (y >= 0) && (y <= 60)) {
			cityGui.changeView("Market1");
		}  else if((x >= 360) && (x <= 420) && (y >= 275) && (y <= 330)){
			cityGui.changeView("Market2");
		}  else if((x >= 780) && (x <= 840) && (y >= 395) && (y <= 455)){
			cityGui.changeView("Market3");
		}
			
		// Banks (2)
		if((x >= 780) && (x <= 850) && (y >= 60) && (y <= 120)){
			cityGui.changeView("Bank1");
		}
		
		// Apartments (2)
		if((x >= 780) && (x <= 850) && (y >= 150) && (y <= 240)){
			cityGui.changeView("Apartment1");
		} else if((x >= 120) && (x <= 210) && (y >= 635) && (y <= 695)){
			cityGui.changeView("Apartment2");
		}
		
		// Houses (26)
		if ((x >= 780) && (x <= 850) && (y >= 570) && (y <= 600)){
			cityGui.changeView(0, 0);
		} else if ((x >= 780) && (x <= 850) && (y >= 510) && (y <= 540)){
			cityGui.changeView(0, 1);
		} else if ((x >= 780) && (x <= 850) && (y >= 450) && (y <= 480)){
			cityGui.changeView(0, 2);
		} else if ((x >= 780) && (x <= 850) && (y >= 370) && (y <= 390)){
			cityGui.changeView(0, 3);
		} else if ((x >= 780) && (x <= 850) && (y >= 240) && (y <= 270)){
			cityGui.changeView(0, 4);
		} else if((x >= 780) && (x <= 850) && (y >= 120) && (y <= 150)){
			cityGui.changeView(0, 5);
		} else if((x >= 690) && (x <= 720) && (y >= 0) && (y <= 60)){
			cityGui.changeView(0, 6);
		} else if((x >= 630) && (x <= 660) && (y >= 0) && (y <= 60)){
			cityGui.changeView(0, 7);
		} else if((x >= 570) && (x <= 600) && (y >= 0) && (y <= 60)){
			cityGui.changeView(0, 8);
		} else if((x >= 510) && (x <= 540) && (y >= 0) && (y <= 60)){
			cityGui.changeView(0, 9);
		} else if((x >= 390) && (x <= 420) && (y >= 0) && (y <= 60)){
			cityGui.changeView(0, 10);
		} else if((x >= 330) && (x <= 360) && (y >= 0) && (y <= 60)){
			cityGui.changeView(0, 11);
		} else if((x >= 210) && (x <= 240) && (y >= 0) && (y <= 60)){
			cityGui.changeView(0, 12);
		} else if((x >= 150) && (x <= 180) && (y >= 0) && (y <= 60)){
			cityGui.changeView(0, 13);
		} else if((x >= 60) && (x <= 120) && (y >= 60) && (y <= 90)){
			cityGui.changeView(0, 14);
		} else if((x >= 60) && (x <= 120) && (y >= 180) && (y <= 210)){
			cityGui.changeView(0, 15);
		} else if((x >= 60) && (x <= 120) && (y >= 240) && (y <= 270)){
			cityGui.changeView(0, 16);
		} else if((x >= 60) && (x <= 120) && (y >= 360) && (y <= 390)){
			cityGui.changeView(0, 17);
		} else if((x >= 60) && (x <= 120) && (y >= 480) && (y <= 510)){
			cityGui.changeView(0, 18);
		} else if((x >= 60) && (x <= 120) && (y >= 540) && (y <= 570)){
			cityGui.changeView(0, 19);
		} else if((x >= 210) && (x <= 240) && (y >= 635) && (y <= 695)){
			cityGui.changeView(0, 20);
		} else if((x >= 270) && (x <= 300) && (y >= 635) && (y <= 695)){
			cityGui.changeView(0, 21);
		} else if((x >= 450) && (x <= 480) && (y >= 635) && (y <= 695)){
			cityGui.changeView(0, 22);
		} else if((x >= 480) && (x <= 510) && (y >= 270) && (y <= 340)){
			cityGui.changeView(0, 23);
		} else if((x >= 420) && (x <= 450) && (y >= 270) && (y <= 340)){
			cityGui.changeView(0, 24);
		} else if((x >= 330) && (x <= 360) && (y >= 270) && (y <= 340)){
			cityGui.changeView(0, 25);
		}
		
	}

	public void addGui(VehicleGui g) {
		guis.add(g);
	}
}
