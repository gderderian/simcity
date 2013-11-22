package interfaces;

import Role.BankCustomerRole;
import test.mock.LoggedEvent;

public interface BankRole {
	
	public void msgAssignMeCustomer(BankCustomerRole customer);
	
	public void msgOpenAccount();

	public void msgDepositIntoAccount(double deposit);

	public void msgWithdrawFromAccount(double withdrawal);

	public void msgGetLoan(double loan);
	
	public void msgPayBackLoan(double paybackloan);
	
}
