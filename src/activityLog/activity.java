package activityLog;

import java.util.Date;

public class activity implements Comparable<activity>{
	ActivityTag type;
	String name;
	String message;
	String building;
	public final Date date;
	boolean person_notRole; 	//this is true if its coming from a person, false if from a role
	
	public activity(ActivityTag t, String m, String n, Date d, boolean p){
		type = t;
		message = m;
		name = n;
		date = d;
		person_notRole = p;
	}
	
	public String getMessage(){
		return message;
	}

	public String getName() {
		return name;
	}

	@Override
	public int compareTo(activity b) {
		if(this.date.after(b.date)){	//if this activity comes after activity b
			return 1;
		}
		else if(this.date.before(b.date)){
			return -1;
		}
		return 0;
	}
}
