package com.globallabs.phoneexceptions;

public class InvalidNumberException extends Exception {

  private static final long serialVersionUID = 1L;
  
  public InvalidNumberException(final String message) {
    super(message);
  }
}
