package com.globallabs.telephone;

import com.globallabs.operator.Pipeline;
import java.util.LinkedList;

public class ProducerForTests extends Producer {

  LinkedList<Integer> streamToSend;

  public ProducerForTests(String str, Pipeline q, 
      LinkedList<Integer> streamToSend, TelephoneWithPipeline phone) {
    super(str, q, phone);
    this.streamToSend = streamToSend;
  }

  @Override
  public void run() {
    for (int bit : streamToSend) {
      publishMessage(bit);
    }
  }
}
