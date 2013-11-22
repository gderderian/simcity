package hollytesting.test;

import junit.framework.TestCase;
import hollytesting.test.mock.MockBankTellerRole;
import hollytesting.test.mock.MockHouse;
import city.PersonAgent;

public class PersonAgentBankTest extends TestCase{
	
	PersonAgent person;
	MockBankTellerRole bankTeller;
	
	public void setUp() throws Exception{
		super.setUp();		
		person = new PersonAgent("Person");
		bankTeller = new MockBankTellerRole("BankTeller");
	}
	
	public void testOpenAccountNormal(){
		
	}

}
