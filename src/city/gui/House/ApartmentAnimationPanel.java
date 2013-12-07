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
import java.util.Collections;
import java.util.List;

import javax.swing.JPanel;

import city.gui.BuildingPanel;
import city.gui.CityGui;
import city.gui.Gui;

public class ApartmentAnimationPanel extends BuildingPanel implements ActionListener, MouseListener{
	private static final int WINDOWX = 900;
	private static final int WINDOWY = 700;
	private static final int NUM_APTS = 10;
	private int aptBuilding;
	
	List<Gui> guis = Collections.synchronizedList(new ArrayList<Gui>());

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
	
	public void addGui(Gui g) {
		guis.add(g);
	}

	public void paintComponent(Graphics g) {
		super.paintComponent(g);

		Graphics2D g2 = (Graphics2D)g;

		//Clear the screen by painting a rectangle the size of the frame
		g2.setColor(getBackground());
		g2.fillRect(0, 0, WINDOWX, WINDOWY);


		//Show a layout of all of the rooms in the apartment
		//*Note: there are no people shown here, it is just the layout of rooms and the user can pick one to view
		for(int i=0; i<NUM_APTS; i++){ //First column of rooms (1-10)
			g2.setColor(Color.RED);
			g2.fillRect(200, 70 + (i * (50 + 5)), 50, 50);
			g2.setColor(Color.BLUE);
			g2.drawString("" + (i + 1), 215, 85 + (i * (50 + 5)));
		}

		for(int i=0; i<NUM_APTS; i++){ //Second column of rooms (11-20)
			g2.setColor(Color.RED);
			g2.fillRect(400, 70 + (i * (50 + 5)), 50, 50);
			g2.setColor(Color.BLUE);
			g2.drawString("" + (i + 11), 415, 85 + (i * (50 + 5)));
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

		//Change to apt 1
		if((x >= 200) && (x <= 250) && (y >= 70) && (y <= 130)){
			System.out.println("To apartment 1");
			super.cityGui.changeView(aptBuilding, 0);
		}

		//Change to apt 2 
		if((x >= 200) && (x <= 250) && (y >= 135) && (y <= 185)){
			System.out.println("To apartment 2");
			super.cityGui.changeView(aptBuilding, 1);
		}

		//Change to apt 3
		if((x >= 200) && (x <= 250) && (y >= 190) && (y <= 240)){
			System.out.println("To apartment 3");
			super.cityGui.changeView(aptBuilding, 2);
		}

		//Change to apt 4
		if((x >= 200) && (x <= 250) && (y >= 245) && (y <= 295)){
			System.out.println("To apartment 4");
			super.cityGui.changeView(aptBuilding, 3);
		}

		//Change to apt 5
		if((x >= 200) && (x <= 250) && (y >= 300) && (y <= 350)){
			System.out.println("To apartment 5");
			super.cityGui.changeView(aptBuilding, 4);
		}

		//Change to apt 6
		if((x >= 200) && (x <= 250) && (y >= 355) && (y <= 405)){
			System.out.println("To apartment 6");
			super.cityGui.changeView(aptBuilding, 5);
		}
		
		//Change to apt 7
		if((x >= 200) && (x <= 250) && (y >= 460) && (y <= 510)){
			System.out.println("To apartment 7");
			super.cityGui.changeView(aptBuilding, 6);
		}
		
		//Change to apt 8
		if((x >= 200) && (x <= 250) && (y >= 565) && (y <= 615)){
			System.out.println("To apartment 8");
			super.cityGui.changeView(aptBuilding, 7);
		}
		
		//Change to apt 9
		if((x >= 200) && (x <= 250) && (y >= 670) && (y <= 720)){
			System.out.println("To apartment 9");
			super.cityGui.changeView(aptBuilding, 8);
		}
		
		//Change to apt 10
		if((x >= 200) && (x <= 250) && (y >= 775) && (y <= 825)){
			System.out.println("To apartment 10");
			super.cityGui.changeView(aptBuilding, 9);
		}
		
		//Change to apt 11
		if((x >= 400) && (x <= 450) && (y >= 70) && (y <= 130)){
			System.out.println("To apartment 11");
			super.cityGui.changeView(aptBuilding, 10);
		}

		//Change to apt 12
		if((x >= 400) && (x <= 450) && (y >= 135) && (y <= 185)){
			System.out.println("To apartment 12");
			super.cityGui.changeView(aptBuilding, 11);
		}

		//Change to apt 13
		if((x >= 400) && (x <= 450) && (y >= 190) && (y <= 240)){
			System.out.println("To apartment 13");
			super.cityGui.changeView(aptBuilding, 12);
		}
		
		//Change to apt 14
		if((x >= 400) && (x <= 450) && (y >= 245) && (y <= 295)){
			System.out.println("To apartment 14");
			super.cityGui.changeView(aptBuilding, 13);
		}

		//Change to apt 15
		if((x >= 400) && (x <= 450) && (y >= 300) && (y <= 350)){
			System.out.println("To apartment 15");
			super.cityGui.changeView(aptBuilding, 14);
		}
		
		//Change to apt 16
		if((x >= 400) && (x <= 450) && (y >= 355) && (y <= 405)){
			System.out.println("To apartment 16");
			super.cityGui.changeView(aptBuilding, 15);
		}
				
		//Change to apt 17
		if((x >= 400) && (x <= 450) && (y >= 460) && (y <= 510)){
			System.out.println("To apartment 17");
			super.cityGui.changeView(aptBuilding, 16);
		}
				
		//Change to apt 18
		if((x >= 400) && (x <= 450) && (y >= 565) && (y <= 615)){
			System.out.println("To apartment 18");
			super.cityGui.changeView(aptBuilding, 17);
		}
				
		//Change to apt 19
		if((x >= 400) && (x <= 450) && (y >= 670) && (y <= 720)){
			System.out.println("To apartment 19");
			super.cityGui.changeView(aptBuilding, 18);
		}
				
		//Change to apt 20
		if((x >= 200) && (x <= 250) && (y >= 775) && (y <= 825)){
			System.out.println("To apartment 20");
			super.cityGui.changeView(aptBuilding, 19);
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
}
