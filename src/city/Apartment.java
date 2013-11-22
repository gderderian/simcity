package city;

public class Apartment extends House {
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
