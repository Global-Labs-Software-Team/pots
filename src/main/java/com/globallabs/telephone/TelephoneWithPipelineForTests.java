package com.globallabs.telephone;

import com.globallabs.operator.Exchange;
import com.globallabs.phonedata.TelephoneModel;
import com.globallabs.phoneexceptions.PhoneExistInNetworkException;

import java.util.LinkedList;

public class TelephoneWithPipelineForTests extends TelephoneWithPipeline {

  LinkedList<Integer> streamToSend;

  public TelephoneWithPipelineForTests(TelephoneModel phoneInfo, Exchange exchange, 
      LinkedList<Integer> streamToSend) throws PhoneExistInNetworkException {
    super(phoneInfo, exchange);
    this.streamToSend = streamToSend;
  }

  @Override
  public void activateConsumerProducerThreads() {
    producer = new ProducerForTests("producer_" + getTelephoneId(), 
        getPublishPipe(), streamToSend, 30);
    consumer = new Consumer("consumer_" + getTelephoneId(), 
        getConsumePipe(), 30);
    producer.start();
    consumer.start();
    
    while (producer.isAlive() || consumer.isAlive()) {};
    
    System.out.println("Call finished");
    System.out.println("(Telephone " + getTelephoneId() + ", " + consumer.getName() + ")"
        + "The information received from the call was : " + consumer.getBitsReceived());
  }
}
