package interfaces;

import city.PersonAgent;

public interface Landlord {

		public abstract void msgEndOfDay();
		
		public abstract void msgHereIsMyRent(PersonAgent p, double rate);
		
		public abstract void msgFixAppliance(PersonAgent p, String appliance);
	
}
