package com.globallabs.telephone;

import com.globallabs.abstractions.TelephoneFunctions;
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
 * 'TelephoneFunctions'
 * 
 * Initialize (using a constructor) a telephone model within the exchange.
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

public class Telephone implements TelephoneFunctions {
  private TelephoneModel phoneInfo;
  private Exchange exchange;
  private Status status;
  
  private Telephone lastCall;
  private Telephone incomingCall;
  
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
    this.phoneInfo = phoneInfo;
    this.exchange = exchange;
    this.exchange.addPhoneToExchange(this);
    this.status = Status.OFF_CALL;
  }
    
  /**
   * Returns the number of the telephone.
   *
   * @return the number of the telephone
   */
  public int getId() {
    return phoneInfo.getId();
  }

  /**
   * Returns the phone model to get all the info.
   *
   * @return a phone model object
   */
  public TelephoneModel getPhoneInfo() {
    return phoneInfo;
  }
    
  /**
   * Returns the status of the telephone.
   *
   * @return the status of the telephone
   */
  public Status getStatus() {
    return status;
  }
    
  /**
   * Sets a new status of the telephone.
   *
   * @param newStatus the status to be set
   */
  public void setStatus(final Status newStatus) {
    status = newStatus;
  }
    
  /**
   * Returns the last phone you were in a call with.
   * If you are in a call, returns the phone you are connected with
   *
   * @return a Telephone object of the last phone you were in a call with
   */
  public Telephone getLastCall() {
    return lastCall;
  }
    
  /**
   * Sets the last phone you were in a call with.
   *
   * @param phone a Telephone object of the last phone you were in a call with
   */
  public void setLastCall(final Telephone phone) {
    lastCall = phone;
  }
    
  /**
   * Returns the phone that is calling you.
   * Returns null if nobody is calling
   *
   * @return the phone calling you
   */
  public Telephone getIncomingCall() {
    return incomingCall;
  }
    
  /**
   * Sets the phone that is calling you.
   *
   * @param phone the phone which is the origin of the call
   */
  public void setIncomingCall(final Telephone phone) {
    incomingCall = phone;
  }
  
  /**
   * Dial a phone by it's number.
   *
   * @param phoneNumber the number to be dialed
   */
  public void dial(final int phoneNumber) 
      throws DialingMySelfException, PhoneNotFoundException, BusyPhoneException {
    if (phoneNumber == phoneInfo.getId()) {
      throw new DialingMySelfException("You are calling yourself");
    }
    setStatus(Status.DIALING);
    try {
      exchange.enrouteCall(phoneInfo.getId(), phoneNumber);
    } catch (Exception e) {
      setStatus(Status.OFF_CALL);
      throw e;
    }
  }

  /**
   * Dialing a call for 10 seconds.
   *
   * @throws BusyPhoneException if the call is not answered in time
   */
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

  /**
   * Answer a call.
   * @throws BusyPhoneException if you are busy
   * @throws NoIncomingCallsException if trying to answer non-existent call
   */
  public void answer() 
      throws BusyPhoneException, NoIncomingCallsException, NoCommunicationPathException {
    if (getStatus().equals(Status.RINGING)) {
      exchange.openCallBetween(getId(), getIncomingCall().getId());
      setStatus(Status.BUSY);
      setLastCall(getIncomingCall());
      setIncomingCall(null);
    } else if (getStatus().equals(Status.BUSY)) {
      throw new BusyPhoneException("You can't answer while you are in another call");
    } else {
      throw new NoIncomingCallsException("No one is calling you");
    }
  }

  @Override
  public void hangUp() throws NoCommunicationPathException {
    Telephone otherPhone;
    if (getStatus() == Status.RINGING) {
      otherPhone = getIncomingCall();
    } else if (getStatus() == Status.DIALING || getStatus() == Status.BUSY) {
      otherPhone = getLastCall();
    } else {
      throw new NoCommunicationPathException("You don't have any active call");
    }
    
    exchange.closeCallBetween(this.getId(), otherPhone.getId());
    setStatus(Status.OFF_CALL);
    setIncomingCall(null);
  }
  
  /**
   * Compare to telephone to see if they are the same.
   *
   * @param o The object to compare
   * @return true if they are equal, false otherwise
   */
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

  /**
   * String representation of the object.
   */
  @Override
  public String toString() {
    String lastCall = this.lastCall == null ? null :
        this.lastCall.getPhoneInfo().toString();
    String incomingCall = this.incomingCall == null ? null :
        this.incomingCall.getPhoneInfo().toString(); 
    return phoneInfo.toString() + " {" + "last call: " + lastCall
      + ", incoming call: " + incomingCall + ", status: " + status + "}";
  }
}
