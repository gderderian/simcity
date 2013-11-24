package restaurant;

public class Table {
	
	int tableNumber;
	public int tableX;
	public int tableY;
	CustomerAgent occupiedBy;
	
	Table(int tableNumber) {
		this.tableNumber = tableNumber;
	}
	
	Table(int tableNumber, int newTableX, int newTableY) {
		this.tableNumber = tableNumber;
		this.tableX = newTableX;
		this.tableY = newTableY;
	}

	// Accessors
	void setOccupant(CustomerAgent cust) {
		occupiedBy = cust;
	}

	void setUnoccupied() {
		occupiedBy = null;
	}

	CustomerAgent getOccupant() {
		return occupiedBy;
	}

	boolean isOccupied() {
		return occupiedBy != null;
	}

	public String toString() {
		return "table " + tableNumber;
	}
	
}