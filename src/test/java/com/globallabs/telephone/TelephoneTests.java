package com.globallabs.telephone;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.concurrent.TimeUnit;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.globallabs.operator.Exchange;
import com.globallabs.phonedata.TelephoneModel;
import com.globallabs.phoneexceptions.BusyPhoneException;
import com.globallabs.phoneexceptions.DialingMySelfException;
import com.globallabs.phoneexceptions.NoCommunicationPathException;
import com.globallabs.phoneexceptions.NoIncomingCallsException;
import com.globallabs.phoneexceptions.PhoneExistInNetworkException;
import com.globallabs.phoneexceptions.PhoneNotFoundException;
import com.globallabs.telephone.Status;
import com.globallabs.telephone.Telephone;

class TelephoneTests {
    
private Exchange exchange;
	
	private Telephone phone1;
	private Telephone phone2;
	
	@BeforeEach
	public void setUp() throws PhoneExistInNetworkException {
		exchange = new Exchange();
		phone1 = new Telephone(new TelephoneModel(1), exchange);
		phone2 = new Telephone(new TelephoneModel(2), exchange);
	}
	
	/**
	 * Test the constructor of the class
	 * @throws PhoneExistInNetworkException
	 */
	@Test
	public void test_constructor() throws PhoneExistInNetworkException {
		Telephone phone1 = new Telephone(new TelephoneModel(4), exchange);
		assertEquals(4, phone1.getId());
	}
	
	/**
	 * Test that the phone dialed successfully to another phone
	 */
	@Test
	public void test_dial_success() throws DialingMySelfException, PhoneNotFoundException, BusyPhoneException {
		phone1.dial(2);
		Status statusPhone1 = phone1.getStatus();
		Status statusPhone2 = phone2.getStatus();
		assertEquals(Status.RINGING, statusPhone2);
		assertEquals(Status.DIALING, statusPhone1);
	}
	
	/**
	 * Test that a dial failed if the other phone
	 * is busy
	 */
	@Test
	public void test_dial_busy_phone() throws DialingMySelfException, PhoneNotFoundException, BusyPhoneException {
		phone2.setStatus(Status.BUSY);
		assertThrows(BusyPhoneException.class, () -> {phone1.dial(2);});
		Status statusPhone1 = phone1.getStatus();
		Status statusPhone2 = phone2.getStatus();
		assertEquals(Status.BUSY, statusPhone2);
		assertEquals(Status.OFF_CALL, statusPhone1);
	}
	
	@Test
	public void test_dial_myself() {
		assertThrows(DialingMySelfException.class, () -> {phone1.dial(1);});
	}

	/**
	 * Test that the status of the target phone is ringing when available
	 * and the origin phone is dialing
	 */
	@Test
	public void test_dialing_phone() throws DialingMySelfException, BusyPhoneException {
		phone1.setStatus(Status.DIALING);
		phone1.setLastCall(phone2);
		phone2.setStatus(Status.RINGING);
		phone2.setIncomingCall(phone1);
		Runnable runnable =
		        () -> { try { phone1.dialing(); } catch(Exception e) {}};
		Thread t = new Thread(runnable);
		t.start();
		try {
			TimeUnit.SECONDS.sleep(1);
		} catch(Exception e) {};
		Status statusPhone1 = phone1.getStatus();
		Status statusPhone2 = phone2.getStatus();
		assertEquals(Status.DIALING, statusPhone1);
		assertEquals(Status.RINGING, statusPhone2);
	}

	/**
	 * Test that after the target phone answers, both phones are busy
	 */
	@Test
	public void test_answer() throws DialingMySelfException, BusyPhoneException, NoIncomingCallsException, NoCommunicationPathException {
		phone1.setStatus(Status.DIALING);
		phone1.setLastCall(phone2);
		phone2.setStatus(Status.RINGING);
		phone2.setIncomingCall(phone1);
		Runnable runnable =
		        () -> { try { phone1.dialing(); } catch(Exception e) {}};
		Thread t = new Thread(runnable);
		t.start();
		phone2.answer();
		Status statusPhone1 = phone1.getStatus();
		Status statusPhone2 = phone2.getStatus();
		assertEquals(Status.BUSY, statusPhone1);
		assertEquals(Status.BUSY, statusPhone2);
	}

	/**
	 * Test that after dialing a busy phone
	 * is busy
	 */
	@Test
	public void test_dialing_unavailable_phone() throws DialingMySelfException, BusyPhoneException, NoIncomingCallsException, InterruptedException {
		phone1.setStatus(Status.DIALING);
		phone1.setLastCall(phone2);
		phone2.setStatus(Status.RINGING);
		phone2.setIncomingCall(phone1);
		Runnable runnable =
		        () -> { try { phone1.dialing(); } catch(Exception e) {}};
		Thread t = new Thread(runnable);
		t.start();
		try {
			TimeUnit.SECONDS.sleep(11);
		} catch(Exception e) {};
		Status statusPhone1 = phone1.getStatus();
		assertEquals(Status.OFF_CALL, statusPhone1);
	}

}
