package city.gui.House;

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

import javax.swing.JPanel;
import javax.swing.Timer;

import city.gui.BuildingPanel;
import city.gui.CityGui;
import city.gui.Gui;
import city.gui.PersonGui;

public class HouseAnimationPanel extends BuildingPanel implements ActionListener, MouseListener {
    private static final int WINDOWX = 900;
    private static final int WINDOWY = 700;
    
    private List<Gui> guis = new ArrayList<Gui>();
    
    public HouseAnimationPanel() {
        setSize(WINDOWX, WINDOWY);
        setPreferredSize(new Dimension(WINDOWX, WINDOWY));
        setMaximumSize(new Dimension(WINDOWX, WINDOWY));
        setMinimumSize(new Dimension(WINDOWX, WINDOWY));
        setVisible(true);
        
        addMouseListener(this);
    }
    
    public void actionPerformed(ActionEvent e) {
                repaint();
    }
    
    public void paintComponent(Graphics g) {         
        Graphics2D g2 = (Graphics2D)g;
                
        //Clear the screen by painting a rectangle the size of the frame
        g2.setColor(getBackground());
        g2.fillRect(0, 0, WINDOWX, WINDOWY);
                
        
        //This is the exit to the city
        g2.setColor(Color.CYAN);
        g2.fillRect(75, 0, 100, 30);
        g2.setColor(Color.BLACK);
        g2.drawString("Exit to City", 90, 15);
        
        
        //This is the kitchen table
        g2.setColor(Color.ORANGE);
        g2.fillRect(250, 350, 40, 40);
        
        //This is the fridge
        g2.setColor(Color.GRAY);
        g2.fillRect(0, 320, 70, 25);
        
        //This is the stove
        g2.setColor(Color.DARK_GRAY);
        g2.fillRect(0, 345, 30, 30);
        
        //This is the oven
        g2.setColor(Color.BLUE);
        g2.fillRect(0, 375, 30, 30);
        
        //This is the microwave
        g2.setColor(Color.LIGHT_GRAY);
        g2.fillRect(0, 405, 30, 30);
        
        
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
                
                //Change to city
                if((x >= 75) && (x <= 175) && (y >= 0) && (y <= 30)){
                        System.out.println("Back to the city view, goodbye!");
                        changeBackToCity(); 
                }
                
        }

        @Override
        public void mouseEntered(MouseEvent e) {
                // TODO Auto-generated method stub
                
        }

        @Override
        public void mouseExited(MouseEvent e) {
                // TODO Auto-generated method stub
                
        }
}