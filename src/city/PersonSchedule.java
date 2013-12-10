package city;

import java.util.ArrayList;
import java.util.HashMap;

import city.PersonTask.TaskType;

public class PersonSchedule {
	
	// This hashmap maps an integer between 1 and 7 (representing each day of the week) to a list of events they do on that day
	private HashMap<Integer, ArrayList<PersonTask>> weeklySchedule;
	ArrayList<PersonTask> MondayTasks = new ArrayList<PersonTask>();
	ArrayList<PersonTask> TuesdayTasks = new ArrayList<PersonTask>();
	ArrayList<PersonTask> WednesdayTasks = new ArrayList<PersonTask>();
	ArrayList<PersonTask> ThursdayTasks = new ArrayList<PersonTask>();
	ArrayList<PersonTask> FridayTasks = new ArrayList<PersonTask>();
	ArrayList<PersonTask> SaturdayTasks = new ArrayList<PersonTask>();
	ArrayList<PersonTask> SundayTasks = new ArrayList<PersonTask>();
	
	/*
	 * I want to have a reminder function in here that notifies the person an hour before their next event
	 * so that the person can get ready for it/start going to the location
	 */
	
	public PersonSchedule(){
		weeklySchedule = new HashMap<Integer, ArrayList<PersonTask>>();
		weeklySchedule.put(1, MondayTasks);
		weeklySchedule.put(2, TuesdayTasks);
		weeklySchedule.put(3, WednesdayTasks);
		weeklySchedule.put(4, ThursdayTasks);
		weeklySchedule.put(5, FridayTasks);
		weeklySchedule.put(6, SaturdayTasks);
		weeklySchedule.put(7, SundayTasks);
	}
	
	public void addTaskToDay(int dayOfWeek, PersonTask dayTask){
		ArrayList<PersonTask> dayTasks = weeklySchedule.get(dayOfWeek);
		dayTasks.add(dayTask);
		weeklySchedule.put(dayOfWeek, dayTasks);
	}
	
	public void removeTaskFromDay(int dayOfWeek, PersonTask task){
		ArrayList<PersonTask> dayTasks = weeklySchedule.get(dayOfWeek);
		dayTasks.remove(task);
		weeklySchedule.put(dayOfWeek, dayTasks);
	}
	
	public ArrayList<PersonTask> getDayTasks(int dayOfWeek){
		return weeklySchedule.get(dayOfWeek);
	}
	
	public void transferTodaysTasksToTomorrow(int dayOfWeekEnding){
		ArrayList<PersonTask> dayTasks = weeklySchedule.get(dayOfWeekEnding);
		ArrayList<PersonTask> tomorrowTasks = weeklySchedule.get(dayOfWeekEnding + 1);
		tomorrowTasks.addAll(dayTasks);
		dayTasks.clear();
		weeklySchedule.put(dayOfWeekEnding, dayTasks);
		weeklySchedule.put(dayOfWeekEnding + 1, tomorrowTasks);
	}
	
	public boolean isTaskAlreadyScheduled(TaskType type, int dayOfWeek){
		ArrayList<PersonTask> dayTasks = weeklySchedule.get(dayOfWeek);
		synchronized(dayTasks){
			for(PersonTask task : dayTasks){
				if(task.type == type){
					return true;
				}
			}
		}
		
		return false;
	}
	
}