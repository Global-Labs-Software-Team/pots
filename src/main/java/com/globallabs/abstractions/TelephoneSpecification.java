package com.globallabs.abstractions;

import com.globallabs.phoneexceptions.BusyPhoneException;
import com.globallabs.phoneexceptions.DialingMySelfException;
import com.globallabs.phoneexceptions.NoCommunicationPathException;
import com.globallabs.phoneexceptions.NoIncomingCallsException;
import com.globallabs.phoneexceptions.PhoneNotFoundException;

public interface TelephoneSpecification {
    
  /**
   * Dial a phone from the current device.
   *
   * @param number number of the destination phone
   * @throws DialingMySelfException if the phone you are dialing is yours
  */
  public void dial(final int number) throws DialingMySelfException;
    
  /**
   * Sets the current device in a ringing state.
   *
   * @throws BusyPhoneException if we are already in a call
   */
  public void dialing() throws BusyPhoneException;
    
  /**
   * Current device answers an incoming call.
   *
   * @throws BusyPhoneException if we are already in a call
   * @throws NoIncomingCallsException if there are no incoming calls
   * @throws NoCommunicationPathException when there is no communication
   path between you and your last incomming call in the exchange
   * @throws PhoneNotFoundException when the phone you try to answer does
   not belong to the exchange
   */
  public void answer() 
      throws BusyPhoneException, NoIncomingCallsException, 
      NoCommunicationPathException, PhoneNotFoundException;
  
  /**
   * Hang up the ongoing call or the incoming call.
   *
   * @throws NoCommunicationPathException when you are hanging up to
   a phone you are not connected
   * @throws PhoneNotFoundException when the phone you try to hangUp does
   not belong to the exchange
   */
  public void hangUp() throws NoCommunicationPathException, PhoneNotFoundException;
}
