package city.gui.Bank;

import javax.swing.*;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import java.util.ArrayList;

public class BankAnimationPanel extends JPanel implements ActionListener {

    private final int WINDOWX = 700;//450
    private final int WINDOWY = 350;
    //getting rid of magic numbers
    private static int tablewidth = 200;
    private static int tablelength = 250;
    private static int tablespacing = 50;
    
    
    private Image bufferImage;
    private Dimension bufferSize;

    private List<Gui> guis = new ArrayList<Gui>();

    public BankAnimationPanel() {
    	setSize(WINDOWX, WINDOWY);
        setVisible(true);
        bufferSize = this.getSize();
    	Timer timer = new Timer(13, this );
    	timer.start();
    }

	public void actionPerformed(ActionEvent e) {
		repaint();  //Will have paintComponent called
	}

    public void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D)g;
        Graphics2D g3 = (Graphics2D)g;
        Graphics2D g4 = (Graphics2D)g;
        Graphics2D g5 = (Graphics2D)g;
        Graphics2D kitchentable = (Graphics2D)g;
        Graphics2D cookingarea = (Graphics2D)g;
        Graphics2D stove = (Graphics2D)g;
        Graphics2D waitingarea = (Graphics2D)g;
        
        //Clear the screen by painting a rectangle the size of the frame
        g2.setColor(getBackground());
        g2.fillRect(0, 0, this.getWidth(), this.getHeight());
        
        g3.setColor(Color.orange);
        g3.fillRect(260, 250, 50, 50);
       
        g4.setColor(Color.orange);
        g4.fillRect(320, 250, 50, 50);
       
        g5.setColor(Color.orange);
        g5.fillRect(380, 250, 50, 50);
        
        kitchentable.setColor(Color.yellow);
        kitchentable.fillRect(340, 70, 250, 40);
        
        cookingarea.setColor(Color.gray);
        cookingarea.fillRect(340, 7, 250, 20);
        
        waitingarea.setColor(Color.gray);
        waitingarea.fillRect(5, 40, 20, 270);
     
        
        //Here is the table
        //got rid of the magic numbers
        g2.setColor(Color.ORANGE);
        g2.fillRect(tablewidth, tablelength, tablespacing, tablespacing);//200 and 250 need to be table params


        for(Gui gui : guis) {
            if (gui.isPresent()) {
                gui.updatePosition();
            }
        }

        for(Gui gui : guis) {
            if (gui.isPresent()) {
                gui.draw(g2);
                gui.draw(g3); 
            }
        }
    }

    public void addGui(BankCustomerRoleGui gui) {
        guis.add(gui);
    }

    public void addGui(BankManagerRoleGui gui) {
        guis.add(gui);
    }
 
    public void addGui(BankTellerRoleGui gui) {
    	guis.add(gui);
    }
}
