package com.globallabs.operator;

import com.globallabs.abstractions.ExchangeSpecification;
import com.globallabs.phoneexceptions.BusyPhoneException;
import com.globallabs.phoneexceptions.NoCommunicationPathException;
import com.globallabs.phoneexceptions.PhoneExistInNetworkException;
import com.globallabs.phoneexceptions.PhoneNotFoundException;
import com.globallabs.telephone.Status;
import com.globallabs.telephone.Telephone;
import java.util.LinkedList;


public class Exchange implements ExchangeSpecification {
    
  private LinkedList<Telephone> telephones;
  
  public Exchange() {
    this.telephones = new LinkedList<Telephone>();
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
    
    if (!(phoneWhoCloseCall.getLastCall() == phoneTheOtherEnd)) {
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
    this.telephones.add(phone);
  }
  
  public int getNumberOfPhones() {
    return this.telephones.size();
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
