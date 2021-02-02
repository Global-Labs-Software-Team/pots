package com.globallabs.abstractions;

import com.globallabs.phonedata.TelephoneModel;
import com.globallabs.phoneexceptions.BusyPhoneException;
import com.globallabs.phoneexceptions.DialingMySelfException;
import com.globallabs.phoneexceptions.NoCommunicationPathException;
import com.globallabs.phoneexceptions.NoIncomingCallsException;
import com.globallabs.phoneexceptions.PhoneExistInNetworkException;
import com.globallabs.phoneexceptions.PhoneNotFoundException;
import com.globallabs.telephone.Status;
import com.globallabs.telephone.Telephone;

/**
 * TelephoneAbstraction is an abstraction that implements some of the methods
 * in 'TelephoneSpecification'.
 *
 * <p>The methods implemented in this class include:
 * <ul>
 * <li>Dials another phone
 * <li>Simulates dialing behavior for a phone
 * <li>Answers an existing call
 * </ul>
 *
 * @since 1.0
 * @author Angelica Acosta
 */
public abstract class TelephoneAbstraction extends Thread implements TelephoneSpecification {

  // This number is used to set lastCall and incomingCall as null.
  public static int PHONE_NOT_SET = -1;
  
  /**
   * Telephone constructor. 
   * Takes the information from the model. Adds the phone to the exchange 
   * and sets it's status to OFF_CALL
   *
   * @param phoneInfo a telephone model containing the phone's information
   * @param exchange the exchange where the phone belongs
   */
  public TelephoneAbstraction(TelephoneModel phoneInfo, ExchangeSpecification exchange) 
      throws PhoneExistInNetworkException {
    super(Integer.toString(phoneInfo.getId()));
    setPhoneInfo(phoneInfo);
    setExchange(exchange);
    setStatus(Status.OFF_CALL);
    setLastCall(PHONE_NOT_SET);
    setIncomingCall(PHONE_NOT_SET);
  }

  /**
   * Alternative telephone constructor.
   * It only takes the information from the model and initialize the
   * variables lastCall and incomingCall. It is used for testing, to
   * be able to add phones manually into the exchange.
   *
   * @param phoneInfo the basic information of the telephone
   */
  public TelephoneAbstraction(TelephoneModel phoneInfo) {
    super(Integer.toString(phoneInfo.getId()));
    setPhoneInfo(phoneInfo);
    setStatus(Status.OFF_CALL);
    setLastCall(PHONE_NOT_SET);
    setIncomingCall(PHONE_NOT_SET);
  }

  @Override
  public void dial(final int phoneNumber) throws DialingMySelfException {
    if (phoneNumber == getPhoneInfo().getId()) {
      throw new DialingMySelfException("You are calling yourself");
    }
    try {
      getExchange().enrouteCall(getPhoneInfo().getId(), phoneNumber);
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
      getExchange().openCallBetween(getTelephoneId(), getIncomingCall());
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
    
    getExchange().closeCallBetween(getTelephoneId(), otherPhoneNumber);
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
    return getPhoneInfo().equals(telephone.getPhoneInfo());
  }

  @Override
  public String toString() {
    return getPhoneInfo().toString() + " {" + "last call phone id: " + getLastCall()
      + ", incoming call id: " + getIncomingCall() + ", status: " + getStatus() + "}";
  }
}
