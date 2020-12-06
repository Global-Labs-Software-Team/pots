package com.globallabs.abstractions;

import com.globallabs.phoneexceptions.BusyPhoneException;
import com.globallabs.phoneexceptions.NoCommunicationPathException;
import com.globallabs.phoneexceptions.PhoneExistInNetworkException;
import com.globallabs.phoneexceptions.PhoneNotFoundException;
import com.globallabs.telephone.Telephone;

public interface ExchangeSpecification {
    
  /**
  * Route the call that a phone is requesting.
  * @param origin The telephone were the call is coming
  * @param destination The destination telephone
  * @throws BusyPhoneException if the other phone is in a call
  * @throws PhoneNotFoundException if the other phone does not belong to the network
  */
  public void enrouteCall(final int origin, final int destination) 
      throws BusyPhoneException, PhoneNotFoundException;
  
  /**
  * Close the communication channel with the given phone
  * number. This update the status of the other phone
  * to OFF_CALL
  * @param origin the phone where the closeCall signal comes from
  * @param destination the phoneNumber that you want to close the communication with
  * @throws NoCommunicationPathException when there is no communication between origin
  and destination
  */
  public void closeCallBetween(final int origin, final int destination) 
      throws NoCommunicationPathException;
  
  /**
  * Open the communication channel with the given phone.
  * This update the status of the other phone to BUSY.
  * @param origin the phone where the openCall signal comes from
  * @param destination the phone number you one accept its call
  * @throws NoCommunicationPathException when a path from phoneNumberFrom and phoneNumber 
  does not exist
  */
  public void openCallBetween(final int origin, final int destination) 
      throws NoCommunicationPathException;
  
  /**
  * Add a phone to the network.
  * @param phone the Telephone object to add to the exchange
  * @throws PhoneExistInNetworkException if the phone is already inside the network
  */
  public void addPhoneToExchange(final Telephone phone) throws PhoneExistInNetworkException;
  
  /**
  * Getter for the telephones list.
  * @return the number of phones in the network
  */
  public int getNumberOfPhones();
  
  /**
  * Get a phone number from the network given a number.
  * @param number the phone number
  * @return a phone entity with that number
  */
  public Telephone getPhone(final int number);
}
