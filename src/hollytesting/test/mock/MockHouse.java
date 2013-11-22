package hollytesting.test.mock;

import java.util.List;

import test.mock.EventLog;
import test.mock.LoggedEvent;
import city.Food;
import city.PersonAgent;
import hollytesting.interfaces.House;

public class MockHouse extends Mock implements House{
	
	public EventLog log = new EventLog();
	PersonAgent person;

	public MockHouse(String name, PersonAgent p) {
		super(name);
		person = p;
	}

	public void boughtGroceries(List<Food> groceries) {
		log.add(new LoggedEvent("Recieved grocery list."));
	}

	public void checkFridge(String type) {
		log.add(new LoggedEvent("Recieved message check fridge."));
		person.msgItemInStock(type);
	}

	public void spaceInFridge() {
		// TODO Auto-generated method stub
		
	}

	public void cookFood(String type) {
		person.msgFoodDone(type);
	}

	public void fixedAppliance(String appliance) {
		// TODO Auto-generated method stub
		
	}

}
