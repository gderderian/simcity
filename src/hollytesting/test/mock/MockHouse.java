package hollytesting.test.mock;

import java.util.List;

import city.Food;
import hollytesting.interfaces.House;

public class MockHouse extends Mock implements House{

	public MockHouse(String name) {
		super(name);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void boughtGroceries(List<Food> groceries) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void checkFridge(String type) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void spaceInFridge() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void cookFood(String type) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void fixedAppliance(String appliance) {
		// TODO Auto-generated method stub
		
	}

}
