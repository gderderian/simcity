package interfaces;

public interface Landlord {

		public abstract void msgEndOfDay();
		
		public abstract void msgHereIsMyRent(Person p, double rate);
		
		public abstract void msgFixAppliance(Person p, String appliance);
	
}
