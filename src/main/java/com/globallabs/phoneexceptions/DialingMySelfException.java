package com.globallabs.phoneexceptions;

public class DialingMySelfException extends Exception {

  private static final long serialVersionUID = 1L;
  
  public DialingMySelfException(final String message) {
    super(message);
  }
}
