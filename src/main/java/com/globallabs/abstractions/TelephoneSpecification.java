package com.globallabs.abstractions;

import com.globallabs.phonedata.TelephoneModel;
import com.globallabs.phoneexceptions.BusyPhoneException;
import com.globallabs.phoneexceptions.DialingMySelfException;
import com.globallabs.phoneexceptions.NoCommunicationPathException;
import com.globallabs.phoneexceptions.NoIncomingCallsException;
import com.globallabs.phoneexceptions.PhoneNotFoundException;
import com.globallabs.telephone.Status;

public interface TelephoneSpecification {

  /**
   * Returns the number of the telephone.
   *
   * @return the number of the telephone
   */ 
  public int getTelephoneId();

  /**
   * Returns the phone model to get all the info.
   *
   * @return a phone model object
   */
  public TelephoneModel getPhoneInfo();  

  /**
   * Sets the phone model of the phone.
   *
   * @param phoneInfo a phone model object
   */
  public void setPhoneInfo(TelephoneModel phoneInfo); 

  /**
   * Returns the status of the telephone.
   *
   * @return the status of the telephone
   */
  public Status getStatus();

  /**
   * Update the status of the telephone.
   *
   * @param updatedStatus the status to be set
   */
  public void setStatus(final Status updatedStatus);

  /**
   * Returns the exchange to which the phone belongs.
   *
   * @return a phone model object
   */
  public ExchangeSpecification getExchange();  

  /**
   * Sets the exchange to which the phone belongs.
   *
   * @param exchange a exchange specification object
   */
  public void setExchange(ExchangeSpecification exchange); 

  /**
   * Returns the last phone you were in a call with.
   * If you are in a call, returns the phone you are connected with
   *
   * @return a Telephone object of the last phone you were in a call with
   */
  public int getLastCall();

  /**
   * Sets the last phone you were in a call with.
   *
   * @param phoneNumber a Telephone object of the last phone you were in a call with
   */
  public void setLastCall(final int phoneNumber);

  /**
   * Returns the phone that is calling you.
   * Returns null if nobody is calling
   *
   * @return the phone calling you
   */
  public int getIncomingCall();

  /**
   * Sets the phone that is calling you.
   *
   * @param phoneNumber the phone which is the origin of the call
   */
  public void setIncomingCall(final int phoneNumber);

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
