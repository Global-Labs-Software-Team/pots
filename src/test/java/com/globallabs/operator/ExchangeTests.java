package com.globallabs.operator;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.globallabs.phonedata.TelephoneModel;
import com.globallabs.phoneexceptions.BusyPhoneException;
import com.globallabs.phoneexceptions.NoCommunicationPathException;
import com.globallabs.phoneexceptions.PhoneExistInNetworkException;
import com.globallabs.phoneexceptions.PhoneNotFoundException;
import com.globallabs.telephone.Status;
import com.globallabs.telephone.Telephone;

public class ExchangeTests {
	
	private Exchange exchange;
	private Telephone telephone;
	private Telephone telephoneTwo;
	
	@BeforeEach
	public void setUp() throws PhoneExistInNetworkException {
		exchange = new Exchange();
		telephone = new Telephone(new TelephoneModel(1), exchange);
		telephoneTwo = new Telephone(new TelephoneModel(2), exchange);
	}
	
	@Test
	void test_constructor() {
		Exchange exchange = new Exchange();
		assertEquals(0, exchange.getNumberOfPhones());
	}
	
	/**
	 * Test that a phone is added successfully
	 * @throws PhoneExistInNetworkException
	 */
	@Test
	void test_addPhoneToExchange_success() throws PhoneExistInNetworkException {
		Exchange exchange = new Exchange();
		new Telephone(new TelephoneModel(1), exchange);
		assertEquals(1, exchange.getNumberOfPhones());
	}
	
	/**
	 * Test that a same phone is not added two times
	 * in the exchange
	 */
	@Test
	void test_addPhoneToExchange_phoneExists() throws PhoneExistInNetworkException {
		Exchange exchange = new Exchange();
		Telephone telephone = new Telephone(new TelephoneModel(1), exchange);
		
		assertThrows(PhoneExistInNetworkException.class, () -> {exchange.addPhoneToExchange(telephone);});
	}
	
	/**
	 * Exchange try to create a path between telephone two and telephone one with two as origin.
	 * Because one is in "OFF_CALL" then a path can be establish and the status of one is
	 * updated to RINGING and the telephone two is set as an Incoming Call of phone one.
	 */
	@Test 
	void test_enrouteCall_success() throws BusyPhoneException, PhoneNotFoundException, PhoneExistInNetworkException {
		exchange.enrouteCall(2, 1); // Telephone two decides to call telephone one
		assertEquals(Status.RINGING, telephone.getStatus()); // The telephone one receive the notification
		assertEquals(telephoneTwo, telephone.getIncomingCall());
	}
	
	/**
	 * Exchange try to create a path between telephone two and one with two as origin
	 * of the call. Because one is in a call then the path cannot be created and a
	 * BusyPhoneException is thrown.
	 */
	@Test
	void test_enrouteCall_when_busy() throws PhoneExistInNetworkException {
		telephone.setStatus(Status.BUSY);

		assertThrows(BusyPhoneException.class, () -> {exchange.enrouteCall(2, 1);});
	}
	
	/**
	 * The exchange try to create a path between the telephone two and one
	 * but one does not exist in the network. A PhoneNotFoundExcetion is
	 * thrown.
	 */
	@Test
	void test_enrouteCall_when_phone_no_exist() {
		Exchange exchange = new Exchange();
		assertThrows(PhoneNotFoundException.class, () -> {exchange.enrouteCall(2, 1);});
	}
	
	/**
	 * The exchange has a path between the telephone one and telephone two because
	 * telephone two is calling telephone one. The exchange will open the communication updating the status
	 * of telephone one to BUSY.
	 * @throws PhoneExistInNetworkException
	 */
	@Test
	void test_openCall_with_incomingCall() throws PhoneExistInNetworkException, NoCommunicationPathException {
		
		// Two is calling one
		telephoneTwo.setLastCall(telephone); 
		telephone.setStatus(Status.DIALING);
		
		// Telephone one is receiving a call from telephone two
		telephone.setStatus(Status.RINGING);
		telephone.setIncomingCall(telephoneTwo);
		
		//  Telephone one decides to accept the call
		telephone.setStatus(Status.BUSY);
		exchange.openCallBetween(1, 2); // One sends signal to open the call to two
		
		// Then telephone two get the status busy also
		assertEquals(Status.BUSY, telephoneTwo.getStatus());
	}
	
	/**
	 * Exchange try to open a communication path that does not exists. For example
	 * this can happen when telephone one try to open a call with two but two is
	 * not calling him.
	 * @throws PhoneExistInNetworkException
	 */
	@Test
	void test_openCall_without_incomingCall() {
		assertThrows(NoCommunicationPathException.class, () -> {exchange.openCallBetween(1, 2);});
	}
	
	/**
	 * Exchange close the call between one and two when one decided
	 * to close it
	 */
	@Test
	void test_closeCall_successfully() throws NoCommunicationPathException {
		// Set up of the scenario where telephoneOne is in a call with telephoneTwo
		telephone.setLastCall(telephoneTwo);
		telephone.setStatus(Status.BUSY);
		
		telephoneTwo.setLastCall(telephone);
		telephoneTwo.setStatus(Status.BUSY);
		
		// telephoneOne close the call
		exchange.closeCallBetween(1, 2);
		telephone.setStatus(Status.OFF_CALL);
		
		// Verification of status
		assertEquals(Status.OFF_CALL, telephoneTwo.getStatus());
	}
	
	/**
	 * Exchange close the communication when a communication path
	 * is not open. This mean that telephone1 is dialing telephone2
	 * and before telephone2 responds, telephone1 cut the communication
	 */
	@Test
	void test_closeCall_when_a_communication_is_not_open() throws NoCommunicationPathException {
		// Set up of the scenario
		telephone.setLastCall(telephoneTwo);
		telephone.setStatus(Status.DIALING);
		
		telephoneTwo.setIncomingCall(telephone);
		telephoneTwo.setStatus(Status.RINGING);
		
		// TelephoneOne cancel the call
		exchange.closeCallBetween(1, 2);
		telephone.setStatus(Status.OFF_CALL);
		
		// Verification of status
		assertEquals(Status.OFF_CALL, telephoneTwo.getStatus());
		assertEquals(null, telephoneTwo.getIncomingCall());
	}
	
	/**
	 * Exchange try to close a communication path between one and two
	 * but there is no path there. So NoCommunicationPathException is thrown
	 */
	@Test
	void test_closeCall_when_a_communication_path_does_not_exist() throws PhoneExistInNetworkException {
		Telephone telephoneThree = new Telephone(new TelephoneModel(3), exchange);

		// Set up of the scenario: One is in a call with three
		telephone.setLastCall(telephoneThree);
		telephone.setStatus(Status.BUSY);
		
		telephoneThree.setLastCall(telephone);
		telephoneThree.setStatus(Status.BUSY);
		
		// Exchange try to cancel a call between 1 and 2 but there is no connection
		assertThrows(NoCommunicationPathException.class, () -> {exchange.closeCallBetween(2, 1);});
	}
}
