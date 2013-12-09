package city;

import java.util.*;
import Role.LandlordRole;

public class ApartmentBuilding {
	LandlordRole landlord;
	List<PersonAgent> tenants= new ArrayList<PersonAgent>();
	
	public void setLandlord(LandlordRole r){
		landlord= r;
		if(tenants != null){
			for(int i=0; i<tenants.size(); i++){
				tenants.get(i).house.setLandlord(r);
				landlord.addTenant(tenants.get(i));
			}
		}
	}
	
	public LandlordRole getLandlord(){
		return landlord;
	}
	
	public void addTenant(PersonAgent p){
		landlord.addTenant(p);
		tenants.add(p);
		p.house.setLandlord(landlord);
	}
}
