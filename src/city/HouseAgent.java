package city;

import java.util.*;

public class HouseAgent {
	//DATA
	PersonAgent owner;
	Timer cook= new Timer();
	Appliance fridge= new Appliance("Fridge");
	Appliance microwave= new Appliance("Microwave");
	Appliance oven= new Appliance("Oven");
	Appliance stove= new Appliance("Stove");
	List<Appliance> cookingAppliances= new ArrayList<Appliance>(); 
	List<MyFood> toCook= Collections.synchronizedList(new ArrayList<MyFood>()); 
	enum CookState{pending, checkedFridge, cooking};

	public HouseAgent(PersonAgent p){
		super();
		
		owner= p;
		cookingAppliances.add(microwave);
		cookingAppliances.add(oven);
		cookingAppliances.add(stove);
	}
	
	
	//MESSAGES
	public void msgBoughtGroceries(List<Food> groceries){
		for(int i=0; (i<groceries.size()) && (fridge.currentAmount < fridge.capacity); i++){
			fridge.addItem(groceries.get(i));
			fridge.currentAmount++;
		}
	}

	public void msgCheckFridge(String type){
		MyFood temp= new MyFood(type);
		temp.cs= CookState.pending;
		toCook.add(temp);
	}

	public void msgCookFood(String type){
		for(MyFood mf : toCook){
			if(mf.food.type.equals(type) && mf.cs.equals(CookState.checkedFridge)){
				mf.cs= CookState.cooking;
			}
		}
	}

	public void msgFixed(String appliance){
		for(Appliance a : cookingAppliances){
			if(a.type.equals(appliance)){
				a.isBroken= false;
			}
		}
	}

	
	//SCHEDULER
	protected Boolean pickAndExecuteAnAction(){
		for(Appliance a : cookingAppliances){
			if(a.isBroken){
				applianceBroke(a);
				return true;
			}
		}
		synchronized(toCook){
			for(MyFood mf : toCook){
				if(mf.cs.equals(CookState.pending)){
					checkFridge(mf);
					return true;
				}
			}
		}
		synchronized(toCook){
			for(MyFood mf : toCook){
				if(mf.cs.equals(CookState.cooking)){
					cookFood(mf);
					return true;
				}
			}
		}
		return false;
	}

	
	//ACTIONS
	private Appliance getAppliance(String type){
		for(Appliance a : cookingAppliances){
			if(a.type.equals(type)){
				return a;
			}
		}
		return null;
	}
	
	public void applianceBroke(Appliance a){
		owner.msgImBroken(a.type);
	}

	public void checkFridge(MyFood mf){
		mf.cs= CookState.checkedFridge;
		if(fridge.food.containsKey(mf.food.type)){
			int tempAmount= fridge.food.get(mf.food.type).currentAmount;
			if(tempAmount > 0){
				owner.msgItemInStock(mf.food.type); 
				return;
			}
		}
		owner.msgDontHaveItem(mf.food.type);
	}

	public void cookFood(final MyFood mf){
		fridge.removeItem(mf.food);
		int cookTime= mf.food.cookTime;
		Appliance app= getAppliance(mf.food.appliance);
		if(app != null && app.currentAmount < app.capacity){
			cook.schedule(new TimerTask() {
				@Override public void run() {
					owner.msgFoodDone(mf.food.type);
					toCook.remove(mf);
				}}, cookTime);
			Random rand = new Random();
			int num= rand.nextInt(10);
			if(num == 0){ //there is a 1/10 chance the appliance will break each time it is used
				app.isBroken= true;
			}
			return;
		}
		//owner.msgCantCookNoSpace(); //what if the appliance needed is currently full? maybe this wont happen because only one person lives in the house
	}
	
	
	//CLASSES
	public class Appliance{
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

	public class MyFood{
		Food food;
		int currentAmount;
		CookState cs;
		
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
