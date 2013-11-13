package city.gui;

import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.Timer;

public class AnimationPanel extends JPanel implements MouseListener, ActionListener {

    private static final int WINDOWX = 1300;
    private static final int WINDOWY = 700;
    private static final int TIMER_INTERVAL = 15;

	ImageIcon background = new ImageIcon("images/background.png");
	
	CityGui cityGui;
    
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
	
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		
		g.drawImage(background.getImage(), 0, 0, this);
		
		Graphics2D g2 = (Graphics2D)g;
		
		g2.setColor(new Color(0, 255, 0)); //Background color = green
		
		g2.fillRect(200, 200, 30, 30);
		
		//g2.fillRect(0, 0, WINDOWX, WINDOWY );
		//g2.fillRect(0, 0, 50, 50);
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

	public void mouseReleased(MouseEvent e) {
		int x = e.getX();
		int y = e.getY();
		if((x >= 200) && (x <= 230) && (y >= 200) && (y <= 230)){
			System.out.println("YAY YOU CLICKED THE SQUARE");
			cityGui.changeView("Restaurant1");
		}
	}
}
