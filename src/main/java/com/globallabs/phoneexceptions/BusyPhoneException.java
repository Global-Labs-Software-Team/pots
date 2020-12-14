/**
 * The class BusyPhoneException is a form of Exception that indicates 
 * that the phone receiving the call is not available, specifically
 * has a BUSY status. 
 *
 * <p>Like all the Exceptions, it must be declared in a method or 
 * contructor's throws clause if they can be thrown by the execution. 
 *
 * @since 1.0
 */

package com.globallabs.phoneexceptions;

public class BusyPhoneException extends Exception {

  private static final long serialVersionUID = 1L;

  /**
   * Constructs a new BusyPhoneException with a specified message.
   *
   * @param message The message that will be displayed.
   */
  public BusyPhoneException(String message) {
    super(message);
  }
}
