package com.globallabs.abstractions;

import com.globallabs.operator.Pipeline;
import com.globallabs.telephone.Consumer;
import com.globallabs.telephone.Producer;

public interface TelephoneWithPipelineSpecification extends TelephoneSpecification {
  
  /**
   * Set the pipe where the phone is going to consume information.
   * For example, telephone one and telephone two are connected, telephone
   * one will obtain the messages reading its pipeline, the same for telephone two. 
   *
   * @param consumePipe The pipeline of the other phone
   */
  public void setConsumePipe(Pipeline consumePipe);
  
  /**
   * Obtain the consume pipe of the current connection.
   *
   * @return the consume pipeline
   */
  public Pipeline getConsumePipe();

  /**
   * Obtain the pipeline where the telephone can publish its messages.
   * With this pipeline other phone can read the messages
   *
   * @return the publish pipe
   */
  public Pipeline getPublishPipe();

  /**
   * Set the publish pipe of the Telephone.
   *
   * @param publishPipe the pipeline where you want to publish messages
   */
  public void setPublishPipe(Pipeline publishPipe);

  /**
   * When a call is established the producer and consumer are started
   * by the use of this method. It will create the threads in charge
   * of transmitting and receiving information. 
   */
  public void activateConsumerProducerThreads();

  /**
   * Get the Consumer thread of the telephone.
   *
   * @return a consumer
   */
  public Consumer getConsumer();
  
  /**
   * Get the Producer thread of the telephone.
   *
   * @return a producer
   */
  public Producer getProducer();

  /**
   * Check the status of the phone to see if it is able to
   * use the methods in exchange.
   *
   * @param nameOfFunction the name of the method in exchange
   * @return true if it is able, otherwise false
   */
  public boolean isAbleTo(String nameOfFunction);
}
