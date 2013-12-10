package interfaces;

import city.MarketOrder;
import city.PersonAgent;
import city.gui.Market.MarketCustomerGui;

public interface MarketCustomer {

	// Messages
	public abstract void msgHereIsYourOrder(MarketOrder o);

	public abstract void releaseSemaphore();

	public abstract String getRoleName();

	public abstract void setGuiActive();

	public abstract void setGui(MarketCustomerGui gui);

	public abstract MarketCustomerGui getGui();

	public abstract void setPerson(PersonAgent p);

	public abstract PersonAgent getPerson();

}