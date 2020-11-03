package com.globallabs.pots;

import java.util.ArrayList;
import java.util.List;

import com.globallabs.operator.Exchange;
import com.globallabs.phoneexceptions.BusyPhoneException;
import com.globallabs.phoneexceptions.DialingMySelfException;
import com.globallabs.phoneexceptions.NoIncomingCallsException;
import com.globallabs.phoneexceptions.PhoneExistInNetworkException;
import com.globallabs.phoneexceptions.PhoneNotFoundException;

public class PhoneSimulation {
	
	public static void main(String[] args) throws PhoneExistInNetworkException, DialingMySelfException, 
			NoIncomingCallsException, BusyPhoneException, PhoneNotFoundException {
		// TODO Auto-generated method stub
		Exchange exchange = new Exchange();
		List<TelephoneDevice> telephones = new ArrayList<TelephoneDevice>();
		
		// Create phones with the numbers: 0, 1, 2, 3
		System.out.println("Telephones in the network:");
		for (int number = 0; number < 4; number++) {
			Telephone telephone = new Telephone(number);
			telephones.add(new TelephoneDevice(telephone, exchange));
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
