package interfaces;

import java.util.List;

import Role.LandlordRole;
import city.Food;
import city.gui.House.HouseAnimationPanel;

public interface HouseInterface {

	//SETTERS/GETTERS
	public abstract void setOwner(Person p);

	public abstract void setHouseAnimationPanel(HouseAnimationPanel p);
	
	public abstract void setLandlord(LandlordRole r);
	
	public abstract LandlordRole getLandlord();
	
	public abstract int getNum();
	
	public abstract String getName();

	//PUBLIC METHODS
	public abstract void boughtGroceries(List<Food> groceries);

	public abstract void checkFridge(String type);

	public abstract void spaceInFridge();

	public abstract void cookFood(String type);

	public abstract void fixedAppliance(String appliance);

	public abstract HouseAnimationPanel getAnimationPanel();

}