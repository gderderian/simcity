package city;

import java.util.ArrayList;
import java.util.HashMap;

public class PersonSchedule {
	
	// This hashmap maps an integer between 1 and 7 (representing each day of the week) to a list of events they do on that day
	private HashMap<Integer, ArrayList<PersonTask>> weeklySchedule;
	
	/*
	 * I want to have a reminder function in here that notifies the person an hour before their next event
	 * so that the person can get ready for it/start going to the location
	 */
	
	public PersonSchedule(){
		weeklySchedule = new HashMap<Integer, ArrayList<PersonTask>>();
	}
	
	public void addTaskToDay(int dayOfWeek, PersonTask dayTask){
		ArrayList<PersonTask> dayTasks = weeklySchedule.get(dayOfWeek);
		dayTasks.add(dayTask);
		weeklySchedule.put(dayOfWeek, dayTasks);
	}
	
	public ArrayList<PersonTask> getDayTasks(int dayOfWeek){
		return weeklySchedule.get(dayOfWeek);
	}
	
}