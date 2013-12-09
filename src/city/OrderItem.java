package city;

public class OrderItem {
	public String type;	//i.e. car or food
	public String name; 	//i.e. Chicken
	public int quantity;
	
	public OrderItem(String n, int q){
		name = n;
		quantity = q;
		type = "food";
	}

	public int getQuantity() {
		return quantity;
	}
	public String getType() {
		return type;
	}
	public String getName() {
		return name;
	}
}