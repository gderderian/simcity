package city;

public class Food {
	String type;
	String appliance;
	int cookTime;
	
	Food(String type, String appliance, int time){
		this.type= type;
		this.appliance= appliance;
		this.cookTime= time;
	}
	
	Food(String type){
		this.type= type;
		// ideally the food class will know which appliance and cookTime is appropriate based on the type so we don't need to make the arguments
		// maybe eventually we'll use a map to do this, but for now the first constructor will work where everything is done manually
	}
}
