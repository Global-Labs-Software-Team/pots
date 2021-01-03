package com.globallabs.telephone;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.globallabs.operator.Exchange;
import com.globallabs.phonedata.TelephoneModel;
import com.globallabs.phoneexceptions.BusyPhoneException;
import com.globallabs.phoneexceptions.DialingMySelfException;
import com.globallabs.phoneexceptions.InvalidNumberException;
import com.globallabs.phoneexceptions.NoCommunicationPathException;
import com.globallabs.phoneexceptions.NoIncomingCallsException;
import com.globallabs.phoneexceptions.PhoneExistInNetworkException;
import com.globallabs.phoneexceptions.PhoneNotFoundException;
import java.util.concurrent.TimeUnit;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;


class TelephoneTests {

  private static Exchange exchange;

  private static Telephone phone1;
  private static Telephone phone2;
  private static Telephone phone3;

  @BeforeAll
  public static void setUp() throws PhoneExistInNetworkException, InvalidNumberException {
    exchange = Exchange.getInstance();
    phone1 = new Telephone(new TelephoneModel(1), exchange);
    phone2 = new Telephone(new TelephoneModel(2), exchange);
    phone3 = new Telephone(new TelephoneModel(3), exchange);
  }

  /**
   * Tests the constructor of the class.
   */
  @Test
  public void test_constructor() throws PhoneExistInNetworkException, InvalidNumberException {
    Telephone phone1 = new Telephone(new TelephoneModel(4), exchange);
    assertEquals(4, phone1.getId());
  }

  /**
   * Tests that a phone dials successfully to another phone.
   */
  @Test
  public void test_dial_success() 
      throws DialingMySelfException, PhoneNotFoundException, BusyPhoneException {
    phone1.dial(2);
    Status statusPhone1 = phone1.getStatus();
    Status statusPhone2 = phone2.getStatus();
    assertEquals(Status.RINGING, statusPhone2);
    assertEquals(Status.DIALING, statusPhone1);
  }

  /**
   * Tests that a dial fails if the other phone is busy.
   */
  @Test
  public void test_dial_busy_phone() 
      throws DialingMySelfException, PhoneNotFoundException, BusyPhoneException {
    phone2.setStatus(Status.BUSY);
    assertThrows(BusyPhoneException.class, () -> {
      phone1.dial(2);
    });
    Status statusPhone1 = phone1.getStatus();
    Status statusPhone2 = phone2.getStatus();
    assertEquals(Status.BUSY, statusPhone2);
    assertEquals(Status.OFF_CALL, statusPhone1);
  }

  /**
   * Tests that a phone can't dial itself.
   */
  @Test
  public void test_dial_myself() {
    assertThrows(DialingMySelfException.class, () -> {
      phone1.dial(1);
    });
  }

  /**
   * Tests that the status of the target phone is ringing when available and the
   * origin phone is dialing.
   */
  @Test
  public void test_dialing_phone() throws DialingMySelfException, BusyPhoneException {
    phone1.setStatus(Status.DIALING);
    phone1.setLastCall(phone2);
    phone2.setStatus(Status.RINGING);
    phone2.setIncomingCall(phone1);
    Runnable runnable = () -> {
      try {
        phone1.dialing();
      } catch (Exception e) {
        System.out.println(e.toString());
      }
    };
    Thread t = new Thread(runnable);
    t.start();
    try {
      TimeUnit.SECONDS.sleep(1);
    } catch (Exception e) {
      System.out.println(e.toString());
    }
    ;
    Status statusPhone1 = phone1.getStatus();
    Status statusPhone2 = phone2.getStatus();
    assertEquals(Status.DIALING, statusPhone1);
    assertEquals(Status.RINGING, statusPhone2);
  }

  /**
   * Tests that after the target phone answers, both phones are busy.
   */
  @Test
  public void test_answer()
      throws DialingMySelfException, BusyPhoneException, 
      NoIncomingCallsException, NoCommunicationPathException,
      PhoneNotFoundException {
    phone1.setStatus(Status.DIALING);
    phone1.setLastCall(phone2);
    phone2.setStatus(Status.RINGING);
    phone2.setIncomingCall(phone1);
    Runnable runnable = () -> {
      try {
        phone1.dialing();
      } catch (Exception e) {
        System.out.println(e.toString());
      }
    };
    Thread t = new Thread(runnable);
    t.start();
    phone2.answer();
    Status statusPhone1 = phone1.getStatus();
    Status statusPhone2 = phone2.getStatus();
    assertEquals(Status.BUSY, statusPhone1);
    assertEquals(Status.BUSY, statusPhone2);
  }

  /**
   * Test the case when the phone does not
   * have incoming calls and try to answer a call.
   */
  @Test
  public void test_answer_without_incomingCall() {
    phone1.setStatus(Status.OFF_CALL);
    phone1.setIncomingCall(null);
    
    assertThrows(NoIncomingCallsException.class, () -> {
      phone1.answer();
    });
  }
  
  /**
   * Test the scenario when there is an ongoing call
   * between two telephones t1, t2 and t1 has an incoming
   * call of t3 and tries to answer it.
   */
  @Test
  public void test_answer_whenBusy() {
    phone1.setStatus(Status.BUSY);
    phone1.setLastCall(phone2);
    phone1.setIncomingCall(phone3);
    
    phone2.setStatus(Status.BUSY);
    phone2.setLastCall(phone1);
    
    phone3.setStatus(Status.DIALING);
    phone3.setLastCall(phone1);
    
    assertThrows(BusyPhoneException.class, () -> {
      phone1.answer();
    });
  }
  
  /**
   * Test that in at on going call between phone1 and phone2
   * if phone1 hang up the call, phone1 and phone2 status
   * are OFF_CALL.
   */
  @Test
  public void test_hangUp_ongoingCall() 
      throws NoCommunicationPathException, PhoneNotFoundException {
    phone1.setStatus(Status.BUSY);
    phone1.setLastCall(phone2);
    phone2.setStatus(Status.BUSY);
    phone2.setLastCall(phone1);
    
    phone1.hangUp();
    assertEquals(Status.OFF_CALL, phone1.getStatus());
    assertEquals(Status.OFF_CALL, phone2.getStatus());
  }
  
  /**
   * Test that at incoming call from phone1 if phone2
   * cancel the incoming the call the status of phone1
   * and phone2 become OFF_CALL and the variable
   * incomingCall is null.
   */
  @Test
  public void test_hangUp_incomingCall() 
      throws NoCommunicationPathException, PhoneNotFoundException {
    phone1.setStatus(Status.DIALING);
    phone1.setLastCall(phone2);
    phone2.setStatus(Status.RINGING);
    phone2.setIncomingCall(phone1);
    
    phone2.hangUp();
    assertEquals(Status.OFF_CALL, phone1.getStatus());
    assertEquals(Status.OFF_CALL, phone2.getStatus());
    assertEquals(null, phone2.getIncomingCall());
  }

  @Test
  public void test_hangUp_whenThereIsNoCall() {
    phone1.setStatus(Status.OFF_CALL);
    phone1.setLastCall(null);
    assertThrows(NoCommunicationPathException.class, () -> {
      phone1.hangUp();
    });
  }
  
  /**
   * Tests that a phone becomes available if it's dial is not answered.
   */
  @Test
  public void test_dialing_unavailable_phone()
      throws DialingMySelfException, BusyPhoneException, 
      NoIncomingCallsException, InterruptedException {
    phone1.setStatus(Status.DIALING);
    phone1.setLastCall(phone2);
    phone2.setStatus(Status.RINGING);
    phone2.setIncomingCall(phone1);
    Runnable runnable = () -> {
      try {
        phone1.dialing();
      } catch (Exception e) {
        System.out.println(e.toString());
      }
    };
    Thread t = new Thread(runnable);
    t.start();
    try {
      TimeUnit.SECONDS.sleep(11);
    } catch (Exception e) {
      System.out.println(e.toString());
    }
    ;
    Status statusPhone1 = phone1.getStatus();
    assertEquals(Status.OFF_CALL, statusPhone1);
  }

}
