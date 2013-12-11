package city.gui.Bank;

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

//JPanel
public class BankAnimationPanel extends BuildingPanel implements ActionListener, MouseListener {

    private final int WINDOWX = 700;//450
    private final int WINDOWY = 350;
    //getting rid of magic numbers
    private static int tablewidth = 200;
    private static int tablelength = 250;
    private static int tablespacing = 50;
    
    public Image imgoftable = new ImageIcon("images/bluetable.png").getImage();
    public Image imgofmanagertable = new ImageIcon("images/table.png").getImage();
    //public Image imgofcouch = new ImageIcon("images/sofaforbank.jpg").getImage();
    public Image imgofbankfloor = new ImageIcon("images/bankfloor2.jpg").getImage();
    public Image imgofsilver = new ImageIcon("images/silver.jpeg").getImage();
    public Image imgofwood = new ImageIcon("images/brownwood.jpg").getImage();
    public Image imgofchristmastree = new ImageIcon("images/brownwood.jpg").getImage();
    public Image imgofcouch = new ImageIcon("images/coachtexture.jpg").getImage();
    
    
    private Image bufferImage;
    private Dimension bufferSize;
    private CityGui citygui;

    private List<Gui> guis = new ArrayList<Gui>();

    public BankAnimationPanel(CityGui citygui) {
    	this.citygui = citygui;
    	setSize(WINDOWX, WINDOWY);
        setVisible(true);
        setBackground(Color.white);
        addMouseListener(this);
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
      
        
        Graphics2D wall = (Graphics2D)g;
        Graphics2D waitingarea = (Graphics2D)g;
        Graphics2D backtocityviewbutton = (Graphics2D)g;
        
        //Clear the screen by painting a rectangle the size of the frame
        //g2.setColor(getBackground());
        //g2.fillRect(0, 0, this.getWidth(), this.getHeight());
       
        g.drawImage(imgofwood, 100, 164, 900, 20, this);
        g.drawImage(imgofsilver, 200, 150, 50, 50, this);
        g.drawImage(imgofsilver, 300, 150, 50, 50, this);
        g.drawImage(imgofsilver, 400, 150, 50, 50, this);
        g.drawImage(imgofsilver, 500, 150, 50, 50, this);
        g.drawImage(imgofsilver, 600, 150, 50, 50, this);
        g.drawImage(imgofsilver, 700, 150, 50, 50, this);
        g.drawImage(imgofsilver, 800, 150, 50, 50, this);
        //g.drawImage(imgofcouch, 50, 300, 400, 100, this);
        // g.drawImage(imgofcouch, 200, 300, 100, 100, this);
        //g.drawImage(imgofcouch, 350, 300, 100, 100, this);
        //g.drawImage(imgofcouch, 500, 300, 100, 100, this);
        g.drawImage(imgofsilver, 100, 0, 20, 182, this);
        //wall.setColor(Color.gray);
        //wall.fillRect(160, 0, 20, 180);
        g.drawImage(imgofwood, 160, 500, 595, 90, this);
        g.drawImage(imgofcouch, 180, 500, 130, 90, this);
        g.drawImage(imgofcouch, 320, 500, 130, 90, this);
        g.drawImage(imgofcouch, 460, 500, 130, 90, this);
        g.drawImage(imgofcouch, 600, 500, 130, 90, this);
        //waitingarea.setColor(Color.white);
        //waitingarea.fillRect(180, 470, 700, 10);
        
        //backtocityviewbutton.setColor(Color.red);
        //backtocityviewbutton.fillRect(20, 20,20, 20);
        
        //banktellerstation1.setColor(Color.gray);
        //banktellerstation1.fillRect(200, 100, 50, 50);
        //banktellerstation2.setColor(Color.gray);
        //banktellerstation2.fillRect(300, 100, 50, 50);
        //banktellerstation3.setColor(Color.gray);
        //banktellerstation3.fillRect(400, 100, 50, 50);
        //banktellerstation4.setColor(Color.gray);
        //banktellerstation4.fillRect(500, 100, 50, 50);
        
        //g.drawImage(imgofbankteller, xPos, yPos + 20, 50, 50, gui);
        
        //Here is the table
        //got rid of the magic numbers

// * This function gets moved to updatePos() below so it's called even when the panel isnt shown
 //* 
        //System.out.println("NUMBER OF GUIS ="+guis.size());
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
    	guis.add((Gui) gui);
    }
    
    public void addGui(BankRobberRoleGui gui) {
    	guis.add((Gui) gui);
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
		if((x >= 25) && (x <= 125) && (y >= 30) && (y <= 55)) {
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

	@Override
	public void updatePos() {
        for(Gui gui : guis) {
            if (gui.isPresent()) {
                gui.updatePosition();
            }
        }
	}

	@Override
	public void addGui(city.gui.Gui g) {
		// TODO Auto-generated method stub
		
	}

}
