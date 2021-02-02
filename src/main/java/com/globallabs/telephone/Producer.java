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
  Random rand = new Random();
  TelephoneWithPipeline phone;

  /**
   * Producer constructor.
   * @param str The name of the thread
   * @param q the queue where the phone can publish
   * @param phone information about the status of phone
   */
  public Producer(String str, Pipeline q, TelephoneWithPipeline phone) {
    super(str);
    queue = q;
    this.phone = phone;
  }

  /**
   * The run method will be executed when the thread starts.
   * As long as the phone is in the call (Status.BUSY), it sends a random number into the pipeline
   */
  public void run() {
    int currentMessage;
    while (phone.getStatus() == Status.BUSY) {
      currentMessage = rand.nextInt(2);
      publishMessage(currentMessage);
      try {
        sleep((int) (1000));
      } catch (InterruptedException e) {
        System.out.println(phone.logInfo + "The execution of the thread was interrupted.");
      }
    }
  }

  /**
   * Publishes a message into the pipeline.
   * @param currentMessage the message to be published
   */
  synchronized void publishMessage(int currentMessage) {
    System.out.println(phone.logInfo + "Producer " + getName() + " produced " 
        + Integer.toString(currentMessage));
    queue.publish(currentMessage);
  }
}
