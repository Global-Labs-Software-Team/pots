package com.globallabs.decorators;

import com.globallabs.abstractions.TelephoneSpecification;
import com.globallabs.phonedata.TelephoneModel;
import com.globallabs.phoneexceptions.BusyPhoneException;
import com.globallabs.phoneexceptions.DialingMySelfException;
import com.globallabs.phoneexceptions.NoCommunicationPathException;
import com.globallabs.phoneexceptions.NoIncomingCallsException;
import com.globallabs.phoneexceptions.PhoneNotFoundException;
import com.globallabs.telephone.Status;

public abstract class TelephoneDecorator implements TelephoneSpecification {
  public TelephoneSpecification telephone;

  public TelephoneDecorator(TelephoneSpecification telephone) {
    this.telephone = telephone;
  }

  @Override
  public int getTelephoneId() {
    return telephone.getTelephoneId();
  }

  @Override
  public TelephoneModel getPhoneInfo() {
    return telephone.getPhoneInfo();
  }

  @Override
  public Status getStatus() {
    return telephone.getStatus();
  }

  @Override
  public void setStatus(Status updatedStatus) {
    telephone.setStatus(updatedStatus);
  }

  @Override
  public int getLastCall() {
    return telephone.getLastCall();
  }

  @Override
  public void setLastCall(int phoneNumber) {
    telephone.setLastCall(phoneNumber);
  }

  @Override
  public int getIncomingCall() {
    return telephone.getIncomingCall();
  }

  @Override
  public void setIncomingCall(int phoneNumber) {
    telephone.setIncomingCall(phoneNumber);
  }

  @Override
  public void dial(int number) throws DialingMySelfException {
    telephone.dial(number);
  }

  @Override
  public void dialing() throws BusyPhoneException {
    telephone.dialing();
  }

  @Override
  public void answer() throws BusyPhoneException, NoIncomingCallsException, 
      NoCommunicationPathException, PhoneNotFoundException {
    telephone.answer();
  }

  @Override
  public void hangUp() throws NoCommunicationPathException, PhoneNotFoundException {
    telephone.hangUp();
  }
}
