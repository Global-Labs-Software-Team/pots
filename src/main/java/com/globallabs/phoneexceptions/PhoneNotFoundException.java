package com.globallabs.phoneexceptions;

public class PhoneNotFoundException extends Exception {

  private static final long serialVersionUID = 1L;
  
  public PhoneNotFoundException(String message) {
    super(message);
  }
}
