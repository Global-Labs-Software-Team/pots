/**
 * The class NoIncomingCallsException is a form of Exception that indicates 
 * that the phone does not have any incomming calls, so it cannot use the
 * answer funtion is not available.
 *
 * <p>Like all the Exceptions, it must be declared in a method or 
 * contructor's throws clause if they can be thrown by the execution. 
 *
 * @since 1.0
 */

package com.globallabs.phoneexceptions;

public class NoIncomingCallsException extends Exception {

  private static final long serialVersionUID = 1L;
  
  /**
   * Constructs a new NoIncomingCallsException with a specified message.
   *
   * @param message The message that will be displayed.
   */
  public NoIncomingCallsException(String message) {
    super(message);
  }
}
