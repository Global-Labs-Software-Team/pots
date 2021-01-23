package com.globallabs.telephone;

import com.globallabs.abstractions.TelephoneSpecification;
import com.globallabs.operator.Exchange;
import com.globallabs.phonedata.TelephoneModel;
import com.globallabs.phoneexceptions.BusyPhoneException;
import com.globallabs.phoneexceptions.DialingMySelfException;
import com.globallabs.phoneexceptions.NoCommunicationPathException;
import com.globallabs.phoneexceptions.NoIncomingCallsException;
import com.globallabs.phoneexceptions.PhoneExistInNetworkException;
import com.globallabs.phoneexceptions.PhoneNotFoundException;

/**
 * Telephone is the class that offers modification of the attributes of a phone model.
 * It's goal is to perform operations for the abstract methods that it implements from
 * 'TelephoneSpecification'
 * 
 * <p>Initialize (using a constructor) a telephone model within the exchange.
 * The functions offered includes:
 * <ul>
 * <li>Retrieve and set the id
 * <li>Retrieve and set the id
 * <li>Retrieve the entire phone model
 * <li>Retrieve and set last call
 * <li>Retrieve and set incoming call
 * <li>Performs the dialing operation with another phone
 * <li>Simulates dialing behavior for a phone
 * <li>Answers an existing call
 * </ul>
 *
 * @since 1.0
 * @author Byron Barkhuizen
 */

public class Telephone extends Thread implements TelephoneSpecification {
  // This number is used when for lastCall and incomingCall do
  // not have any number assigned. For example, lastCall when
  // the Telephone object is created or incomingCall when
  // a call is answered
  public static int PHONE_NOT_SET = -1;
  
  private TelephoneModel phoneInfo;
  private Exchange exchange;
  private Status status;

  private int lastCall;
  private int incomingCall;
  
  /**
   * Telephone constructor. 
   * Takes the information from the model. Adds the phone to the exchange 
   * and sets it's status to OFF_CALL
   *
   * @param phoneInfo a telephone model containing the phone's information
   * @param exchange the exchange where the phone belongs
   */
  public Telephone(TelephoneModel phoneInfo, Exchange exchange) 
      throws PhoneExistInNetworkException {
    super(Integer.toString(phoneInfo.getId()));
    this.phoneInfo = phoneInfo;
    this.exchange = exchange;
    this.status = Status.OFF_CALL;
    lastCall = PHONE_NOT_SET;
    incomingCall = PHONE_NOT_SET;
  }

  /**
   * Alternative telephone constructor.
   * It only takes the information from the model and initialize the
   * variables lastCall and incomingCall. It is used for testing, to
   * be able to add phones manually into the exchange.
   *
   * @param phoneInfo the basic information of the telephone
   */
  public Telephone(TelephoneModel phoneInfo) {
    super(Integer.toString(phoneInfo.getId()));
    this.phoneInfo = phoneInfo;
    status = Status.OFF_CALL;
    lastCall = PHONE_NOT_SET;
    incomingCall = PHONE_NOT_SET;
  }

  @Override
  public int getTelephoneId() {
    return phoneInfo.getId();
  }

  @Override
  public TelephoneModel getPhoneInfo() {
    return phoneInfo;
  }
  
  @Override
  public Status getStatus() {
    return status;
  }
    
  @Override
  public void setStatus(final Status updatedStatus) {
    status = updatedStatus;
  }
    
  @Override
  public int getLastCall() {
    return lastCall;
  }
    
  @Override
  public void setLastCall(final int phoneNumber) {
    lastCall = phoneNumber;
  }
    
  @Override
  public int getIncomingCall() {
    return incomingCall;
  }
    
  @Override
  public void setIncomingCall(final int phoneNumber) {
    incomingCall = phoneNumber;
  }
  
  @Override
  public void dial(final int phoneNumber) throws DialingMySelfException {
    if (phoneNumber == phoneInfo.getId()) {
      throw new DialingMySelfException("You are calling yourself");
    }
    try {
      exchange.enrouteCall(phoneInfo.getId(), phoneNumber);
    } catch (PhoneNotFoundException e) {
      setStatus(Status.OFF_CALL);
    } catch (BusyPhoneException e) {
      setStatus(Status.OFF_CALL);
    }
  }

  @Override
  public void dialing() throws BusyPhoneException {
    if (getStatus().equals(Status.DIALING)) {
      long start = System.currentTimeMillis();
      long end = start + 10 * 1000;
      while (System.currentTimeMillis() < end) {
        if (getStatus().equals(Status.BUSY)) {
          return;
        }
        //System.out.println("Ringing");
      }
      setStatus(Status.OFF_CALL);
    } else {
      throw new BusyPhoneException("");
    }
    
  }

  @Override
  public void answer() 
      throws BusyPhoneException, NoIncomingCallsException, 
      NoCommunicationPathException, PhoneNotFoundException {
    if (getStatus().equals(Status.RINGING)) {
      exchange.openCallBetween(getTelephoneId(), getIncomingCall());
    } else if (getStatus().equals(Status.BUSY)) {
      throw new BusyPhoneException("You can't answer while you are in another call");
    } else {
      throw new NoIncomingCallsException("No one is calling you");
    }
  }

  @Override
  public void hangUp() throws NoCommunicationPathException, PhoneNotFoundException {
    int otherPhoneNumber;
    if (getStatus() == Status.RINGING) {
      otherPhoneNumber = getIncomingCall();
    } else if (getStatus() == Status.DIALING || getStatus() == Status.BUSY) {
      otherPhoneNumber = getLastCall();
    } else {
      throw new NoCommunicationPathException("You don't have any active call");
    }
    
    exchange.closeCallBetween(this.getTelephoneId(), otherPhoneNumber);
  }
  
  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (!(o instanceof Telephone)) {
      return false;
    }
    Telephone telephone = (Telephone) o;
    return this.phoneInfo.equals(telephone.phoneInfo);
  }

  @Override
  public String toString() {
    return phoneInfo.toString() + " {" + "last call phone id: " + lastCall
      + ", incoming call id: " + incomingCall + ", status: " + status + "}";
  }
}
