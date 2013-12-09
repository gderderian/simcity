package city;

import interfaces.HouseInterface;

import java.util.Random;
import java.util.TimerTask;

import Role.LandlordRole;
import test.mock.LoggedEvent;

public class Apartment extends House implements HouseInterface{
	LandlordRole landlord;
	int aptNum;
	int aptBuilding;
	
	public Apartment(String name){
		super(name);
	}
	
	//public Apartment(String name, int num, int buildNum){
	public Apartment(String name, int num){
		super(name);
		aptNum= num;
		//aptBuilding= buildNum;
	}
	
	public void setLandlord(LandlordRole r){
		landlord= r;
		System.out.println("I now have a new landlord, joy");
	}
	
	public void setRoom(int num){
		aptNum= num;
	}
	
	public void setBuilding(int num){
		aptBuilding= num;
	}
	
	public int getNum(){
		int temp= aptNum;
		if(aptBuilding == 1){
			temp += 21;
		}
		else if(aptBuilding == 2){
			temp += 41;
		}
		return temp;
	}
	
	public String getName(){
		String temp= "apart1";
		if(aptBuilding == 2){
			temp= "apart2";
		}
		return temp;
	}
	
	public LandlordRole getLandlord(){
		return landlord;
	}
	
	@Override
	public void cookFood(String type){
		log.add(new LoggedEvent("Cooking " + type + "."));
		System.out.println("Apartment trying to cook");
		final MyFood temp= new MyFood(type);
		if(fridge.food.containsKey(temp.food.type)){
			int tempAmount= fridge.food.get(temp.food.type).currentAmount;
			if(tempAmount > 0){
				System.out.println("I have the type of food I'm trying to cook.");
				fridge.removeItem(temp.food);
				int cookTime= temp.food.cookTime;
				final Appliance app= getAppliance(temp.food.appliance);
				if(app.isBroken){
					owner.msgApplianceBrokeCantCook(temp.food.type); 
				}
				else if(app != null){
					System.out.println("The appliance needed exists.");
					cook.schedule(new TimerTask() {
						@Override public void run() {
							System.out.println("Food is done cooking!");
							owner.msgFoodDone(temp.food.type);
				
							Random rand = new Random();
							int num= rand.nextInt(10);
							if(num == 0 || owner.getName().equals("brokenApplianceTest")){ //there is a 1/10 chance the appliance will break each time it is used
							System.out.println("The appliance broke!");
								app.isBroken= true;
								owner.msgImBroken(app.type);
							}
							System.out.println("Done with the cookFood function.");
							return;
						}}, cookTime);
				}
			}
		}
	}
}
