package com.globallabs.decorators;

import com.globallabs.abstractions.ExchangeSpecification;
import com.globallabs.phoneexceptions.BusyPhoneException;
import com.globallabs.phoneexceptions.NoCommunicationPathException;
import com.globallabs.phoneexceptions.PhoneExistInNetworkException;
import com.globallabs.phoneexceptions.PhoneNotFoundException;
import com.globallabs.telephone.TelephoneWithPipeline;

import java.util.LinkedList;

public abstract class ExchangeDecorator implements ExchangeSpecification {
  public ExchangeSpecification exchange;

  public ExchangeDecorator(ExchangeSpecification exchange) {
    this.exchange = exchange;
  }

  @Override
  public void enrouteCall(int origin, int destination) 
      throws BusyPhoneException, PhoneNotFoundException {
    exchange.enrouteCall(origin, destination);
  }

  @Override
  public void closeCallBetween(int origin, int destination)
      throws NoCommunicationPathException, PhoneNotFoundException {
    exchange.closeCallBetween(origin, destination);
  }

  @Override
  public void openCallBetween(int origin, int destination)
      throws NoCommunicationPathException, PhoneNotFoundException {
    exchange.openCallBetween(origin, destination);
  }

  @Override
  public boolean communicationExists(TelephoneWithPipeline telephoneOne, 
      TelephoneWithPipeline telephoneTwo) {
    return exchange.communicationExists(telephoneOne, telephoneTwo);
  }

  @Override
  public void addPhoneToExchange(TelephoneWithPipeline phone) throws PhoneExistInNetworkException {
    exchange.addPhoneToExchange(phone);
  }

  @Override
  public void setTelephones(LinkedList<TelephoneWithPipeline> telephones) {
    exchange.setTelephones(telephones);
  }

  @Override
  public LinkedList<TelephoneWithPipeline> getTelephones() {
    return exchange.getTelephones();
  }

  @Override
  public int getNumberOfPhones() {
    return exchange.getNumberOfPhones();
  }

  @Override
  public TelephoneWithPipeline getPhone(int number) throws PhoneNotFoundException {
    return exchange.getPhone(number);
  }
}
