package city;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import Role.BankTellerRole;
import Role.BankManagerRole;

public class Bank {

	
        BankManagerRole bankmanager;
        public List<account> accounts = Collections.synchronizedList(new ArrayList<account>());
        public static List<bankstation> bankstations = Collections.synchronizedList(new ArrayList<bankstation>());
        public static int uniqueaccountnumber = 1;
        int initialxcofbank = 200;
        int initialycofbank = 100;
        public double bankasset;
        private boolean isOpen;
        
        
        public Bank() {
                
                accounts = new ArrayList<account>();
                bankasset = 1000;
                isOpen = true;
                //bankmanager = new BankManagerRole(this);
                bankstations = new ArrayList<bankstation>();
                for(int i = 0; i < 6; i++)
                {
                	bankstations.add(new bankstation(null, i+1, initialxcofbank += 100, initialycofbank));
                }
                
                //this is for testing
                accounts.add(new account (null, 1));
                accounts.get(0).balance = 10;
                accounts.get(0).loan = 0;
                
                accounts.add(new account (null, 2));
                accounts.get(1).balance = 20;
                accounts.get(1).loan = 30;
                
                accounts.add(new account (null, 3));
                accounts.get(2).balance = 5;
                accounts.get(2).loan = 50;
                
                accounts.add(new account (null, 4));
                accounts.get(3).balance = 10;
                accounts.get(3).loan = 5;
                
                
        }
        
        public void resetBankAsset(double reset) {
        	bankasset += reset;;
        }
        
        public BankManagerRole getBankManager()
        {
               if(isOpen == false)
               {
            	   return null;
               }
               else
               {
        		return bankmanager;
               }
        }
        
        public boolean isOpen(){
    		return isOpen;
    	}
        
        public void toggleOpen() {
    		if(isOpen == true){
    			isOpen = false;
    		} else {
    			isOpen = true;
    		}
    	}
        
        public void setBankManager(BankManagerRole bm)
        {
        	bankmanager = bm;
        }
        
        public void addBankTeller(BankTellerRole btr)
        {
        	bankmanager.msgBankTellerArrivedAtBank(btr);
        }
              
        public class bankstation {
        	
        	public int stationnumber;
        	public BankTellerRole bankteller;
        	int xcoordinate;
        	int ycoordinate;
        	
        	public bankstation(BankTellerRole setbankteller, int setstationnumber, int setxcoordinate, int setycoordinate)
        	{
        		bankteller = setbankteller;
        		stationnumber = setstationnumber;
        		xcoordinate = setxcoordinate;
        		ycoordinate = setycoordinate;
        		
        	}
        	
        	public boolean isOccupied()
        	{
        		if(bankteller != null)
        		return true;
        		else
        		return false;
        		
        	}
        	
        	public void setBankTeller(BankTellerRole setbankteller)
        	{
        		bankteller = setbankteller;
        	}
        	
        	
        	
        }

}


