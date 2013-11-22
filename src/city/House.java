package city;

import java.util.*;

import Role.HomeOwnerRole;

public class House {
	//DATA
	private PersonAgent owner;
	private HomeOwnerRole homeowner;
	private Timer cook= new Timer();
	private Appliance fridge= new Appliance("Fridge");
	private Appliance microwave= new Appliance("Microwave");
	private Appliance oven= new Appliance("Oven");
	private Appliance stove= new Appliance("Stove");
	private List<Appliance> cookingAppliances= new ArrayList<Appliance>(); 

	
	//CONSTRUCTOR
	public House(){
		super();
		
		cookingAppliances.add(microwave);
		cookingAppliances.add(oven);
		cookingAppliances.add(stove);
	}
	
	
	//SETTERS/GETTERS
	public void setOwner(PersonAgent p){
		owner= p;
		homeowner= new HomeOwnerRole(this);
		owner.addRole(homeowner, true);
	}
	
	
	//PUBLIC METHODS
	public void boughtGroceries(List<Food> groceries){
		if(groceries.size() <= (fridge.capacity - fridge.currentAmount)){
			homeowner.msgFridgeFull();
		}
		for(int i=0; (i<groceries.size()) && (fridge.currentAmount < fridge.capacity); i++){
			fridge.addItem(groceries.get(i));
			fridge.currentAmount++;
		}
	}

	public void checkFridge(String type){
		MyFood temp= new MyFood(type);
		if(fridge.food.containsKey(temp.food.type)){
			int tempAmount= fridge.food.get(temp.food.type).currentAmount;
			if(tempAmount > 0){
				homeowner.msgItemInStock(temp.food.type); 
				return;
			}
		}
		homeowner.msgDontHaveItem(temp.food.type);	
	}
	
	public void spaceInFridge(){
		int spaceLeft= fridge.capacity - fridge.currentAmount;
		homeowner.msgSpaceInFridge(spaceLeft);
	}

	public void cookFood(String type){
		final MyFood temp= new MyFood(type);
		if(fridge.food.containsKey(temp.food.type)){
			int tempAmount= fridge.food.get(temp.food.type).currentAmount;
			if(tempAmount > 0){
				fridge.removeItem(temp.food);
				int cookTime= temp.food.cookTime;
				Appliance app= getAppliance(temp.food.appliance);
				if(app.isBroken){
					homeowner.msgApplianceBrokeCantCook();
				}
				if((app != null) && (app.currentAmount < app.capacity)){
					cook.schedule(new TimerTask() {
						@Override public void run() {
							homeowner.msgFoodDone(temp.food.type);
						}}, cookTime);
					Random rand = new Random();
					int num= rand.nextInt(10);
					if(num == 0){ //there is a 1/10 chance the appliance will break each time it is used
						app.isBroken= true;
					}
					return;
				}
			}
		}
		homeowner.msgNoSpaceCantCook();
	}

	public void fixedAppliance(String appliance){
		for(Appliance a : cookingAppliances){
			if(a.type.equals(appliance)){
				a.isBroken= false;
			}
		}
	}

	
	//INTERNAL METHODS
	private Appliance getAppliance(String type){
		for(Appliance a : cookingAppliances){
			if(a.type.equals(type)){
				return a;
			}
		}
		return null;
	}
	
	
	//CLASSES
	private class Appliance{
		String type;
		int capacity; 
		int currentAmount;
		Map<String, MyFood>  food= new HashMap<String, MyFood>(); 
		Boolean isBroken= false;

		Appliance(String type){
	 		this.type= type;
			currentAmount= 0;
			if(type.equals("Fridge")){
				capacity= 10;
			}
			else if(type.equals("Microwave") || type.equals("Oven")){
				capacity= 1;
			}
			else if(type.equals("Stove")){
				capacity= 4;
			}
		}

		public void addItem(Food f){
			MyFood myFood;
			if(food.containsKey(f.type)){
				myFood= food.get(f.type);
				myFood.currentAmount += 1;
				food.put(myFood.food.type, myFood);
			}
			else{
				myFood= new MyFood(f);
				food.put(myFood.food.type, myFood);
			}
			currentAmount++;
		}

		public void removeItem(Food f){
			MyFood myFood;
			if(food.containsKey(f.type)){
				myFood= food.get(f.type);
				myFood.currentAmount -= 1;
				food.put(myFood.food.type, myFood);
			}
			currentAmount--;
		}

		public Boolean contains(String type){
			return food.containsKey(type);
		}
	}

	private class MyFood{
		Food food;
		int currentAmount;
		
		MyFood(Food f){
			food= f;
			currentAmount= 0;
		}
		
		MyFood(String type){
			food= new Food(type, "Microwave", 1000); //assigned manually until we decide how to handle this
			currentAmount= 0;
		}
	}
}
