package com.globallabs.phoneexceptions;

public class BusyPhoneException extends Exception {
	
	private static final long serialVersionUID = 1L;
	
	public BusyPhoneException(String message) {
		super(message);
	}
}
