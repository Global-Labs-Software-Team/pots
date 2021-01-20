package com.globallabs.telephone;

import com.globallabs.abstractions.TelephoneWithPipelineSpecification;
import com.globallabs.operator.Exchange;
import com.globallabs.operator.Pipeline;
import com.globallabs.phonedata.TelephoneModel;
import com.globallabs.phoneexceptions.PhoneExistInNetworkException;

/**
 * This special class that extends from Telephone add the feature
 * of transmitting information. For making this communication, four
 * variables are defined:
 * <ul>
 * <li> publishPipe: It is a pipeline. It is where the Telephone will publish
 * its messages. Other phones cannot publish into this publishPipe, only the one
 * who created it. From this pipeline other phones in connection can obtain the messages
 * from this phone.
 * <li> consumePipe: It is also a pipeline. Here will be store the publish pipeline of the
 * phone you are connected. Through this pipe you will be able to obtain the information
 * send by the other peer.
 * <li> producer: It is the thread in charge of publishing messages into the publishPipe.
 * <li> consumer: It is the thread that will read the consumePipe constantly to process
 * information
 * </ul>
 * Example: Let's say that we have two telephones: Telephone One (t1) and Telephone Two (t2).
 * Then, t1 establish a communication with t2 (This means t1 dial t2 and t2 answer t1). After this
 * process t1.consumePipe will contain the t2.publishPipe and t2.consumePipe will 
 * contain t1.publishPipe. Now the two telephones can transmit information just publishing in their
 * own publishPipes.
 */
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
