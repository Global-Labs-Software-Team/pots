/**
 * The class NoCommunicationPathException is a form of Exception that indicates 
 * that there is not a communication link in the exchange betweend the phone 
 * performing the call and the phone receiving it. 
 *
 * <p>Like all the Exceptions, it must be declared in a method or 
 * contructor's throws clause if they can be thrown by the execution. 
 *
 * @since 1.0
 */

package com.globallabs.phoneexceptions;

public class NoCommunicationPathException extends Exception {

  private static final long serialVersionUID = 1L;
  
  /**
   * Constructs a new NoCommunicationPathException with a specified message.
   *
   * @param message The message that will be displayed.
   */
  public NoCommunicationPathException(String message) {
    super(message);
  }
}
