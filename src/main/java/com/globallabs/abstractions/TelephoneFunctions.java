package com.globallabs.abstractions;

import com.globallabs.phoneexceptions.BusyPhoneException;
import com.globallabs.phoneexceptions.DialingMySelfException;
import com.globallabs.phoneexceptions.NoCommunicationPathException;
import com.globallabs.phoneexceptions.NoIncomingCallsException;
import com.globallabs.phoneexceptions.PhoneNotFoundException;

public interface TelephoneFunctions {
    
  /**
   * Dial a phone from the current device.
   *
   * @param number number of the destination phone
   * @throws DialingMySelfException if the phone you are dialing is you
  */
  public void dial(final int number)
      throws DialingMySelfException, BusyPhoneException, PhoneNotFoundException;
    
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
     */
  public void answer() 
      throws BusyPhoneException, NoIncomingCallsException, NoCommunicationPathException;
  //
  // public void hangUp();
}
