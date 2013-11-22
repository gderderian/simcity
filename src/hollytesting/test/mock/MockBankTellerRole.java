package hollytesting.test.mock;

import Role.BankCustomerRole;
import test.mock.Mock;
import interfaces.BankRole;

public class MockBankTellerRole extends Mock implements BankRole{

        public MockBankTellerRole(String name) {
                super(name);
        }

        @Override
        public void msgOpenAccount() {
                // TODO Auto-generated method stub
                
        }

        @Override
        public void msgDepositIntoAccount(double deposit) {
                // TODO Auto-generated method stub
                
        }

        @Override
        public void msgWithdrawFromAccount(double withdrawal) {
                // TODO Auto-generated method stub
                
        }

        @Override
        public void msgGetLoan(double loan) {
                // TODO Auto-generated method stub
                
        }

        @Override
        public void msgPayBackLoan(double paybackloan) {
                // TODO Auto-generated method stub
                
        }

		@Override
		public void msgAssignMeCustomer(BankCustomerRole customer) {
			// TODO Auto-generated method stub
			
		}
        
        
        
}