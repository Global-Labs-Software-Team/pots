package com.globallabs.operator;

import com.globallabs.abstractions.ExchangeSpecification;
import com.globallabs.phoneexceptions.BusyPhoneException;
import com.globallabs.phoneexceptions.NoCommunicationPathException;
import com.globallabs.phoneexceptions.PhoneExistInNetworkException;
import com.globallabs.phoneexceptions.PhoneNotFoundException;
import com.globallabs.telephone.Status;
import com.globallabs.telephone.Telephone;
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
 * @author Daniel RODRIGUEZ
 * @since 1.0
 */
public class Exchange implements ExchangeSpecification {
    
  private LinkedList<Telephone> telephones;
  
  public Exchange() {
    telephones = new LinkedList<Telephone>();
  }
  
  @Override
  public void enrouteCall(final int phoneNumberFrom, final int phoneNumber) 
      throws BusyPhoneException, PhoneNotFoundException {
    Telephone phoneToCall = getPhone(phoneNumber);
    Telephone originPhone = getPhone(phoneNumberFrom);
    if (phoneToCall == null || originPhone == null) {
      throw new PhoneNotFoundException("Either, the phone with id " 
        + phoneNumber + "or" + phoneNumberFrom + " does not belongs to the network");
    }
    if (phoneToCall.getStatus() == Status.BUSY) {
      throw new BusyPhoneException("The phone with id " + phoneNumber + " is busy");
    }
    originPhone.setLastCall(phoneToCall);
    phoneToCall.setStatus(Status.RINGING);
    phoneToCall.setIncomingCall(originPhone);
  }
  
  @Override
  public void openCallBetween(final int receiverNumber, final int callingNumber) 
      throws NoCommunicationPathException {
    Telephone phone = getPhone(receiverNumber); // Phone receiving the call
    Telephone origin = getPhone(callingNumber); // Phone that is calling
      
    // If the origin phone is not the one calling the other phone
    // or
    // the origin last call is not the same as the other phone
    if (!origin.equals(phone.getIncomingCall()) || !phone.equals(origin.getLastCall())) {
      throw new NoCommunicationPathException("There is no path between " 
      + origin + " and " + phone);
    }
      
    origin.setStatus(Status.BUSY);
  }
  
  @Override
  public void closeCallBetween(final int numberWhoCloseCall, final int theOtherNumberInCall) 
      throws NoCommunicationPathException {
    Telephone phoneWhoCloseCall = getPhone(numberWhoCloseCall);
    Telephone phoneTheOtherEnd = getPhone(theOtherNumberInCall);
    if (!((phoneWhoCloseCall.getLastCall() != null 
        && phoneWhoCloseCall.getLastCall().equals(phoneTheOtherEnd)) 
          || (phoneWhoCloseCall.getIncomingCall() != null 
          && phoneWhoCloseCall.getIncomingCall().equals(phoneTheOtherEnd)))) {
      throw new NoCommunicationPathException(
                "There is no path between " + phoneWhoCloseCall + " and " + phoneTheOtherEnd);
    }
    
    if (phoneTheOtherEnd.getStatus() == Status.RINGING) {
      phoneTheOtherEnd.setIncomingCall(null);
    }
    
    phoneTheOtherEnd.setStatus(Status.OFF_CALL);
  }
  
  @Override
  public void addPhoneToExchange(final Telephone phone) 
      throws PhoneExistInNetworkException {
    if (getPhone(phone.getId()) != null) {
      throw new PhoneExistInNetworkException("The phone " 
        + phone + "is already in the network");
    }
    telephones.add(phone);
  }
  
  public int getNumberOfPhones() {
    return telephones.size();
  }
  
  @Override
  public Telephone getPhone(final int phoneNumber) {
    for (Telephone phone: telephones) {
      if (phone.getId() == phoneNumber) {
        return phone;
      }
    }
    return null;
  }
}
