package interfaces;

import java.util.List;
import java.util.Random;
import java.util.TimerTask;

import city.Food;

public interface House {
	
	public void boughtGroceries(List<Food> groceries);

	public void checkFridge(String type);
	
	public void spaceInFridge();

	public void cookFood(String type);

	public void fixedAppliance(String appliance);

}
