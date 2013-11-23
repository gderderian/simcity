package city.gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.Timer;

public class CityClock {
	
	CityGui cityGui;
	Timer cityTime;
	Timer checkTimer;
	long beginTime;
	dayStates dayState;
	int day;
	
	public enum dayStates {morning, afternoon, night};
	
	public CityClock(CityGui cityGui){
		this.cityGui= cityGui;
	}

	public void startTime(){
		
		// Begin actual city timer
		cityTime = new Timer(179999,
				new ActionListener() { public void actionPerformed(ActionEvent event) {
					// Timer is done, it is now a new day
					day++;
		      }
		});
		cityTime.start();
		beginTime = System.currentTimeMillis();
		
		// Initial setup
		dayState = dayStates.morning;
		day = 1;
		
		// Begin checker/notification timer to manually adjust day/night
		checkTimer = new Timer(2000,
				new ActionListener() { public void actionPerformed(ActionEvent event) {
					// Message all people saying the current time is:
					// System.currentTimeMillis() - beginTime
					checkTimer.restart(); // Restarts every two seconds
		      }
		});
		checkTimer.start();
	}
	
	public int getCurrentTime(){
		return (int)(System.currentTimeMillis() - beginTime);
	}
	
}
