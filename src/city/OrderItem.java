package city;

public class OrderItem {
	public String type;	//i.e. car or food
	public String name; 	//i.e. Chicken
	public int quantity;
	
	public OrderItem(String n, int q){
		name = n;
		quantity = q;
	}

}