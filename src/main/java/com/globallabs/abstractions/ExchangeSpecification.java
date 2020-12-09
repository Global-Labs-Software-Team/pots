package com.globallabs.abstractions;

import com.globallabs.phoneexceptions.BusyPhoneException;
import com.globallabs.phoneexceptions.NoCommunicationPathException;
import com.globallabs.phoneexceptions.PhoneExistInNetworkException;
import com.globallabs.phoneexceptions.PhoneNotFoundException;
import com.globallabs.telephone.Telephone;

public interface ExchangeSpecification {
    
  /**
  * Create a communication link between two Telephone devices. This method is
  for the use of the device initiating the call (dialing). The remark to this
  method is that it will change the status of Telephone receiving the call, to
  notify him that is receiving a call.
  *
  * <p>Example of its use: There is a Telephone 1 (t1) who is dialing a
  Telephone 2 (t2). To connect them t1 make use of enrouteCall(t1 number, t2 number)
  to create a communication link with t2.
  * @param origin The Telephone number of the device dialing (initiating the call).
  * @param destination The Telephone number of the device receiving the call.
  * @throws BusyPhoneException If the destination has an Status equal to Busy.
  * @throws PhoneNotFoundException if the Telephone the origin is trying to reach (destination)
  does not belong to the exchange.
  */
  public void enrouteCall(final int origin, final int destination) 
      throws BusyPhoneException, PhoneNotFoundException;
  
  /**
  * Close the communication link between two Telephones that are
  currently in a call. This method can be called from either of
  the devices that belongs to the communication. It will update
  the status of the Telephone that did not close the call, to notify
  him that the call has ended.
  *
  * <p>Example of its use: Two Telephones are in a call: t1, t2. Then, t2
  decides to close the call, it uses closeCallBetween(t2 number, t1 number).
  * 
  * <p>Important: This method cannot be called before, creating a communication link (enrouteCall)
  and the peer receiving the call accept it (openCallBetween).
  * @param origin the number of the Telephone that wants to close the communication link.
  * @param destination the number of the Telephone in the other side of the communication
  link.
  * @throws NoCommunicationPathException when there is no communication link between origin
  and destination.
  */
  public void closeCallBetween(final int origin, final int destination) 
      throws NoCommunicationPathException;
  
  /**
  * Open the communication link with the destination phone. This method is for
  the use of the Telephone that is receiving a call (it has an incoming call). It will
  update the status of the Telephone who is calling properly.
  *
  * <p>Example of its use: The Telephone 1 (t1) dialed Telephone 2 (t2) using enrouteCall().
  Telephone (t2) accept the call using openCallBetween(number t2, number t1). Now the
  communication is fully established.
  *
  * <p>Important: This method cannot be called before creating a communication link
  (enrouteCall).
  * @param origin The Telephone who accept the call.
  * @param destination The Telephone who is dialing.
  * @throws NoCommunicationPathException when there is no communication link between
  origin and destination.
  */
  public void openCallBetween(final int origin, final int destination) 
      throws NoCommunicationPathException;
  
  /**
  * Add a phone to the exchange.
  * @param phone the Telephone device to be added.
  * @throws PhoneExistInNetworkException if the Telephone is already inside the network.
  */
  public void addPhoneToExchange(final Telephone phone) throws PhoneExistInNetworkException;
  
  /**
  * Get the number of phone currently in the exchange.
  * @return the number of phones in the exchange
  */
  public int getNumberOfPhones();
  
  /**
  * Get a phone number from the exchange given a number.
  * @param number the phone number
  * @return a phone entity with that number
  */
  public Telephone getPhone(final int number);
}
