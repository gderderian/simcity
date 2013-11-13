package city;

import java.util.Collections;
import java.util.List;
import java.util.ArrayList;

import agent.Agent;

public class PersonAgent extends Agent{
	
	//DATA
	public List<String> events = Collections.synchronizedList(new ArrayList<String>());
	
	

	public PersonAgent(){
		super();
	}
	
	//MESSAGES
	public void msgImHungry(){	//sent from GUI ?
		events.add("GotHungry");
		stateChanged();
	}
	
	
	//SCHEDULER
	protected boolean pickAndExecuteAnAction() {
		synchronized(events){
			for(String e : events){
				if(e.equals("GotHungry")){
					Eat();
					return true;
				}
			}
		}
		
		return false;
	}
	
	
	//ACTIONS
	
	public void Eat(){
		
		
	}

}
