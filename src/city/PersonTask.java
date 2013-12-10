package city;

public class PersonTask {
	
	enum TaskType {gotHungry, goToWork, goToMarket, getOnBus, goToBank, goToApartment, doneWithWork, goHome, robBank};
	enum Transportation {undecided, walking, bus, car};
	enum State {initial, processing, inTransit, arrived, complete};
	/*
	 * STATES explained
	 * initial - before the person starts the task
	 * inTransit - if a person is in the process of using transportation (other than walking), they are in this state
	 * arrived - if the person has arrived at his/her destination, but has not completed the task
	 * complete - once the person has completed the task, the task is in this state
	 */
	
	public int startHour; // Positive integer representing the hour of day on the 12-hour clock, so 1-12
		//Should make everything start on the hour to simplify
	public int startMinute; // Positive integer representing the minutes within an hour, so 0-59
		//Should use military time to simplify
	public String amPm; // String "am" or "pm" to represent first or second half of a day
	public String location; // String that holds the location of where this action is supposed to take place, "market1"
	public String role;
	//public Role role;	should it change to this?
	public TaskType type;
	public Transportation transportation;
	public State state;
	
	public PersonTask(TaskType t){
		type = t;
		state = State.initial;
		transportation = Transportation.undecided;
		startHour = -1;
		startMinute = -1;	//may take this out
		amPm = null;	//may take this out
		location = null;
		role = null;
		//initialized most variables to null for checking purposes later and so the person doesnt get wrong information
	}
	
	public PersonTask(String t){
		type = TaskType.valueOf(t);
		state = State.initial;
		transportation = Transportation.undecided;
		startHour = -1;
		startMinute = -1;	//may take this out
		amPm = null;	//may take this out
		location = null;
		role = null;
		//initialized most variables to null for checking purposes later and so the person doesnt get wrong information
	}
	
}
