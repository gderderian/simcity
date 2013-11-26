package city.gui;

import javax.swing.JPanel;

public abstract class BuildingPanel extends JPanel {
	protected CityGui cityGui;
	
	public void setCityGui(CityGui g) {
		cityGui = g;
	}
	
	protected void changeBackToCity() {
		cityGui.changeView("City");
	}
	
	public abstract void addGui(Gui g);
	
	public abstract void updatePos();
}