package city.gui;

import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.Timer;

public class AnimationPanel extends JPanel implements ActionListener {

    private static final int WINDOWX = 800;
    private static final int WINDOWY = 800;
    private static final int TIMER_INTERVAL = 15;

	ImageIcon background = new ImageIcon("images/background.png");
    
    private Timer timer;
    
    public AnimationPanel() {
    	setSize(WINDOWX, WINDOWY);
    	setPreferredSize(new Dimension(WINDOWX, WINDOWY));
    	setMaximumSize(new Dimension(WINDOWX, WINDOWY));
    	setMinimumSize(new Dimension(WINDOWX, WINDOWY));
        setVisible(true);
    	
        timer = new Timer(TIMER_INTERVAL, this);
        timer.start();
    }

	public void actionPerformed(ActionEvent e) {
		repaint();  //Will have paintComponent called
	}
	
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		
		g.drawImage(background.getImage(), 0, 0, this);
		
		Graphics2D g2 = (Graphics2D)g;
		g2.setColor(new Color(0, 255, 0)); //Background color = green
		//g2.fillRect(0, 0, WINDOWX, WINDOWY );
		//g2.fillRect(0, 0, 50, 50);
	}
}
