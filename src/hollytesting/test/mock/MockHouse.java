package hollytesting.test.mock;

import java.util.List;

import test.mock.EventLog;
import test.mock.LoggedEvent;
import city.Food;
import hollytesting.interfaces.House;

public class MockHouse extends Mock implements House{
	
	public EventLog log = new EventLog();

	public MockHouse(String name) {
		super(name);
		// TODO Auto-generated constructor stub
	}

	public void boughtGroceries(List<Food> groceries) {
		log.add(new LoggedEvent("Recieved grocery list."));
	}

	public void checkFridge(String type) {
		// TODO Auto-generated method stub
		
	}

	public void spaceInFridge() {
		// TODO Auto-generated method stub
		
	}

	public void cookFood(String type) {
		// TODO Auto-generated method stub
		
	}

	public void fixedAppliance(String appliance) {
		// TODO Auto-generated method stub
		
	}

}
