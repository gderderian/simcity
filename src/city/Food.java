package city;

import java.util.*;

public class Food {
        String type;
        String appliance;
        public int cookTime;
        Map<String, MyFood> allFoodTypes= new HashMap<String, MyFood>();
        MyFood eggs= new MyFood("Stove", 1000);
        MyFood pancakes= new MyFood("Stove", 1250);
        MyFood waffels= new MyFood("Stove", 1250);
        MyFood bacon= new MyFood("Stove", 1000);
        MyFood steak= new MyFood("Microwave", 1750);
        MyFood chicken= new MyFood("Oven", 1250);
        MyFood pizza= new MyFood("Microwave", 1000);
        MyFood salad= new MyFood("Microwave", 1000);
        
        public Food(String type, String appliance, int time){
                this.type= type;
                this.appliance= appliance;
                this.cookTime= time;
        }
        
        public Food(String type){
                allFoodTypes.put("Eggs", eggs);
                allFoodTypes.put("Pacakes", pancakes);
                allFoodTypes.put("Waffels", waffels);
                allFoodTypes.put("Bacon", bacon);
                allFoodTypes.put("Steak", steak);
                allFoodTypes.put("Chicken", chicken);
                allFoodTypes.put("Pizza", pizza);
                allFoodTypes.put("Salad", salad);
                
                this.type= type;
                MyFood temp= allFoodTypes.get(type);
                this.appliance= temp.appliance;
                this.cookTime= temp.cookTime;
        }
        
        private class MyFood{
                String appliance;
                int cookTime;
                
                MyFood(String type,  int time){
                        this.appliance= type;
                        this.cookTime= time;
                }
        }
}