package city.gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.Timer;

public class CityClock implements ActionListener {
	
	CityGui cityGui;
	Timer cityTime;
	
	public CityClock(CityGui cityGui){
		this.cityGui= cityGui;
	}

	public void startTime(){
		
		cityTime = new Timer(179999,
				new ActionListener() { public void actionPerformed(ActionEvent event) {
					// Timer is done, it is now a new day
		      }
		});
		cityTime.start();
		
	}

	public void actionPerformed(ActionEvent e) {
		
	}
	
	
}
