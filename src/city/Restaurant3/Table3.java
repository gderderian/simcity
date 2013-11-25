package city.Restaurant3;

public class Table3 {
	
	int tableNumber;
	public int tableX;
	public int tableY;
	CustomerRole3 occupiedBy;
	
	Table3(int tableNumber) {
		this.tableNumber = tableNumber;
	}
	
	Table3(int tableNumber, int newTableX, int newTableY) {
		this.tableNumber = tableNumber;
		this.tableX = newTableX;
		this.tableY = newTableY;
	}

	// Accessors
	void setOccupant(CustomerRole3 cust) {
		occupiedBy = cust;
	}

	void setUnoccupied() {
		occupiedBy = null;
	}

	CustomerRole3 getOccupant() {
		return occupiedBy;
	}

	boolean isOccupied() {
		return occupiedBy != null;
	}

	public String toString() {
		return "table " + tableNumber;
	}
	
}