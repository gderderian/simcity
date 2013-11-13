package city;

import java.util.Collections;
import java.util.List;
import java.util.ArrayList;
import java.util.Random;

import agent.Agent;

public class PersonAgent extends Agent{
	
	//DATA
	public List<String> events = Collections.synchronizedList(new ArrayList<String>());
	public List<String> foodsToEat = new ArrayList<String>();
	

	public PersonAgent(){
		super();
		
		//populate foods list -- need to make sure this matches up with market
		foodsToEat.add("Chicken");
		foodsToEat.add("Steak");
		foodsToEat.add("Salad");
		foodsToEat.add("Pizza");
		
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
	
	public void Eat(){	//hacked for now so that it randomly picks eating at home or going out
		Random rand = new Random();
		int x = rand.nextInt(1);
		if(x == 1){
			int y = rand.nextInt(foodsToEat.size());
			String food = foodsToEat.get(y);
			//house.msgCheckFridge(food);
			print("I'm going to eat " + food);
		}
		else{
			goToRestaurant();
		}
	}
	
	public void goToRestaurant(){
		print("Going to go to a restaurant");
	}
	
	
	//OTHER METHODS

}
