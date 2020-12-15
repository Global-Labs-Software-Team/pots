/**
 * The class InvalidNumberException is a form of Exception that indicates 
 * that the identifier of the phone receiving the call has an invalid format. 
 *
 * <p>Like all the Exceptions, it must be declared in a method or 
 * contructor's throws clause if they can be thrown by the execution. 
 *
 * @since 1.0
 */

package com.globallabs.phoneexceptions;

public class InvalidNumberException extends Exception {

  private static final long serialVersionUID = 1L;

  /**
   * Constructs a new InvalidNumberException with a specified message.
   *
   * @param message The message that will be displayed.
   */
  public InvalidNumberException(final String message) {
    super(message);
  }
}
