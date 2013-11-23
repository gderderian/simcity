package city.gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Timer;
import java.util.TimerTask;

public class CityClock implements ActionListener {
	CityGui cityGui;
	Timer time= new Timer();
	
	public CityClock(CityGui cityGui){
		this.cityGui= cityGui;
	}

	public startTime(){
		time.scheduleAtFixedRate(new TimerTask() {
			@Override public void run() {
				@Override
				public void actionPerformed(ActionEvent e) {
					
				}
			}}, 179999);
	}
	
	
}
