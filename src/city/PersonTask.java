package city;

import Role.Role;

public class PersonTask {
	
	int startHour; // Positive integer representing the hour of day on the 12-hour clock, so 1-12
		//Should make everything start on the hour to simplify
	int startMinute; // Positive integer representing the minutes within an hour, so 0-59
		//Should use military time to simplify
	String amPm; // String "am" or "pm" to represent first or second half of a day
	String location; // String that holds the location of where this action is supposed to take place, "market1"
	Role role;
	
}
