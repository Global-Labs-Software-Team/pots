/**
 * The class PhoneNotFoundException is a form of Exception that indicates 
 * that the identifier of the specified phone does not exists in the network.
 *
 * <p>Like all the Exceptions, it must be declared in a method or 
 * contructor's throws clause if they can be thrown by the execution. 
 *
 * @since 1.0
 */

package com.globallabs.phoneexceptions;

public class PhoneNotFoundException extends Exception {

  private static final long serialVersionUID = 1L;
  
  /**
   * Constructs a new PhoneNotFoundException with a specified message.
   *
   * @param message The message that will be displayed.
   */
  public PhoneNotFoundException(String message) {
    super(message);
  }
}
