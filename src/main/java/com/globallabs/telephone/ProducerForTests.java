package com.globallabs.telephone;

import com.globallabs.operator.Pipeline;
import java.util.LinkedList;

public class ProducerForTests extends Producer {

  LinkedList<Integer> streamToSend;

  public ProducerForTests(String str, Pipeline q, LinkedList<Integer> streamToSend, int seconds) {
    super(str, q, seconds);
    this.streamToSend = streamToSend;
  }

  @Override
  public void run() {
    for (int bit : streamToSend) {
      try {
        sleep(1000);
        publishMessage(bit);
      } catch (InterruptedException e) {
        System.out.println(e);
      }
    }
  }
}
