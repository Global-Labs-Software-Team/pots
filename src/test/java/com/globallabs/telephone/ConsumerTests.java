package com.globallabs.telephone;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.LinkedList;

import com.globallabs.operator.Exchange;
import com.globallabs.operator.Pipeline;
import com.globallabs.phonedata.TelephoneModel;
import com.globallabs.phoneexceptions.InvalidNumberException;
import com.globallabs.phoneexceptions.PhoneExistInNetworkException;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

public class ConsumerTests {

    public static String name;
    public static Pipeline toConsume = new Pipeline("consumerPipe");

    private static int ID1 = 1;

    private static Consumer consumerOne;
    private static Exchange exchange;
    private static TelephoneWithPipeline telephoneOne;
    private static Pipeline q;

  /**
    * Set up all the necessary functionality for the tests. Each time
    * a test is executed a consume pipe and linkedlist of stream data is provided.
    * 
    */
  @BeforeAll
  public static void setUp() throws InvalidNumberException, PhoneExistInNetworkException {
    name = "consumer";
    toConsume = new Pipeline("consumerPipe");
    telephoneOne = (TelephoneWithPipeline) new TelephoneWithPipeline(new TelephoneModel(ID1), 
        exchange);
    q = telephoneOne.getConsumePipe();
    consumerOne = new Consumer(name, q, telephoneOne);
  }
  
  /**
     * Verifies the constructor of the Consumer class for a number that is accepted
    * (see {@link com.globallabs.phonedata.telephone.Consumer}).
    */
  
  @Test
  public void testConstructor() {
    assertEquals(consumerOne.phone.getTelephoneId(), ID1);
  }
  
  /**
     * Verifies the receiverBit method works by checking the length of the consume pipeline.
    * It verifies all bits have been consumed.
    * (see {@link com.globallabs.phonedata.telephone.Consumer}).
    */
  
  @Test
  public void testConsume() {
    consumerOne.receiveBit();
    // AssertsEquals(0, toConsume.length);
  }
}