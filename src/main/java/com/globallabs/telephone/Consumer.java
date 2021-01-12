package com.globallabs.telephone;

import com.globallabs.operator.Pipeline;
import java.util.ArrayList;
import java.util.NoSuchElementException;

/**
 * Consumer is the class that handles the consumption of a message for a telephone
 * that is in the exchange. It will consume messages from the Pipeline and it will
 * store them into a queue represented as an ArrayList.
 *
 * @since 1.0
 * @author Angelica Acosta
 */

public class Consumer extends Thread {
  ArrayList<Integer> bitsReceived;
  Pipeline toConsume;
  int time;

  /**
   * Consumer constructor.
   */
  public Consumer(String str, Pipeline toConsume, ArrayList<Integer> bitsReceived, int seconds) {
    super(str);
    this.bitsReceived = bitsReceived;
    this.toConsume = toConsume;
    time = seconds;
  }

  /**
   * This will be executed when the thread starts.
   */
  public void run() {
    for (int i = 0; i < time; i++) {
      try {
        sleep((int) (Math.random() * 500));
      } catch (InterruptedException e) {
        System.out.println("The execution of the thread was interrupted.");
      }
      receiveBit();
    }
  }

  void receiveBit() {
    try {
      int infoReceived = toConsume.consume();
      bitsReceived.add(infoReceived); // Change to receive in other part
      // System.out.println("Bit received by " + getName() + " is: 0");
    } catch (NoSuchElementException e) {
      System.out.println("There was a problem with the consumption from the Pipeline.");
    } 
  }
}