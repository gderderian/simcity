package city;

import java.util.ArrayList;
import java.util.HashMap;

public class PersonSchedule {
	
	// This hashmap maps an integer between 1 and 8 (representing each day of the week) to a list of events they do on that day
	HashMap<Integer, ArrayList<PersonDayPlanner>> weeklySchedule;
	
}