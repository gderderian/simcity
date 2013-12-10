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
	int week;
	boolean manualSet;
	int cityTimeDelay = 359998;
	
	public enum dayStates {morning, afternoon, night};
	
	public CityClock(CityGui cityGui){
		this.cityGui= cityGui;
		day = 1;
	}

	public void startTime(){
		
		// Begin actual city timer
		cityTime = new Timer(cityTimeDelay, // Start of day, 179999 (day is three minutes)
				new ActionListener() { public void actionPerformed(ActionEvent event) {
					// Timer is done, it is now a new day
					if (day == 8){
						day = 1;
						week++;
					} else {
						day++;
					}
					cityTime.restart();
					beginTime = System.currentTimeMillis();
					System.out.println("Today is a new day: " + day + " - " + getDayOfWeek());
					if (manualSet == true){
						manualSet = false;
						cityTimeDelay = 359998;
					}
		      }
		});
		cityTime.start();
		beginTime = System.currentTimeMillis();
		
		// Initial setup
		dayState = dayStates.morning;
		day = 1;
		week = 1;
		
		// Begin checker/notification timer to manually adjust day/night
		checkTimer = new Timer(3750, // Messages to people in city fire every 2 seconds, was 1875
				new ActionListener() { public void actionPerformed(ActionEvent event) {
					// Message all people saying the current time is:
					// System.currentTimeMillis() - beginTime
					if (getCurrentTime() <= 119998){ // Was 59999
						dayState = dayStates.morning;
					} else if (getCurrentTime() >= 120000 && getCurrentTime() <= 239998){ // Was 60000 and 119999
						dayState = dayStates.afternoon;
					} else  if (getCurrentTime() >= 240000){ // Was 120000
						dayState = dayStates.night;
					}
					
					// Below is test to manually set date/time if necessary
					/*
					if (dayState == dayStates.afternoon){
						setDayTime(10, 0, "pm");
					}
					*/
					
					System.out.println("Time since start is " + getCurrentTime() + ", day is " + day + " (" + getDayOfWeek() + "), portion of day is " + getDayState() + "  - Delay is " + cityTime.getDelay());
					System.out.println("Human time is " + getHumanTime());
					String fullTimeToSend = getHumanTime() + "  -  " + getDayOfWeek() + "  -  Week " + week;
					cityGui.timerTick(getCurrentTime(), getHourOfDayInHumanTime(), getMinuteOfDay(), getDayState(), getAmPm(), fullTimeToSend);
					checkTimer.restart(); // Restarts every two seconds
		      }
		});
		checkTimer.start();
	}
	
	public int getCurrentTime(){
		return (int)(System.currentTimeMillis() - beginTime);
	}
	
	public long getCurrentLongTime(){
		return (System.currentTimeMillis() - beginTime);
	}
	
	public String getDayOfWeek(){
		String dayText = "";
		switch(day){
			case 1:
				dayText = "Monday";
				break;
			case 2:
				dayText = "Tuesday";
				break;
			case 3:
				dayText = "Wednesday";
				break;
			case 4:
				dayText = "Thursday";
				break;
			case 5:
				dayText = "Friday";
				break;
			case 6:
				dayText = "Saturday";
				break;
			case 7:
				dayText = "Sunday";
				break;
			default:
				dayText = "";
				break;
		}
		return dayText;
	}
	
	public int getDayOfWeekNum(){
		return day;
	}
	
	public String getDayState(){
		if (dayState.equals(dayStates.morning)){
			return "Morning";
		} else if (dayState.equals(dayStates.afternoon)){
			return "Afternoon";
		} else {
			return "Night";
		}
	}
	
	public long getHourOfDayInMilTime(long baseTime){ // You should normally pass in getCurrentTime() by default
		long calcHour = baseTime * 1440;
		long hourFinalCalc = calcHour / 359998; // Was 179999
		return hourFinalCalc / 60;
	}
	
	public int getHourOfDayInHumanTime(){
		long hours = getHourOfDayInMilTime(getCurrentLongTime()) % 12;
		return (int)hours;
	}
	
	public long getMinuteOfDay(long baseTime){ // You should normally pass in getCurrentTime() by default
		long calcMinute = baseTime * 1440;
		long minuteFinalCalc = calcMinute / 359998;
		return minuteFinalCalc % 60;
	}
	
	public long getMinuteOfDay(){ // You should normally pass in getCurrentTime() by default
		long calcMinute = getCurrentTime() * 1440;
		long minuteFinalCalc = calcMinute / 359998;
		return minuteFinalCalc % 60;
	}
	
	public String getHumanTime(int baseHours, int baseMinutes){
		String amPm = "";
		int hours = baseHours % 12;
		if (baseHours <= 11){
			amPm = "am"; 
		} else {
			amPm = "pm";
		}
		return hours + ":" + baseMinutes + amPm;
	}
	
	public String getHumanTime(){
		String amPm = "";
		long minutes = getMinuteOfDay(getCurrentLongTime());
		long hours = getHourOfDayInMilTime(getCurrentLongTime()) % 12;
		String leadingDisplayZero = "";
		if (hours == 0){
			hours = 12;
		}
		if (minutes < 10){
			leadingDisplayZero = "0";
		}
		if (getHourOfDayInMilTime(getCurrentTime()) <= 11){
			amPm = "am"; 
		} else {
			amPm = "pm";
		}
		return hours + ":" + leadingDisplayZero + minutes + amPm;
	}
	
	public String getAmPm(){
		String amPm = "";
		if (getHourOfDayInMilTime(getCurrentTime()) <= 11){
			amPm = "am"; 
		} else {
			amPm = "pm";
		}
		return amPm;
	}
	
	public void setDayTime(int hour, int minute, String amPm){
		
		
		if (amPm.equals("pm")){
				hour = hour + 12;	
		}
		
		// Calculate hours in total day timer
		int calcDayHours = 359998 * hour; // Was 179999
		int totalDayHours = calcDayHours / 24;
		
		//System.out.println("Newly set hours: " + totalDayHours);
		
		// Calculate minutes in total day timer
		int calcDayMinutes = 59 * minute;
		int totalDayMinutes = calcDayMinutes / 59;
		
		//System.out.println("Newly set minutes: " + totalDayMinutes);
		
		int finalTimerSet = totalDayHours + totalDayMinutes; // This is what our timer needs to be reset and then set to
		
		//System.out.println("Newly set total: " + finalTimerSet);
		
		if (getCurrentTime() <= 119998){ // Was 59999
			dayState = dayStates.morning;
		} else if (getCurrentTime() >= 120000 && getCurrentTime() <= 239998){ // Was 60000 and 119999
			dayState = dayStates.afternoon;
		} else  if (getCurrentTime() >= 240000){ // Was 120000
			dayState = dayStates.night;
		}
		
		beginTime = System.currentTimeMillis() - (finalTimerSet);
		
		cityTime.stop();
		
		cityTime.setInitialDelay(359998 - finalTimerSet); // Was 179999
		manualSet = true;
		//System.out.println("Time delay: " + cityTime.getDelay() + " - New begin: " + finalTimerSet);
		cityTime.start();
		//beginTime = System.currentTimeMillis();
		checkTimer.stop();
		checkTimer.setInitialDelay(3750); // Was 1875
		checkTimer.start();
		
		//System.out.println("Newly set time: " + getHumanTime());
		
	}
	
}
