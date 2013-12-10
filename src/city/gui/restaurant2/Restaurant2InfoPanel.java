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
    String closed_open;
    private JLabel restaurantState = new JLabel("Restaurant is OPEN");
    private JLabel title = new JLabel("Restaurant 2 Info/Options\n");
    Font titleFont = new Font("Title Font", 200, Font.BOLD);
	
	public Restaurant2InfoPanel(){
        add(title);
        setPreferredSize(panelDim);
        setBorder(BorderFactory.createLineBorder(Color.black));
        close_open.addActionListener(this);
        add(Box.createVerticalStrut(10));
        add(restaurantState);
        add(close_open);
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
		
	}
	
}