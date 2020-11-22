package com.globallabs.phoneexceptions;

public class NoIncomingCallsException extends Exception {

  private static final long serialVersionUID = 1L;
  
  public NoIncomingCallsException(String message) {
    super(message);
  }
}
