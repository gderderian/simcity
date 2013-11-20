package city;

public class Apartment extends HouseAgent {
	PersonAgent landlord;
	int aptNum;
	
	public Apartment(){
		super();
	}
	
	public Apartment(int num){
		super();
		aptNum= num;
	}
	
	public void setNum(int num){
		this.aptNum= num;
	}
}
