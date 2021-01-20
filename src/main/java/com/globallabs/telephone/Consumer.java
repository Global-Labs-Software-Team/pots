package com.globallabs.telephone;

import com.globallabs.operator.Pipeline;
import java.util.LinkedList;
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
  LinkedList<Integer> bitsReceived;
  Pipeline toConsume;
  int time;
  TelephoneWithPipeline phone;

  /**
   * Consumer constructor.
   */
  public Consumer(String str, Pipeline toConsume, LinkedList<Integer> bitsReceived, int seconds) {
    super(str);
    this.bitsReceived = bitsReceived;
    this.toConsume = toConsume;
    time = seconds;
  }

  /**
   * Consumer constructor in the case of three or more phones connected.
   */
  public Consumer(String str, Pipeline toConsume, int seconds) {
    super(str);
    this.bitsReceived = new LinkedList<Integer>();
    this.toConsume = toConsume;
    time = seconds;
  }

  /**
   * Consumer constructor in the case of three or more phones connected.
   */
  public Consumer(String str, Pipeline toConsume, TelephoneWithPipeline phone) {
    super(str);
    this.bitsReceived = new LinkedList<Integer>();
    this.toConsume = toConsume;
    this.phone = phone;
  }


  public LinkedList<Integer> getBitsReceived() {
    return bitsReceived;
  }
  
  /**
   * The run method will be executed when the thread starts.
   */
  public void run() {
    while (phone.getStatus() == Status.BUSY) {
      try {
        sleep((int) (1000));
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
      System.out.println("Bit received from " + toConsume.getPipeName() + " is: 0");
    } catch (NoSuchElementException e) {
      System.out.println("There is no information in the pipe");
    } 
  }
}