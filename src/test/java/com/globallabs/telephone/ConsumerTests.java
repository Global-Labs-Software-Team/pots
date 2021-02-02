package com.globallabs.telephone;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.LinkedList;

import com.globallabs.operator.Exchange;
import com.globallabs.operator.Pipeline;
import com.globallabs.phonedata.TelephoneModel;

import org.junit.jupiter.api.BeforeAll;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties.Consumer;


public class ConsumerTests {

    String name = "consumer";
    public static LinkedList<Integer> toConsume = new LinkedList<Integer>();
    toConsume.add(1); //TO DO - randomly designate a stream of bits to 'toConsume'
    toConsume.add(0);

    private static int ID1 = 1;

    private static Consumer consumerOne;
    private static Exchange exchange;
    private static TelephoneWithPipeline telephoneOne;
    private static Pipeline q;

}

 /**
   * Set up all the necessary functionality for the tests. Each time
   * a test is executed a consume pipe and linkedlist of stream data is provided.
   * 
   */

@BeforeAll
public static void setUp() {
    telephoneOne = (TelephoneWithPipeline) new Telephone(new TelephoneModel(ID1), exchange);
    q = telephoneOne.getConsumePipe();
    consumerOne = new Consumer(String name, Pipeline q, LinkedList<Integer> toConsume, TelephoneWithPipeline telephoneOne);
  }

/**
   * Verifies the constructor of the Consumer class for a number that is accepted
   * (see {@link com.globallabs.phonedata.telephone.Consumer}).
   */

@Test 
public void testConstructor() {
    Consumer consumerTwo = new Consumer(String str, Pipeline q, LinkedList<Integer> toConsume, TelephoneWithPipeline telephoneOne);
    assertEquals(consumerTwo.phone.getTelephoneId(),ID1)
}

/**
   * Verifies the receiverBit method works by checking the length of the consume pipeline.
   * It verifies all bits have been consumed.
   * (see {@link com.globallabs.phonedata.telephone.Consumer}).
   */

@Test
public void testConsume() {
        consumerOne.receiveBit();
        AssertsEquals(0,toConsume.length);
}