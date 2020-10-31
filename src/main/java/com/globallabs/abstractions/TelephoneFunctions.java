package com.globallabs.abstractions;

import com.globallabs.phoneexceptions.*;
import com.globallabs.pots.Telephone;

public interface TelephoneFunctions {
	
	/**
	 * Dial a phone from the current device
	 * @param phoneToCall
	 * @throws DialingMySelfException if the phone you are dialing is you
	 */
	public void dial(final int number) throws DialingMySelfException;
	
	/**
	 * Sets the current device in a ringing state
	 * @throws BusyPhoneException if we are already in a call
	 */
	public void ring() throws BusyPhoneException;
	
	/**
	 * Current device answers an incoming call
	 * @throws BusyPhoneException if we are already in a call
	 * @throws NoIncomingCallsException if there are no incoming calls
	 */
	public void answer() throws BusyPhoneException, NoIncomingCallsException;
//	
//	public void hangUp();
	
	/**
	 * Gets the info basic information
	 * @return Telephone object
	 */
	public Telephone getPhoneInfo();
}
