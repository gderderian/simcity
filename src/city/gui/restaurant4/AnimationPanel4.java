package city.gui.restaurant4;

import javax.swing.*;

import city.gui.BuildingPanel;
import city.gui.CityGui;
import city.gui.Gui;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.List;
import java.util.ArrayList;

public class AnimationPanel4 extends BuildingPanel implements ActionListener, MouseListener {

    private static final int NTABLES= 4;
	private static final int xPosTable= 100;
    private final int WINDOWX = 900;
    private final int WINDOWY = 700;
    private final int TIMER = 8;
    private final int SCREENX = 0;
    private final int SCREENY = 0;
    private final int TABLEX = 100;
    private final int TABLEY = 250;
    private final int TABLEDIMENSIONS = 50;
    private final int GRILLDIMENSIONS= 30;
    private final int GRILLX= 300;
    private final int PLATEX= 325;
    private final int GRILLXEND= 440;
    private final int GRILLY= 550;
    private final int PLATEY= 450;
    private final int NXSTATIONS= 5;
    private final int NYSTATIONS= 3;

    private List<Gui> guis = new ArrayList<Gui>();

    
  	
    public AnimationPanel4() {
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
		repaint();  //Will have paintComponent called
	}

    public void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D)g;

        //Clear the screen by painting a rectangle the size of the frame
        g2.setColor(getBackground());
        g2.fillRect(SCREENX, SCREENY, WINDOWX, WINDOWY);

        //Back to city map
        /*g2.setColor(Color.CYAN);
        g2.fillRect(700, 65, 100, 30);
        g2.setColor(Color.BLACK);
        g2.drawString("Exit to City", 715, 80); */
        
        //Here is the table
        for(int i=0; i<NTABLES; i++){
        	g2.setColor(Color.ORANGE);
        	g2.fillRect(TABLEX + (xPosTable*i), TABLEY, TABLEDIMENSIONS, TABLEDIMENSIONS);
        }

        //Here is the cook's grilling station
        g2.setColor(Color.LIGHT_GRAY);
        for(int i=0; i<NXSTATIONS; i++){
        	g2.fillRect(GRILLX + (GRILLDIMENSIONS*i), GRILLY, GRILLDIMENSIONS, GRILLDIMENSIONS);
        }
        for(int i=0; i<NYSTATIONS; i++){
        	g2.fillRect(GRILLXEND , GRILLY - (GRILLDIMENSIONS*i), GRILLDIMENSIONS, GRILLDIMENSIONS);
        }
        
        //Here is the cook's plating station
        g2.setColor(Color.YELLOW);
        for(int i=0; i<NXSTATIONS; i++){
        	g2.fillRect(PLATEX + (GRILLDIMENSIONS*i), PLATEY, GRILLDIMENSIONS, GRILLDIMENSIONS);
        }
        
        /*
         * This gets moved to updatePos below so it will be called even when the panel is not visible
         *
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
    
    public void addGui(Gui gui) {
        guis.add(gui);
    }

	public void setCityGui(CityGui cityGui) {
		this.cityGui= cityGui;
		
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
		int clickedX= e.getX();
		int clickedY= e.getY();
		
		/*if((clickedX > 700) && (clickedX < 800) && (clickedY > 75) && (clickedY < 105)){
			changeBackToCity();
		}*/
		
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
		// TODO Auto-generated method stub
		
	}
}
