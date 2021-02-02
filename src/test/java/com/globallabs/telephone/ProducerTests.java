package com.globallabs.telephone;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.globallabs.operator.Exchange;
import com.globallabs.operator.Pipeline;
import com.globallabs.phonedata.TelephoneModel;
import com.globallabs.phoneexceptions.InvalidNumberException;
import com.globallabs.phoneexceptions.PhoneExistInNetworkException;
import java.util.LinkedList;
import java.util.NoSuchElementException;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

/**
 * Test suite for the {@link com.globallabs.telephone.Producer} class.
 * 
 * @version 1.0
 * @author Ilia Didenko
 */

class ProducerTests {
  private static Producer producer;
  private static Pipeline pipeline;
  private static TelephoneWithPipeline phone;
  private static int PhoneID = 1;

  @BeforeAll
  public static void setUp() throws PhoneExistInNetworkException, InvalidNumberException {
    pipeline = new Pipeline("Test Pipeline");
    phone = new TelephoneWithPipeline(new TelephoneModel(PhoneID), 
      Exchange.getInstance(), pipeline);

    producer = new Producer("Test Producer", pipeline, phone);
  }

  /** 
   * Tests the constuctor of the class.
   * (see {@link com.globallabs.telephone.Producer#Producer(String,Pipeline,TelephoneWithPipeline)})
   */
  @Test
  public void test_constructor() {
    String producerName = "New Producer";
    Producer newProducer = new Producer(producerName, pipeline, phone);
    assertEquals(newProducer.getName(), producerName);
    assertEquals(newProducer.queue.getPipeName(), pipeline.getPipeName());
    assertEquals(PhoneID, newProducer.phone.getId());
  }

  /**
   * Tests that the producer can be activated.
   * The thread is activated and the status of the phone is set to BUSY for 2 seconds.
   * The thread is expected to send at least 1 message during this time, 
   * meaining we can consume it in the pipeline.
   * (see {@link com.globallabs.telephone.Producer#run()})
   */
  @Test
  public void test_run() {
    phone.setStatus(Status.BUSY);
    producer.start();
    try {
      Thread.sleep(2000);
    } catch (InterruptedException e) {
      System.out.println("Execution was interrupted: " + e.toString());
    }
    phone.setStatus(Status.OFF_CALL);
    assertDoesNotThrow(() -> {
      pipeline.consume(); });
    
  }

  /**
   * Tests the correctness of publishMessage.
   * We send 2 values and a consumer receives them. The values are checked against
   * the values that were sent. And we check that the pipeline is empty afterwards.
   * (see {@link com.globallabs.telephone.Producer#publishMessage()})
   */
  @Test
  public void test_publishMessage() {
    LinkedList<Integer> toSend = new LinkedList<Integer>();
    toSend.add(0);
    toSend.add(1);

    Consumer consumer = new Consumer("Test Consumer", pipeline, phone);
    
    for (Integer message : toSend) {
      producer.publishMessage(message);
      consumer.receiveBit();
    }

    assertEquals(toSend, consumer.getBitsReceived());

    assertThrows(NoSuchElementException.class, () -> { 
      pipeline.consume(); });
  }
}
