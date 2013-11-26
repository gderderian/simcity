package city;

import interfaces.Person;

import java.util.*;

import city.gui.House.HouseAnimationPanel;
import test.mock.*;

public class House {
	//DATA
	public Person owner;
	String houseName;
	HouseAnimationPanel h;
	protected Timer cook= new Timer();
	public Appliance fridge= new Appliance("Fridge");
	private Appliance microwave= new Appliance("Microwave");
	private Appliance oven= new Appliance("Oven");
	private Appliance stove= new Appliance("Stove");
	public List<Appliance> cookingAppliances= new ArrayList<Appliance>(); 
	public EventLog log= new EventLog();

	
	//CONSTRUCTOR
	public House(String name){
		super();
		
		this.houseName = name;
		
		cookingAppliances.add(microwave);
		cookingAppliances.add(oven);
		cookingAppliances.add(stove);
	}
	
	
	//SETTERS/GETTERS
	public void setOwner(Person p){
		owner= p;
		//owner= new HomeOwnerRole(this);
		//owner.addRole(homeowner, true);
	}
	
	public void setHouseAnimationPanel(HouseAnimationPanel p){
		h= p;
	}
	
	
	//PUBLIC METHODS
	public void boughtGroceries(List<Food> groceries){
		log.add(new LoggedEvent("Received boughtGroceries from person, fridge should now have " + groceries.size() + " items."));
		System.out.println("Received boughtGroceries from person, fridge should now have " + groceries.size() + " items.");
		if(groceries.size() <= (fridge.capacity - fridge.currentAmount)){
			owner.msgFridgeFull();
		}
		for(int i=0; (i<groceries.size()) && (fridge.currentAmount < fridge.capacity); i++){
			fridge.addItem(groceries.get(i));
		}
	}

	public void checkFridge(String type){
		log.add(new LoggedEvent("Recieved checkFridge from person, checking if there is any " + type + " in the fridge."));
		System.out.println("Recieved checkFridge from person, checking if there is any " + type + " in the fridge.");
		MyFood temp= new MyFood(type);
		if(fridge.food.containsKey(temp.food.type)){
			int tempAmount= fridge.food.get(temp.food.type).currentAmount;
			System.out.println("tempAmount: " + tempAmount);
			if(tempAmount > 0){
				owner.msgItemInStock(temp.food.type); 
				return;
			}
		}
		owner.msgDontHaveItem(temp.food.type);	
	}
	
	public void spaceInFridge(){
		log.add(new LoggedEvent("Checking to see if there is any space left in the fridge."));
		int spaceLeft= fridge.capacity - fridge.currentAmount;
		owner.msgSpaceInFridge(spaceLeft);
	}

	public void cookFood(String type){
		log.add(new LoggedEvent("Cooking " + type + "."));
		final MyFood temp= new MyFood(type);
		if(fridge.food.containsKey(temp.food.type)){
			int tempAmount= fridge.food.get(temp.food.type).currentAmount;
			if(tempAmount > 0){
				System.out.println("I have the type of food I'm trying to cook.");
				fridge.removeItem(temp.food);
				int cookTime= temp.food.cookTime;
				final Appliance app= getAppliance(temp.food.appliance);
				if(app != null){
					System.out.println("The appliance needed exists.");
					cook.schedule(new TimerTask() {
						@Override public void run() {
							System.out.println("Food is done cooking!");
							owner.msgFoodDone(temp.food.type);
							System.out.println("Done with the cookFood function.");
							return;
						}}, cookTime);
				}
			}
		}
	}

	public void fixedAppliance(String appliance){
		log.add(new LoggedEvent("Recieved fixedAppliance from person, " + appliance + " is now fixed."));
		for(Appliance a : cookingAppliances){
			if(a.type.equals(appliance)){
				a.isBroken= false;
			}
		}
	}

	
	//INTERNAL METHODS
	protected Appliance getAppliance(String type){
		for(Appliance a : cookingAppliances){
			if(a.type.equals(type)){
				return a;
			}
		}
		return null;
	}
	
	public String getName() {
		return houseName;
	}
	
	
	//CLASSES
	public class Appliance{
		public String type;
		int capacity; 
		public int currentAmount;
		Map<String, MyFood> food= new HashMap<String, MyFood>(); 
		public Boolean isBroken= false;

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
				System.out.println("Adding an item I've had before to fridge...");
				myFood= food.get(f.type);
				myFood.currentAmount += 1;
				System.out.println("Current amount of " + myFood.food.type + ": " + myFood.currentAmount);
				food.put(myFood.food.type, myFood);
			}
			else{
				System.out.println("Adding a new item I've never had before to fridge...");
				myFood= new MyFood(f);
				myFood.currentAmount += 1;
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

	class MyFood{
		Food food;
		int currentAmount;
		
		MyFood(Food f){
			food= f;
			currentAmount= 0;
		}
		
		MyFood(String type){
			food= new Food(type); //assigned manually until we decide how to handle this
			currentAmount= 0;
		}
	}
}
