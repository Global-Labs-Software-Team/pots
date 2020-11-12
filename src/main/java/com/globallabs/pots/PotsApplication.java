package com.globallabs.pots;

import java.util.ArrayList;
import java.util.List;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.globallabs.operator.Exchange;
import com.globallabs.phonedata.TelephoneModel;
import com.globallabs.phoneexceptions.BusyPhoneException;
import com.globallabs.phoneexceptions.DialingMySelfException;
import com.globallabs.phoneexceptions.InvalidNumberException;
import com.globallabs.phoneexceptions.NoCommunicationPathException;
import com.globallabs.phoneexceptions.NoIncomingCallsException;
import com.globallabs.phoneexceptions.PhoneExistInNetworkException;
import com.globallabs.phoneexceptions.PhoneNotFoundException;
import com.globallabs.telephone.Telephone;

//@SpringBootApplication
public class PotsApplication {

	public static void main(String[] args) throws PhoneExistInNetworkException, 
					BusyPhoneException, PhoneExistInNetworkException, PhoneNotFoundException, 
					DialingMySelfException, NoCommunicationPathException, NoIncomingCallsException, InvalidNumberException {
		// TODO Auto-generated method stub
		Exchange exchange = new Exchange();
		List<Telephone> telephones = new ArrayList<Telephone>();
				
		// Create phones with the numbers: 0, 1, 2, 3
		System.out.println("Telephones in the network:");
		for (int number = 0; number < 4; number++) {
			Telephone telephone = new Telephone(new TelephoneModel(number), exchange);
			telephones.add(telephone);
			System.out.println(telephone);
		}
		System.out.println();
		// Simulating a call from 0 to 1 and 2 to 3
		telephones.get(0).dial(1);
		System.out.println(telephones.get(0) + " is calling " +telephones.get(1));
				
		// Telephone 1 accept the call of telephone 0
		telephones.get(1).answer();
		System.out.println(telephones.get(0) + " is talking " + telephones.get(1));
				
		// Telephone 3 try to call one when it is busy
		try {
			System.out.println(telephones.get(3) + " will try to call " + telephones.get(1));
			telephones.get(3).dial(1);
		} catch(Exception e) {
			System.out.println(telephones.get(3) +" phone call failed: " + e.getMessage());
		}
	}
}
