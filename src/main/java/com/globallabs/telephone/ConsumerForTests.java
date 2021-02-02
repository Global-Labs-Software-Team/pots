package com.globallabs.telephone;

import java.util.LinkedList;

import com.globallabs.operator.Pipeline;

/**
 * The class to be used for running tests on the consumer class. Contains a constructor method with an additional phone 
 * argument and a linkedlist of bits of streaming data to consume for testing purposes.
 * 
 * @since 1.0
 * @author Byron Barkhuizen
 */

public class ConsumerForTests extends Consumer {

    LinkedList<Integer> streamToReceive;
    Pipeline consumePipe;
  
    public ConsumerForTests(String str, Pipeline q, LinkedList<Integer> streamToReceive, TelephoneWithPipeline phone) {
      super(str, q, phone);
      consumePipe = q;
      this.streamToReceive=streamToReceive;
    }
  
    @Override
    public void run() {
        receiveBit();
    }
  }
  