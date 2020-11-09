package com.globallabs.abstractions;

import com.globallabs.phoneexceptions.*;
import com.globallabs.telephone.Telephone;

public interface TelephoneFunctions {
	
	/**
	 * Dial a phone from the current device
	 * @param phoneToCall
	 * @throws DialingMySelfException if the phone you are dialing is you
	 */
	public void dial(final int number) throws DialingMySelfException, BusyPhoneException, PhoneNotFoundException;
	
	/**
	 * Sets the current device in a ringing state
	 * @throws BusyPhoneException if we are already in a call
	 */
	public void dialing() throws BusyPhoneException;
	
	/**
	 * Current device answers an incoming call
	 * @throws BusyPhoneException if we are already in a call
	 * @throws NoIncomingCallsException if there are no incoming calls
	 */
	public void answer() throws BusyPhoneException, NoIncomingCallsException, NoCommunicationPathException;
//	
	/**
	 * Hang up the current call or a incoming call
	 * @throws NoCommunicationPathException if there is no active call
	 */
	public void hangUp()throws NoCommunicationPathException;
}
