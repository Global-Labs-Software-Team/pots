package com.globallabs.telephone;

import com.globallabs.abstractions.ExchangeSpecification;
import com.globallabs.abstractions.TelephoneAbstraction;
import com.globallabs.phonedata.TelephoneModel;
import com.globallabs.phoneexceptions.PhoneExistInNetworkException;

/**
 * Telephone is the class that offers modification of the attributes of a phone model.
 * It's goal is to perform operations for the abstract methods that are not implemented
 * in 'TelephoneAbstraction'
 * 
 * <p>Initialize (using a constructor) a telephone model within the exchange.
 * The methods implemented in this class include:
 * <ul>
 * <li>Retrieve the id
 * <li>Retrieve and set the entire phone model
 * <li>Retrieve and set the status
 * <li>Retrieve and set the last call
 * <li>Retrieve and set the incoming call
 * <li>Retrieve and set the exchange
 * </ul>
 *
 * @since 1.0
 * @author Angelica Acosta and Byron Barkhuizen
 */

public class Telephone extends TelephoneAbstraction {
  
  private TelephoneModel phoneInfo;
  private ExchangeSpecification exchange;
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
  public Telephone(TelephoneModel phoneInfo, ExchangeSpecification exchange) 
      throws PhoneExistInNetworkException {
    super(phoneInfo, exchange);
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
    super(phoneInfo);
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
  public void setPhoneInfo(TelephoneModel phoneInfo) {
    this.phoneInfo = phoneInfo;
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
  public ExchangeSpecification getExchange() {
    return exchange;
  }

  @Override
  public void setExchange(ExchangeSpecification exchange) {
    this.exchange = exchange;
  }
  
}
