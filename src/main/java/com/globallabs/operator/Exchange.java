package com.globallabs.operator;

import com.globallabs.abstractions.ExchangeSpecification;
import com.globallabs.phoneexceptions.BusyPhoneException;
import com.globallabs.phoneexceptions.NoCommunicationPathException;
import com.globallabs.phoneexceptions.PhoneExistInNetworkException;
import com.globallabs.phoneexceptions.PhoneNotFoundException;
import com.globallabs.telephone.Status;
import com.globallabs.telephone.Telephone;
import com.globallabs.telephone.TelephoneWithPipeline;
import java.util.LinkedList;

/**
 * Exchange is the class that manages the communication between
 * Telephones. Its principal goal of all its functionalities 
 * is to synchronize the calls between Telephones. 
 * Between these functionalities are:
 * <ul>
 * <li>Create a line of communication between Telephones.
 * <li>If a Telephone t1 accept a call from a Telephone t2 then t1
 *     "open" the call
 * <li>"Close" a communication when one of the peers end the call
 * <li>Keeps a list of all the Telephones belonging to the
 *     exchange
 * </ul>
 *  
 * <p>One remark to keep mind when using Exchange is that
 * its three communication management functions (enrouteCall, openCallBetween
 * and closeCallBetween) work as a pipeline. To make a full call
 * process (dial -> call -> hang up) you have to use the functions in the
 * following order:
 * 1) enrouteCall (Made by the Telephone dialing)
 * 2) openCallBetween (Made by the Telephone receiving the call)
 * 3) closeCallBetween (Made by the Telephone who hang up the call)
 * 
 * <p>Second remark for all the three communication functions the order
 * of the parameters is important. These parameters follow this logic:
 * the first parameter represents the Telephone who called the function,
 * the second parameter represents the Telephone that is going to be
 * synchronized by the consequences of this function. For example:
 * 
 * <p>You have two telephones t1 and t2, t1 dial t2, this means that t1
 * called the function enrouteCall. Then, enrouteCall will synchronize t2
 * to give it the information that t1 is calling him. All the other functions
 * work in the same way.
 * 
 * <p>Third remark, it only exists one Exchange instance across the application.
 * All the phones to be able to connect between each other must use this single
 * instance. Application of the Singleton design pattern.
 *
 * @author Daniel RODRIGUEZ
 * @since 1.0
 */
public class Exchange implements ExchangeSpecification {
  
  protected LinkedList<TelephoneWithPipeline> telephones;
  
  public Exchange() {
    telephones = new LinkedList<TelephoneWithPipeline>();
  }

  /**
   * This private class will create an only exchange
   * when a Telephone try to get it (Singleton Pattern).
   * If the exchange does not exists then it will create it,
   * else it will retrieve it. 
   */
  private static class ExchangeHolder {
    private static final Exchange INSTANCE = new Exchange();
  }

  /**
   * Access to the singleton of Exchange.
   *
   * @return the exchange
   */
  public static Exchange getInstance() {
    return ExchangeHolder.INSTANCE;
  }
  
  @Override
  public void enrouteCall(final int origin, final int destination) 
      throws BusyPhoneException, PhoneNotFoundException {
    Telephone originPhone = getPhone(origin);
    Telephone destinationPhone = getPhone(destination);
    if (destinationPhone.getStatus() == Status.BUSY) {
      throw new BusyPhoneException("The phone with id " + destination + " is busy");
    }
    originPhone.setStatus(Status.DIALING);
    originPhone.setLastCall(destinationPhone.getTelephoneId());
    destinationPhone.setStatus(Status.RINGING);
    destinationPhone.setIncomingCall(originPhone.getTelephoneId());
  }
  
  @Override
  public void openCallBetween(final int origin, final int destination) 
      throws NoCommunicationPathException, PhoneNotFoundException {
    TelephoneWithPipeline originPhone = getPhone(origin); // Phone receiving the call
    TelephoneWithPipeline destinationPhone = getPhone(destination); // Phone that is calling     
    // If the origin phone is not the one calling the other phone
    // or
    // the origin last call is not the same as the other phone
    if (!communicationExists(originPhone, destinationPhone)) {
      throw new NoCommunicationPathException("There is no path between " 
      + destinationPhone + " and " + originPhone);
    }
    // Destination phone only needs to change the status
    // because it is the one that is making the call. In other terms,
    // it is the one that used the enrouteCall. So it last call will be
    // the id of the originPhone.
    destinationPhone.setStatus(Status.BUSY);
    destinationPhone.setConsumePipe(originPhone.getPublishPipe());
    originPhone.setStatus(Status.BUSY);
    originPhone.setLastCall(destinationPhone.getTelephoneId());
    originPhone.setIncomingCall(Telephone.PHONE_NOT_SET);
    originPhone.setConsumePipe(destinationPhone.getPublishPipe());
  }
  
  @Override
  public void closeCallBetween(final int origin, final int destination) 
      throws NoCommunicationPathException, PhoneNotFoundException {
    TelephoneWithPipeline originPhone = getPhone(origin);
    TelephoneWithPipeline destinationPhone = getPhone(destination);
    if (!communicationExists(originPhone, destinationPhone)) {
      throw new NoCommunicationPathException(
                "There is no path between " + originPhone + " and " + destinationPhone);
    }
    destinationPhone.setStatus(Status.OFF_CALL);
    destinationPhone.setIncomingCall(Telephone.PHONE_NOT_SET);
    destinationPhone.setConsumePipe(null);
    originPhone.setStatus(Status.OFF_CALL);
    originPhone.setIncomingCall(Telephone.PHONE_NOT_SET);
    originPhone.setConsumePipe(null);
  }
  
  @Override
  public boolean communicationExists(final TelephoneWithPipeline telephoneOne, 
      final TelephoneWithPipeline telephoneTwo) {
    int phoneOneNumber = telephoneOne.getTelephoneId();
    int phoneTwoNumber = telephoneTwo.getTelephoneId();
    // Scenario One
    if ((telephoneOne.getStatus() == Status.BUSY 
        && telephoneTwo.getStatus() == Status.BUSY) && // Both BUSY
        // The last call of telephone one is telephone two and vice versa for telephone two
        (telephoneOne.getLastCall() == phoneTwoNumber 
        && telephoneTwo.getLastCall() == phoneOneNumber) 
    ) {
      return true;
    // Scenario Two
    } else if ((telephoneOne.getStatus() == Status.DIALING // One is dialing Two
        && telephoneTwo.getStatus() == Status.RINGING)
        && (telephoneOne.getLastCall() == phoneTwoNumber 
        && telephoneTwo.getIncomingCall() == phoneOneNumber)) {
      return true;
    // Scenario Two but parameters inverted
    } else if ((telephoneOne.getStatus() == Status.RINGING // One is dialing Two
        && telephoneTwo.getStatus() == Status.DIALING)
        && (telephoneTwo.getLastCall() == phoneOneNumber 
        && telephoneOne.getIncomingCall() == phoneTwoNumber)) {
      return true;
    }
    return false;
  }

  @Override
  public void addPhoneToExchange(final TelephoneWithPipeline phone) 
      throws PhoneExistInNetworkException {
    try {
      Telephone itExists = getPhone(phone.getTelephoneId());
      throw new PhoneExistInNetworkException("The phone " 
        + itExists + "is already in the network");
    } catch (PhoneNotFoundException e) {
      telephones.add(phone);
    } 
  }
  
  public int getNumberOfPhones() {
    return telephones.size();
  }
  
  @Override
  public TelephoneWithPipeline getPhone(final int phoneNumber) throws PhoneNotFoundException {
    for (TelephoneWithPipeline phone : telephones) {
      if (phone.getTelephoneId() == phoneNumber) {
        return phone;
      }
    }
    throw new PhoneNotFoundException("Either, the phone with id " 
        + phoneNumber + " does not belongs to the network");
  }
}
