package city.Restaurant4;

import Role.Role;
import city.gui.restaurant4.HostGui4;

import java.util.*;

import justinetesting.interfaces.Customer4;
import justinetesting.interfaces.Waiter4;

/**
 * Restaurant Host Agent
 */
public class HostRole4 extends Role {
	static final int NTABLES = 4;//a global for the number of tables.
	public List<Customer4> waitingToSit = Collections.synchronizedList(new ArrayList<Customer4>());
	public List<Customer4> line = Collections.synchronizedList(new ArrayList<Customer4>());
	public List<MyWaiter> waiters;
	public enum WaiterState {goingOnBreak, onBreak, denied, reset};
	public Collection<Table> tables;
	private String name;
	public HostGui4 hostGui = null;
	private boolean closed= false;

	public HostRole4(String name) {
		super();
		this.name = name;
		
		// make the tables
		tables = new ArrayList<Table>(NTABLES);
		for (int ix = 1; ix <= NTABLES; ix++) {
			tables.add(new Table(ix));//how you add to a collections
		}
		// make the waiters
		waiters= Collections.synchronizedList(new ArrayList<MyWaiter>());
	}

	public String getMaitreDName() {
		return name;
	}

	public String getName() {
		return name;
	}

	public List<Customer4> getWaitingCustomers() {
		return waitingToSit;
	}

	public Collection<Table> getTables() {
		return tables;
	}
	
	public void addWaiter(Waiter4 waiter){
		waiters.add(new MyWaiter(waiter));
		waiter.msgNumber(waiters.size());
		stateChanged();
	}
	
	
	// MESSAGES
	public void msgIWantFood(Customer4 c) {
		waitingToSit.add(c);
		line.add(c);
		stateChanged();
	}

	public void msgSeatingCustomer(Customer4 c){
		line.remove(c);
		stateChanged();
	}
	
	public void msgLeavingRest(Customer4 c){
		synchronized(waitingToSit){
			for(Customer4 cust : waitingToSit){
				if(cust == c){
					waitingToSit.remove(cust);
					line.remove(cust);
					stateChanged();
					return;
				}
			}
		}
	}
	
	public void msgTableAvaliable(Customer4 c){
		for (Table table : tables) {
			if (table.getOccupant() == c) {
				table.setUnoccupied();
				stateChanged();
			}
		}
		for (MyWaiter waiter : waiters){
			if(c.getWaiter() == waiter.w && waiter.numCustomers != 0){
				waiter.numCustomers--;
			}
		}
		stateChanged();
	}

	public void msgWantToGoOnBreak(Waiter4 w){
		print("A waiter wants to take a break, let me see if I should accept or deny...");
		MyWaiter waiter= find(w);
		boolean okay= true;
		if(waiters.size() > 1){
			for(MyWaiter wait : waiters){
				if(wait.ws == WaiterState.onBreak){
					okay= false;
				}
			}
			if(okay){
				waiter.ws= WaiterState.goingOnBreak;
				stateChanged();
				return;
			}
		}
		waiter.ws= WaiterState.denied;
		stateChanged();
	}
	
	public void msgReadyToWork(Waiter4 w){
		print("Oh good, a waiter is coming off break");
		MyWaiter waiter= find(w);
		waiter.ws= WaiterState.reset;
		stateChanged();
	}
	
	public void msgRestClosed(){
		closed= true;
		stateChanged();
	}
	
	
	/**
	 * Scheduler.  Determine what action is called for, and do it.
	 */
	protected boolean pickAndExecuteAnAction() {
		if(closed){
			return false;
		}
		synchronized(line){
			for(int i=0; i<line.size(); i++){
				tellCustomer(i);
			}
		}
		for (Table table : tables) {
			if (!table.isOccupied()) {
				synchronized(waitingToSit){
					if (!waitingToSit.isEmpty() && !waiters.isEmpty()) {
						print("Please seat this waiting customer!");
						synchronized(waitingToSit){
							delegateToWaiter(waitingToSit.get(0), table);//the action
						}
						return true;
					}
				}
			}
		}
		synchronized(waitingToSit){
			if(!waitingToSit.isEmpty()){
				synchronized(waitingToSit){
					for(Customer4 c : waitingToSit){
						c.msgRestaurantFull();
					}
				}
			}
		}
		synchronized(waiters){
			for(MyWaiter waiter : waiters){
				if(waiter.ws == WaiterState.goingOnBreak){
					approveBreak(waiter);
					return true;
				}
				else if(waiter.ws == WaiterState.denied){
					denyBreak(waiter);
					return true;
				}
			}
		}
		return false;
	}

	// ACTIONS
	private void tellCustomer(int i){
		line.get(i).msgPositionInLine(i);
	}
	
	private void delegateToWaiter(Customer4 customer4, Table table){
		MyWaiter mw= findLeastBusyWaiter(); 
		int tableNum= table.getTableNumber();
		mw.w.msgPleaseSeatCustomer(customer4, tableNum);
		mw.numCustomers++; 
		table.setOccupant(customer4);
		waitingToSit.remove(customer4);
	}

	private MyWaiter findLeastBusyWaiter(){
		int numCust= 100;
		MyWaiter seatCust= waiters.get(0);        // initializing the assignment to the first waiter to be updated in loop below
		for(int i=0; i<waiters.size(); i++){
			if(waiters.get(i).ws.equals(WaiterState.onBreak) && (waiters.size() >= (i+1))){
				seatCust= waiters.get(i+1);
			}
			else if((waiters.get(i).numCustomers < numCust)){
				print("WaiterState: " + waiters.get(i).ws);
				numCust= waiters.get(i).numCustomers;
				seatCust= waiters.get(i);
			}
		}
		return seatCust;
	}
	
	private MyWaiter find(Waiter4 w){
		MyWaiter temp= waiters.get(0);  // initialize to the first waiter, will be updated in loop below
		for(MyWaiter waiter : waiters){
			if( w.equals(waiter.w) ){
				return temp;
			}
		}
		return temp; 
	}
	
	public void approveBreak(MyWaiter w){
		print("Yes of course you can take a break, good job today!");
		w.ws= WaiterState.onBreak;
		w.w.msgBreakApproved();
		stateChanged();
	}
	
	public void denyBreak(MyWaiter w){
		print("Sorry, you have to keep working, ask for a break again later!");
		w.w.msgBreakDenied();
		w.ws= WaiterState.reset;
		stateChanged();
	}
	
	// UTILITIES
	public void setGui(HostGui4 gui) {
		hostGui = gui;
	}

	public HostGui4 getGui() {
		return hostGui;
	}

	// CLASSES
	private class MyWaiter {
		Waiter4 w;
		WaiterState ws= WaiterState.reset;
		int numCustomers= 0;  // each waiter starts with zero customers
		
		MyWaiter(Waiter4 waiter){
			this.w= waiter;
		}
	}
	
	private class Table {
		Customer4 occupiedBy;
		int tableNumber;

		Table(int tableNumber) {
			this.tableNumber = tableNumber;
		}

		void setOccupant(Customer4 customer4) {
			occupiedBy = customer4;
		}

		void setUnoccupied() {
			occupiedBy = null;
		}

		Customer4 getOccupant() {
			return occupiedBy;
		}

		boolean isOccupied() {
			return occupiedBy != null;
		}

		public String toString() {
			return "table " + tableNumber;
		}
		
		public int getTableNumber(){
			return tableNumber;
		}
	}
}

