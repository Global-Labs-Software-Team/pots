package com.globallabs.pots;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.globallabs.operator.Exchange;
import com.globallabs.phoneexceptions.*;

import java.lang.Thread;
import java.util.concurrent.TimeUnit;

public class TelephoneDeviceTests {
	private Exchange exchange;
	
	private TelephoneDevice phone1;
	private TelephoneDevice phone2;
	
	@BeforeEach
	public void setUp() throws PhoneExistInNetworkException {
		exchange = new Exchange();
		phone1 = new TelephoneDevice(new Telephone(1), exchange);
		phone2 = new TelephoneDevice(new Telephone(2), exchange);
	}
	
	/**
	 * Test the constructor of the class
	 * @throws PhoneExistInNetworkException
	 */
	@Test
	public void test_constructor() throws PhoneExistInNetworkException {
		Telephone phone1 = new Telephone(4);
		TelephoneDevice phone1Device = new TelephoneDevice(phone1, exchange);
		assertEquals(phone1, phone1Device.getPhoneInfo());
	}
	
	/**
	 * Test that the phone dialed successfully to another phone
	 */
	@Test
	public void test_dial_success() throws DialingMySelfException {
		phone1.dial(2);
		Status statusPhone1 = phone1.getPhoneInfo().getStatus();
		Status statusPhone2 = phone2.getPhoneInfo().getStatus();
		assertEquals(Status.RINGING, statusPhone2);
		assertEquals(Status.DIALING, statusPhone1);
	}
	
	/**
	 * Test that a dial failed if the other phone
	 * is busy
	 */
	@Test
	public void test_dial_busy_phone() throws DialingMySelfException {
		phone2.getPhoneInfo().setStatus(Status.BUSY);
		phone1.dial(2);
		Status statusPhone1 = phone1.getPhoneInfo().getStatus();
		Status statusPhone2 = phone2.getPhoneInfo().getStatus();
		assertEquals(Status.BUSY, statusPhone2);
		assertEquals(Status.OFF_CALL, statusPhone1);
	}
	
	@Test
	public void test_dial_myself() {
		assertThrows(DialingMySelfException.class, () -> {phone1.dial(1);});
	}

	/**
	 * Test that a dial failed if the other phone
	 * is busy
	 */
	@Test
	public void test_ring_phone() throws DialingMySelfException, BusyPhoneException {
		phone1.dial(2);
		Runnable runnable =
		        () -> { try {phone1.ring(); } catch(Exception e) {}};
		Thread t = new Thread(runnable);
		t.start();
		try {
			TimeUnit.SECONDS.sleep(1);
		} catch(Exception e) {};
		Status statusPhone1 = phone1.getPhoneInfo().getStatus();
		assertEquals(Status.RINGING, statusPhone1);
	}

	@Test
	public void test_answer() throws DialingMySelfException, BusyPhoneException, NoIncomingCallsException {
		phone1.dial(2);
		phone2.answer();
		Status statusPhone2 = phone2.getPhoneInfo().getStatus();
		assertEquals(Status.BUSY, statusPhone2);
	}

	/**
	 * Test that a dial failed if the other phone
	 * is busy
	 */
	@Test
	public void test_ring_unavailable_phone() throws DialingMySelfException, BusyPhoneException, NoIncomingCallsException, InterruptedException {
		phone1.dial(2);
		phone1.ring();
		Thread.sleep(11000);
		Status statusPhone1 = phone1.getPhoneInfo().getStatus();
		assertEquals(Status.OFF_CALL, statusPhone1);
	}

	
}
