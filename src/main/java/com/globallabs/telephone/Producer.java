package com.globallabs.telephone;

import com.globallabs.operator.Pipeline;
import java.util.Random;

/**
 * Producer class to add fields to a thread in order to manage a queue and the time for
 * which it runs.
 *
 * @version 1.0
 * @author Byron Barkhuizen and Angelica Acosta
 */

public class Producer extends Thread {
  Pipeline queue;
  int time;
  Random rand = new Random();
  TelephoneWithPipeline phone;

  /**
   * Producer constructor.
   */
  public Producer(String str, Pipeline q, int seconds) {
    super(str);
    queue = q;
    time = seconds;
  }

  /**
   * Producer constructor.
   * @param str a
   * @param q b
   * @param seconds c
   * @param phone d
   */
  public Producer(String str, Pipeline q, TelephoneWithPipeline phone) {
    super(str);
    queue = q;
    this.phone = phone;
  }

  /**
   * The run method will be executed when the thread starts.
   */
  public void run() {
    int currentMessage;
    while (phone.getStatus() == Status.BUSY) {
      currentMessage = rand.nextInt(2);
      publishMessage(currentMessage);
      try {
        sleep((int) (1000));
      } catch (InterruptedException e) {
        System.out.println("The execution of the thread was interrupted.");
      }
    }
  }

  synchronized void publishMessage(int currentMessage) {
    System.out.println("Producer " + getName() + " produced " + Integer.toString(currentMessage));
    queue.publish(currentMessage);
  }
}
