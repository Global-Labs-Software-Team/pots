/**
 * The class DialingMySelfException is a form of Exception that indicates 
 * that the identifier of the phone receiving the call is the same identifier
 * as the phone performing the call. 
 *
 * <p>Like all the Exceptions, it must be declared in a method or 
 * contructor's throws clause if they can be thrown by the execution. 
 *
 * @since 1.0
 */

package com.globallabs.phoneexceptions;

public class DialingMySelfException extends Exception {

  private static final long serialVersionUID = 1L;
  
  /**
   * Constructs a new DialingMySelfException with a specified message.
   *
   * @param message The message that will be displayed.
   */
  public DialingMySelfException(final String message) {
    super(message);
  }
}
