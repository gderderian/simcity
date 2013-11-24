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
    
    public Image imgofcouch = new ImageIcon("images/sofaforbank.jpg").getImage();
    public Image imgofbankfloor = new ImageIcon("images/bankfloor.jpg").getImage();
    private Image bufferImage;
    private Dimension bufferSize;

    private List<Gui> guis = new ArrayList<Gui>();

    public BankAnimationPanel() {
    	setSize(WINDOWX, WINDOWY);
        setVisible(true);
        setBackground(Color.white);
        bufferSize = this.getSize();
    	Timer timer = new Timer(13, this );
    	timer.start();
    }

	public void actionPerformed(ActionEvent e) {
		repaint();  //Will have paintComponent called
	}

	
	
    public void paintComponent(Graphics g) {
        g.drawImage(imgofbankfloor, 0,0,this);
    	Graphics2D g2 = (Graphics2D)g;
        Graphics2D banktellerstation1 = (Graphics2D)g;
        Graphics2D banktellerstation2 = (Graphics2D)g;
        Graphics2D banktellerstation3 = (Graphics2D)g;
        Graphics2D banktellerstation4 = (Graphics2D)g;
      
        
        Graphics2D waitingarea = (Graphics2D)g;
        
        //Clear the screen by painting a rectangle the size of the frame
        g2.setColor(getBackground());
        g2.fillRect(0, 0, this.getWidth(), this.getHeight());
       
        g.drawImage(imgofcouch, 50, 300, 400, 100, this);
        // g.drawImage(imgofcouch, 200, 300, 100, 100, this);
        //g.drawImage(imgofcouch, 350, 300, 100, 100, this);
        g.drawImage(imgofcouch, 500, 300, 100, 100, this);

        waitingarea.setColor(Color.gray);
        waitingarea.fillRect(5, 40, 20, 270);
        
        banktellerstation1.setColor(Color.gray);
        banktellerstation1.fillRect(200, 100, 50, 50);
        banktellerstation2.setColor(Color.gray);
        banktellerstation2.fillRect(300, 100, 50, 50);
        banktellerstation3.setColor(Color.gray);
        banktellerstation3.fillRect(400, 100, 50, 50);
        banktellerstation4.setColor(Color.gray);
        banktellerstation4.fillRect(500, 100, 50, 50);
        
        //g.drawImage(imgofbankteller, xPos, yPos + 20, 50, 50, gui);
        
        //Here is the table
        //got rid of the magic numbers

        for(Gui gui : guis) {
            if (gui.isPresent()) {
                gui.updatePosition();
            }
        }

        for(Gui gui : guis) {
            if (gui.isPresent()) {
                gui.draw(g2);
                //gui.draw(g3); 
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
