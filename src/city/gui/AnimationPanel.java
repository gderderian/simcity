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

import city.PersonAgent;
import city.gui.restaurant2.Restaurant2AnimationPanel;

public class AnimationPanel extends JPanel implements MouseListener, ActionListener {

    private static final int WINDOWX = 1300;
    private static final int WINDOWY = 700;
    private static final int TIMER_INTERVAL = 15;

	ImageIcon background = new ImageIcon("images/new_background.png");

	Restaurant2AnimationPanel testRest2AnimPanel = new Restaurant2AnimationPanel();
	
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
		
		//Restaurant 2
		if((x >= 60) && (x <= 120) && (y >= 120) && (y <= 180)){
			System.out.println("This is Restaurant2");
			cityGui.changeView("Restaurant2");
		}
	}
}
