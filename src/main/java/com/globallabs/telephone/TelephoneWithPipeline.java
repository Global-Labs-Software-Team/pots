package com.globallabs.telephone;

import com.globallabs.abstractions.TelephoneWithPipelineSpecification;
import com.globallabs.operator.Exchange;
import com.globallabs.operator.Pipeline;
import com.globallabs.phonedata.TelephoneModel;
import com.globallabs.phoneexceptions.PhoneExistInNetworkException;

public class TelephoneWithPipeline extends Telephone 
    implements TelephoneWithPipelineSpecification {

  private Pipeline publishPipe;
  private Pipeline consumePipe;
  protected Producer producer;
  protected Consumer consumer;

  /**
   * Complete.
   * @param phoneInfo a
   * @param exchange a
   * @param publishPipe a
   * @throws PhoneExistInNetworkException a
   */
  public TelephoneWithPipeline(TelephoneModel phoneInfo, Exchange exchange, Pipeline publishPipe) 
      throws PhoneExistInNetworkException {
    super(phoneInfo, exchange);
    exchange.addPhoneToExchange(this);
    this.publishPipe = publishPipe;
  }

  /**
   * complet.
   * @param phoneInfo complet
   * @param exchange complet
   * @throws PhoneExistInNetworkException complet
   */
  public TelephoneWithPipeline(TelephoneModel phoneInfo, Exchange exchange)
      throws PhoneExistInNetworkException {
    super(phoneInfo, exchange);
    publishPipe = new Pipeline("pipe" + getTelephoneId());
    exchange.addPhoneToExchange(this); 
  }

  public TelephoneWithPipeline(TelephoneModel phoneInfo) {
    super(phoneInfo);
  }

  @Override
  public void setConsumePipe(Pipeline consumePipe) {
    this.consumePipe = consumePipe;
  }

  @Override
  public Pipeline getConsumePipe() {
    return consumePipe;
  }

  @Override
  public void setPublishPipe(Pipeline publishPipe) {
    this.publishPipe = publishPipe;
  }

  @Override
  public Pipeline getPublishPipe() {
    return publishPipe;
  }

  /**
   * Main loop of the phone.
   */
  public void run() {
    System.out.println("Telephone with id " + getTelephoneId() + " is starting in thread: " 
        + getId());
    String telephoneName = "( Telephone " + getTelephoneId() + " ) ";
    while (true) {
      System.out.println(telephoneName + getStatus());
      if (getStatus() == Status.RINGING) {
        System.out.println(telephoneName + "You are receiving a call....");
      } else if (getStatus() == Status.BUSY) {
        System.out.println(telephoneName + "The call has been answered");
        break;
      }
    }
    activateConsumerProducerThreads();
  }

  @Override
  public void activateConsumerProducerThreads() {
    producer = new Producer("producer_" + getTelephoneId(), getPublishPipe(), 30);
    consumer = new Consumer("consumer_" + getTelephoneId(), 
        getConsumePipe(), 30);
    producer.start();
    consumer.start();
    
    while (producer.isAlive() || consumer.isAlive()) {};

    System.out.println("Call finished");
    System.out.println("(Telephone " + getTelephoneId() + ", " + consumer.getName() + ")"
        + "The information received from the call was : " + consumer.getBitsReceived());
  }

  public Consumer getConsumer() {
    return consumer;
  }

  public Producer getProducer() {
    return producer;
  }

  @Override
  public boolean isAbleTo(String nameOfFunction) {
    if (nameOfFunction.equals("enrouteCall")) {
      boolean validStatus = (getStatus() == Status.OFF_CALL);
      boolean incomingCallNotSet = (getIncomingCall() == PHONE_NOT_SET);
      return validStatus && incomingCallNotSet;
    } else if (nameOfFunction.equals("openCallBetween")) {
      boolean validStatus = (getStatus() == Status.RINGING);
      boolean incomingCallSet = (getIncomingCall() != PHONE_NOT_SET);
      return validStatus && incomingCallSet;
    } else if (nameOfFunction.equals("closeCallBetween")) {
      Status phoneStatus = getStatus();
      boolean isBusy = phoneStatus == Status.BUSY;
      boolean isRinging = phoneStatus == Status.RINGING;
      boolean isDialing = phoneStatus == Status.DIALING;
      if (isBusy) {
        boolean lastCallSet = (getLastCall() != PHONE_NOT_SET);
        return lastCallSet;
      } else if (isRinging) {
        boolean incomingCallSet = (getIncomingCall() != PHONE_NOT_SET);
        return incomingCallSet;
      } else if (isDialing) {
        boolean lastCallSet = (getLastCall() != PHONE_NOT_SET);
        return lastCallSet;
      }
    }
    return false;
  }
}
