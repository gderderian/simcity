package city.gui.House;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JPanel;
import javax.swing.Timer;

import city.gui.BuildingPanel;
import city.gui.CityGui;
import city.gui.Gui;
import city.gui.PersonGui;

public class HouseAnimationPanel extends BuildingPanel implements ActionListener, MouseListener {
    private static final int WINDOWX = 900;
    private static final int WINDOWY = 900;
    private final int TIMER = 8;
    
    private List<Gui> guis = new ArrayList<Gui>();
    
    //Functional things
    public Image bed = new ImageIcon("images/bed.png").getImage();
    public Image lamp = new ImageIcon("images/lamp.png").getImage();
    public Image table = new ImageIcon("images/houseTable.png").getImage();
    public Image stove = new ImageIcon("images/stove.png").getImage();
    public Image oven = new ImageIcon("images/oven.png").getImage();
    public Image fridge = new ImageIcon("images/fridge.png").getImage();
    public Image microwave = new ImageIcon("images/microwave.png").getImage();
    
    //Christmas things
    public Image tree = new ImageIcon("images/christmasTree.png").getImage();
    public Image poinsettia = new ImageIcon("images/poinsettia.png").getImage();
    public Image santaHat = new ImageIcon("images/santaHat.png").getImage();
    
    
    public HouseAnimationPanel() {
        setSize(WINDOWX, WINDOWY);
        setPreferredSize(new Dimension(WINDOWX, WINDOWY));
        setMaximumSize(new Dimension(WINDOWX, WINDOWY));
        setMinimumSize(new Dimension(WINDOWX, WINDOWY));
        setVisible(true);
        
        addMouseListener(this);
        
        Timer timer = new Timer(TIMER, this );
    	timer.start();
    }
    
    public void actionPerformed(ActionEvent e) {
                repaint();
    }
    
    public void paintComponent(Graphics g) {         
        Graphics2D g2 = (Graphics2D)g;
                
        //Clear the screen by painting a rectangle the size of the frame
        g2.setColor(getBackground());
        g2.fillRect(0, 0, WINDOWX, WINDOWY);
        
        
        //This is the kitchen table
        g.drawImage(table, 500, 200, 75, 75, this);
        //This is the fridge
        g.drawImage(fridge, 450, 0, 75, 80, this);
        //This is the stove
        g.drawImage(stove, 525, 25, 55, 55, this);
        //This is the oven
        g.drawImage(oven, 580, 25, 55, 55, this);
        //This is the microwave
        g.drawImage(microwave, 635, 25, 55, 55, this);
        //This is the bed
        g.drawImage(bed,100, 500, 180, 140, this);
        //This is the lamp
        g.drawImage(lamp, 50, 575, 50, 50, this);
        
        
        //This is the Christmas tree
        g.drawImage(tree, 600, 480, 175, 225, this);
        //This is the poinsettia
        g.drawImage(poinsettia, 525, 210, 25, 25, this);
        //This is the santa hat
        g.drawImage(santaHat, 100, 510, 30, 30, this);

        for(Gui gui : guis) {
            if (gui.isPresent()) {
                gui.draw(g2);
            }
        }
                
        }
    
    public void addGui(Gui gui) {
        guis.add(gui);
    }
    
    public void notInHouse(Gui gui){
    	guis.remove(gui);
    }
    
        @Override
        public void mouseClicked(MouseEvent e) {
                // TODO Auto-generated method stub
                
        }

        @Override
        public void mousePressed(MouseEvent e) {
                // TODO Auto-generated method stub
                
        }

        @Override
        public void mouseReleased(MouseEvent e) {
                int x = e.getX();
                int y = e.getY();
                
        }

        @Override
        public void mouseEntered(MouseEvent e) {
                // TODO Auto-generated method stub
                
        }

        @Override
        public void mouseExited(MouseEvent e) {
                // TODO Auto-generated method stub
                
        }

		@Override
		public void updatePos() {
			for(Gui gui : guis) {
	            if (gui.isPresent()) {
	                gui.updatePosition();
	            }
	        }
		}

}