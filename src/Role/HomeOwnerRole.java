package Role;

import interfaces.HomeOwner;

import java.util.*;

import city.Food;
import city.HouseAgent;
import city.PersonAgent;

public class HomeOwnerRole extends Role implements HomeOwner {
	//DATA
	PersonAgent landlord;
	HouseAgent house;
	List<Food> groceries = new ArrayList<Food>();
	boolean rentDue;
	boolean hungry;
	int fridgeSpace;
	String choice;
	//needs access to person's money to pay rent

	public HomeOwnerRole(HouseAgent house){
		this.house= house;
	}
	
	public void setHungry(boolean hungry, String choice){
		this.hungry= hungry;
		this.choice= choice;
	}
	
	
	//MESSAGES
	//does this need to get some sort of I'm hungry message to start simulation?
	
	public void msgFridgeFull(){
		
	}
	
	public void msgDontHaveItem(String type){
		//tryFridgeAgain();
	}
	
	public void msgItemInStock(String type){
		//cook(type);
	}
	
	public void msgSpaceInFridge(int space){
		fridgeSpace= space;
	}
	
	public void msgNoSpaceCantCook(){
		//tryCookAgain();
	}
	
	public void msgApplianceBrokeCantCook(){
		//tryCookAgain();
		//fixAppliance();
	}
	
	public void msgFoodDone(String type){
		//eat();
	}
	
	public void msgRentDue(Double rent){
		
	}
	
	
	//SCHEDULER
	protected boolean pickAndExecuteAnAction() {
		if(hungry){
			cookFood();
		}
		
		return false;
	}
	
	
	//ACTIONS
	private void cookFood(){
		house.checkFridge(choice);
	}
} 
