package com.globallabs.phoneexceptions;

public class PhoneExistInNetworkException extends Exception {

  private static final long serialVersionUID = 1L;
  
  public PhoneExistInNetworkException(String message) {
    super(message);
  }
}
