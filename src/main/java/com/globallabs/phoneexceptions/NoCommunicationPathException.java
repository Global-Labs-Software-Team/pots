package com.globallabs.phoneexceptions;

public class NoCommunicationPathException extends Exception {

  private static final long serialVersionUID = 1L;
  
  public NoCommunicationPathException(String message) {
    super(message);
  }
}
