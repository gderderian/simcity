package city;

import java.util.List;
import java.util.Collections;
import java.util.Vector;

public class Menu {
	List<Food> options = Collections.synchronizedList(new Vector<Food>());
	
	public Menu(){
		options.add(new Food("Steak", 15.99));
		options.add(new Food("Chicken",  10.99));
		options.add(new Food("Salad", 5.99));
		options.add(new Food("Pizza", 8.99));
	}
	
	Menu(String remove){
		if(remove != "Steak"){
			options.add(new Food("Steak", 15.99));
		}
		if(remove != "Chicken"){
			options.add(new Food("Chicken", 10.99));
		}
		if(remove != "Salad"){
			options.add(new Food("Salad", 5.99));
		}
		if(remove != "Pizza"){
			options.add(new Food("Pizza", 8.99));
		}
	}
	public Menu remove(String food){
		synchronized(options){
			for(Food f : options){
				if(f.name == food){
					options.remove(f);
				}
			}
		}
		return this;
	}
	
	public String chooseFood(){
		int y = (int)Math.round(Math.random() * (options.size()-1));
		return options.get(y).name;
	}
	
	public double getPrice(String food){
		synchronized(options){
			for(Food f : options){
				if(f.name == food){
					return f.price;
				}
			}
		}
		return 0;
	}
	
	
	private class Food{
		String name;
		double price;
		
		public Food(String n, double p){
			name = n;
			price = p;
		}
	}

	public boolean doIHaveEnoughFor(double wallet, String choice) {
		synchronized(options){
			for(Food f : options){
				if(wallet < f.price){
					return false;
				}
			}
		}
		return true;
	}
	
	public boolean doIHaveEnough(double wallet) {
		int notEnough = 0;
		synchronized(options){
			for(Food f : options){
				if(wallet < f.price){
					notEnough++;
				}
			}
		}
		if(notEnough == options.size()){
			return false;
		}
		return true;
	}
	
}
