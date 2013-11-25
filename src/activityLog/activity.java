package activityLog;

public class activity {
	ActivityTag type;
	String name;
	String message;
	String building;
	
	public activity(ActivityTag t, String m, String n){
		type = t;
		message = m;
		name = n;
	}
	
	public String getMessage(){
		return message;
	}

	public String getName() {
		return name;
	}
}
