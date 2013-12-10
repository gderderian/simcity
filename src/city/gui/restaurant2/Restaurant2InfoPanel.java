package city.gui.restaurant2;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

import city.Restaurant2.Restaurant2;

public class Restaurant2InfoPanel extends JPanel implements ActionListener{

	Restaurant2 restaurant = Restaurant2.getInstance();
    private int WINDOWX = 360;
    private int WINDOWY = 710;
    private Dimension panelDim = new Dimension(WINDOWX, WINDOWY);
    private JButton close_open = new JButton("Close Restaurant");
    private JButton set_inventory = new JButton("Set inventory low");
    String closed_open;
    private JLabel restaurantState = new JLabel("Restaurant is OPEN");
    private JLabel title = new JLabel("Restaurant 2 Info/Options\n");
    Font titleFont = new Font("Title Font", 200, Font.BOLD);
    private Dimension labelSize = new Dimension(30, 30);
    private Dimension buttonSize = new Dimension(WINDOWX - 50, 30);
	
	public Restaurant2InfoPanel(){
		title.setPreferredSize(labelSize);
		title.setAlignmentX(CENTER_ALIGNMENT);
		restaurantState.setPreferredSize(labelSize);
		restaurantState.setAlignmentX(CENTER_ALIGNMENT);
		set_inventory.setPreferredSize(buttonSize);
		set_inventory.addActionListener(this);
        add(title);
        setPreferredSize(panelDim);
        setBorder(BorderFactory.createLineBorder(Color.black));
        close_open.addActionListener(this);
        close_open.setPreferredSize(buttonSize);
        add(Box.createVerticalStrut(10));
        add(close_open);
        add(restaurantState);
        add(set_inventory);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getSource() == close_open){
			if(close_open.getText().equals("Close Restaurant")){
				restaurant.closeRestaurant();
				close_open.setText("Open Restaurant");
				restaurantState.setText("Restaurant is CLOSED");
			}
			else{
				restaurant.openRestaurant();
				close_open.setText("Close Restaurant");
				restaurantState.setText("Restaurant is OPEN");
			}
		}
		else if(e.getSource() == set_inventory){
			restaurant.setInventoryLow();
		}
		
	}
	
}