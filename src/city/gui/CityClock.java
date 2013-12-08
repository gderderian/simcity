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
	int cityTimeDelay = 179999;
	
	public enum dayStates {morning, afternoon, night};
	
	public CityClock(CityGui cityGui){
		this.cityGui= cityGui;
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
						cityTimeDelay = 179999;
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
		checkTimer = new Timer(1875, // Messages to people in city fire every 2 seconds
				new ActionListener() { public void actionPerformed(ActionEvent event) {
					// Message all people saying the current time is:
					// System.currentTimeMillis() - beginTime
					if (getCurrentTime() <= 59999){
						dayState = dayStates.morning;
					} else if (getCurrentTime() >= 60000 && getCurrentTime() <= 119999){
						dayState = dayStates.afternoon;
					} else  if (getCurrentTime() >= 120000){
						dayState = dayStates.night;
					}
					
					// Below is test to manually set date/time if necessary
					/*
					if (dayState == dayStates.afternoon){
						setDayTime(10, 0, "pm");
					}
					*/
					
					//System.out.println("Time since start is " + getCurrentTime() + ", day is " + day + " (" + getDayOfWeek() + "), portion of day is " + getDayState() + "  - Delay is " + cityTime.getDelay());
					//System.out.println("Human time is " + getHumanTime());
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
	
	public String getDayState(){
		if (dayState.equals(dayState.morning)){
			return "Morning";
		} else if (dayState.equals(dayState.afternoon)){
			return "Afternoon";
		} else {
			return "Night";
		}
	}
	
	public long getHourOfDayInMilTime(long baseTime){ // You should normally pass in getCurrentTime() by default
		long calcHour = baseTime * 1440;
		long hourFinalCalc = calcHour / 179999;
		return hourFinalCalc / 60;
	}
	
	public int getHourOfDayInHumanTime(){
		long hours = getHourOfDayInMilTime(getCurrentLongTime()) % 12;
		return (int)hours;
	}
	
	public long getMinuteOfDay(long baseTime){ // You should normally pass in getCurrentTime() by default
		long calcMinute = baseTime * 1440;
		long minuteFinalCalc = calcMinute / 179999;
		return minuteFinalCalc % 60;
	}
	
	public long getMinuteOfDay(){ // You should normally pass in getCurrentTime() by default
		long calcMinute = getCurrentTime() * 1440;
		long minuteFinalCalc = calcMinute / 179999;
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
		int calcDayHours = 179999 * hour;
		int totalDayHours = calcDayHours / 24;
		
		//System.out.println("Newly set hours: " + totalDayHours);
		
		// Calculate minutes in total day timer
		int calcDayMinutes = 179999 * minute;
		int totalDayMinutes = calcDayMinutes / 60;
		
		//System.out.println("Newly set minutes: " + totalDayMinutes);
		
		int finalTimerSet = totalDayHours + totalDayMinutes + 1; // This is what our timer needs to be reset and then set to
		
		//System.out.println("Newly set total: " + finalTimerSet);
		
		if (finalTimerSet <= 59999){
			dayState = dayStates.morning;
		} else if (getCurrentTime() >= 60000 && getCurrentTime() <= 119999){
			dayState = dayStates.afternoon;
		} else  if (getCurrentTime() >= 120000){
			dayState = dayStates.night;
		}
		
		beginTime = System.currentTimeMillis() - (finalTimerSet);
		
		cityTime.stop();
		cityTime.setDelay(finalTimerSet);
		manualSet = true;
		//System.out.println("Time delay: " + cityTime.getDelay());
		cityTime.restart();
		//beginTime = System.currentTimeMillis();
		//checkTimer.restart();
		
		//System.out.println("Newly set time: " + getHumanTime());
		
	}
	
}
