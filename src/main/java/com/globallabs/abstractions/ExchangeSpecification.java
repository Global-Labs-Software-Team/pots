package com.globallabs.abstractions;

import com.globallabs.phoneexceptions.BusyPhoneException;
import com.globallabs.phoneexceptions.NoCommunicationPathException;
import com.globallabs.phoneexceptions.PhoneExistInNetworkException;
import com.globallabs.phoneexceptions.PhoneNotFoundException;
import com.globallabs.pots.Telephone;

public interface ExchangeSpecification {
	
	/**
	 * Route the call that a phone is requesting
	 * @param phoneNumberFrom The telephone were the call is coming
	 * @param phoneNumber The destination telephone
	 * @return The status of the other phone
	 * @throws BusyPhoneException if the other phone is in a call
	 * @throws PhoneNotFoundException if the other phone does not belong to the network
	 */
	public void enrouteCall(final int phoneNumberFrom, final int phoneNumber) throws BusyPhoneException, PhoneNotFoundException;
	
	/**
	 * Close the communication channel with the given phone
	 * number. This update the status of the other phone
	 * to OFF_CALL
	 * @param theOtherNumberInCall the phoneNumber that you want to close the communication with
	 * @param numberWhoCloseCall the phone where the closeCall signal comes from
	 */
	public void closeCallBetween(final int numberWhoCloseCall, final int theOtherNumberInCall) throws NoCommunicationPathException;
	
	/**
	 * Open the communication channel with the given phone.
	 * This update the status of the other phone to BUSY.
	 * @param callingNumber the phone number you one accept its call
	 * @param receiverNumber the phone where the openCall signal comes from
	 * @throws NoCommunicationPathException when a path from phoneNumberFrom and phoneNumber does not exist
	 */
	public void openCallBetween(final int receiverNumber, final int callingNumber) throws NoCommunicationPathException;
	
	/**
	 * Add a phone to the network
	 * @param phone
	 * @throws PhoneExistInNetworkException if the phone is already inside the network
	 */
	public void addPhoneToExchange(final Telephone phone) throws PhoneExistInNetworkException;
	
	/**
	 * Getter for the telephones list
	 * @return the number of phones in the network
	 */
	public int getNumberOfPhones();
	
	/**
	 * Get a phone number from the network given a number
	 * @param number the phone number
	 * @return a phone entity with that number
	 */
	public Telephone getPhone(final int number);
}
