package activityLog;

import java.util.ArrayList;
import java.util.List;

public class ActivityLog {
	
	private static ActivityLog instance = new ActivityLog();
	
	private List<activity> log = new ArrayList<activity>();
	static ActivityPane pane;
	
	public static ActivityLog getInstance(){
		return instance;
	}
	
	public static void setPane(ActivityPane p){
		pane = p;
	}
	
	/*
	 * Activity gets added to a log of all total activities
	 * Also gets added to newActivities, which gets used when updating the activity log panel
	 */
	public void logActivity(ActivityTag t, String m, String n){
		activity a = new activity(t, m, n);
		log.add(a);
		pane.addActivity(a);
	}

}
