package city.gui.restaurant2;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.Timer;

import city.Restaurant2.Restaurant2;
import city.gui.BuildingPanel;
import city.gui.Gui;

public class Restaurant2AnimationPanel extends BuildingPanel implements ActionListener{
	
    private static final int TIMER_INTERVAL = 15;
    private Timer timer;
	
	private final int WINDOWX = 900;
    private final int WINDOWY = 750;
    
    ImageIcon background = new ImageIcon("images/restaurant2.png");
    
    Restaurant2 restaurant;
	
	public Restaurant2AnimationPanel(Restaurant2 r){
		System.out.println("Animation panel created");
    	setSize(WINDOWX, WINDOWY);
    	setPreferredSize(new Dimension(WINDOWX, WINDOWY));
    	setMaximumSize(new Dimension(WINDOWX, WINDOWY));
    	setMinimumSize(new Dimension(WINDOWX, WINDOWY));
        setVisible(false);
                
        restaurant = r;
        
        timer = new Timer(TIMER_INTERVAL, this);
        timer.start();
        
	}
	
    private List<Gui> guis = Collections.synchronizedList(new ArrayList<Gui>());

	public void actionPerformed(ActionEvent e) {
		repaint();  //Will have paintComponent called
	}

    public void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D)g;
        
		g.drawImage(background.getImage(), 0, 0, this);
        
        //Clear the screen by painting a rectangle the size of the frame
        //g2.setColor(getBackground());
        //g2.fillRect(0, 0, getWidth(), getHeight());

		/*
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
            g2.fillOval(STOVEX + 4 + (i*25), STOVEY + 3, TABLESPACE/3, TABLESPACE/3);
        }
        g2.setColor(Color.BLACK);
        for(int i = 0; i < 4; i++){
            g2.fillOval(STOVEX + 6 + (i*25), STOVEY + 5, TABLESPACE/4, TABLESPACE/4);
        }
        g2.setColor(Color.LIGHT_GRAY);
        for(int i = 0; i < 4; i++){
            g2.fillOval(STOVEX + 8 + (i*25), STOVEY + 7, TABLESPACE/6, TABLESPACE/6);
        }
        
        //Kitchen pickup counter
        g2.setColor(Color.DARK_GRAY);
        g2.fillRect(STOVEX, KITCHENY - 20, TABLEDIM/2, TABLEDIM);
        g2.fillRect(STOVEX, KITCHENY -30, TABLEDIM*2, TABLEDIM/2);
        
        //Kitchen Sign
        g2.drawString("KITCHEN", KITCHENX - 50, KITCHENY - 40);        
        
        /* Moved to updatePos function below so it will be called when panel is invisible
        for(Gui gui : guis) {
            if (gui.isPresent()) {
                gui.updatePosition();
            }
        }
*/
        for(Gui gui : guis) {
            if (gui.isPresent()) {
                gui.draw(g2);
            }
        }
    }
    
    public void addGui(Gui g){
    	guis.add(g);
    }
    
    public void updatePos(){
		synchronized(guis){
			for(Gui gui : guis) {
				if (gui.isPresent()) {
					gui.updatePosition();
				}
			}
		}
    }

}
