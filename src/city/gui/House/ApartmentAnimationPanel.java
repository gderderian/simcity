package city.gui.House;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JPanel;

import city.gui.BuildingPanel;
import city.gui.CityGui;

public class ApartmentAnimationPanel extends BuildingPanel implements ActionListener, MouseListener{
        private static final int WINDOWX = 1300;
    private static final int WINDOWY = 700;
    private static final int NUM_APTS = 5;
    private static int aptBuilding;
    
    public ApartmentAnimationPanel(int num) {
            setSize(WINDOWX, WINDOWY);
            setPreferredSize(new Dimension(WINDOWX, WINDOWY));
            setMaximumSize(new Dimension(WINDOWX, WINDOWY));
            setMinimumSize(new Dimension(WINDOWX, WINDOWY));
        setVisible(true);
        
        aptBuilding= num;
        
        addMouseListener(this);
    }
    
    public void actionPerformed(ActionEvent e) {
                repaint();
        }
    
    public void paintComponent(Graphics g) {
                super.paintComponent(g);
                
                Graphics2D g2 = (Graphics2D)g;
                
                //Clear the screen by painting a rectangle the size of the frame
        g2.setColor(getBackground());
        g2.fillRect(0, 0, WINDOWX, WINDOWY);
                
        
        //This is the door to the city
        g2.setColor(Color.CYAN);
        g2.fillRect(400, 0, 100, 30);
        g2.setColor(Color.BLACK);
        g2.drawString("Back to City", 405, 20);
        
        
        //Show a layout of all of the rooms in the apartment
        //*Note: there are no people shown here, it is just the layout of rooms and the user can pick one to view
        for(int i=0; i<NUM_APTS; i++){ //First column of rooms (1-5)
                g2.setColor(Color.RED);
                g2.fillRect(600, 70 + (i * (50 + 5)), 50, 50);
                g2.setColor(Color.BLUE);
                g2.drawString("" + (i + 1), 615, 85 + (i * (50 + 5)));
        }
        
        for(int i=0; i<NUM_APTS; i++){ //Second column of rooms (6-10)
                g2.setColor(Color.RED);
                g2.fillRect(800, 70 + (i * (50 + 5)), 50, 50);
                g2.setColor(Color.BLUE);
                g2.drawString("" + (i + 6), 815, 85 + (i * (50 + 5)));
        }
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
                if((x >= 400) && (x <= 575) && (y >= 0) && (y <= 30)){
                        System.out.println("Back to the city view, goodbye!");
                        cityGui.changeView("City");
                }
                
                //Change to apt 1
                if((x >= 600) && (x <= 650) && (y >= 70) && (y <= 130)){
                        System.out.println("To apartment 1");
                        cityGui.changeView(aptBuilding, 0);
                }
                
                //Change to apt 2 
                if((x >= 600) && (x <= 650) && (y >= 135) && (y <= 185)){
                        System.out.println("To apartment 2");
                        cityGui.changeView(aptBuilding, 1);
                }
                
                //Change to apt 3
                if((x >= 600) && (x <= 650) && (y >= 190) && (y <= 240)){
                        System.out.println("To apartment 3");
                        cityGui.changeView(aptBuilding, 2);
                }
                
                //Change to apt 4
                if((x >= 600) && (x <= 650) && (y >= 245) && (y <= 295)){
                        System.out.println("To apartment 4");
                        cityGui.changeView(aptBuilding, 3);
                }
                
                //Change to apt 5
                if((x >= 600) && (x <= 650) && (y >= 300) && (y <= 350)){
                        System.out.println("To apartment 5");
                        cityGui.changeView(aptBuilding, 4);
                }
                
                //Change to apt 6
                if((x >= 800) && (x <= 850) && (y >= 70) && (y <= 130)){
                        System.out.println("To apartment 6");
                        cityGui.changeView(aptBuilding, 5);
                }
                
                //Change to apt 7
                if((x >= 800) && (x <= 850) && (y >= 135) && (y <= 185)){
                        System.out.println("To apartment 7");
                        cityGui.changeView(aptBuilding, 6);
                }
                 
                //Change to apt 8
                if((x >= 800) && (x <= 850) && (y >= 190) && (y <= 240)){
                        System.out.println("To apartment 8");
                        cityGui.changeView(aptBuilding, 7);
                }
                
                //Change to apt 9
                if((x >= 800) && (x <= 850) && (y >= 245) && (y <= 295)){
                        System.out.println("To apartment 9");
                        cityGui.changeView(aptBuilding, 8);
                }
                
                //Change to apt 10
                if((x >= 800) && (x <= 850) && (y >= 300) && (y <= 350)){
                        System.out.println("To apartment 10");
                        cityGui.changeView(aptBuilding, 9);
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
