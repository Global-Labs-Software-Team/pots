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

  private static Telephone telephoneOne;
  private static Telephone telephoneTwo;
  private static Telephone telephoneThree;
  
  private static int ID1 = 1;
  private static int ID2 = 2;
  private static int ID3 = 3;
  private static int ID_NONEXISTING = 4;

  @BeforeAll
  public static void setUp() throws PhoneExistInNetworkException, InvalidNumberException {
    exchange = Exchange.getInstance();
    telephoneOne = new Telephone(new TelephoneModel(ID1), exchange);
    telephoneTwo = new Telephone(new TelephoneModel(ID2), exchange);
    telephoneThree = new Telephone(new TelephoneModel(ID3), exchange);
  }

  /**
   * Tests the constructor of the class (see 
   * {@link com.globallabs.telephone.Telephone#Telephone(TelephoneModel, Exchange)}).
   */
  @Test
  public void test_constructor() throws PhoneExistInNetworkException, InvalidNumberException {
    Telephone telephoneOne = new Telephone(new TelephoneModel(ID_NONEXISTING), exchange);
    assertEquals(ID_NONEXISTING, telephoneOne.getTelephoneId());
  }

  /**
   * Tests that a phone dials successfully to another phone (see 
   * {@link com.globallabs.telephone.Telephone#dial(int)}).
   */
  @Test
  public void test_dial_success() 
      throws DialingMySelfException, PhoneNotFoundException, BusyPhoneException {
    telephoneOne.dial(ID2);
    Status statustelephoneOne = telephoneOne.getStatus();
    Status statustelephoneTwo = telephoneTwo.getStatus();
    assertEquals(Status.RINGING, statustelephoneTwo);
    assertEquals(Status.DIALING, statustelephoneOne);
  }

  /**
   * Tests that a dial fails if the other phone is busy (see 
   * {@link com.globallabs.telephone.Telephone#dial(int)}).
   */
  @Test
  public void test_dial_busy_phone() 
      throws DialingMySelfException, PhoneNotFoundException, BusyPhoneException {
    telephoneTwo.setStatus(Status.BUSY);
    telephoneOne.dial(ID2);
    Status statusTelephoneOne = telephoneOne.getStatus();
    Status statusTelephoneTwo = telephoneTwo.getStatus();
    assertEquals(Status.OFF_CALL, statusTelephoneOne);
    assertEquals(Status.BUSY, statusTelephoneTwo);
  }

  /**
   * Tests that a phone can't dial itself (see 
   * {@link com.globallabs.telephone.Telephone#dial(int)}).
   */
  @Test
  public void test_dial_myself() {
    assertThrows(DialingMySelfException.class, () -> {
      telephoneOne.dial(1);
    });
  }

  /**
   * Tests that the status of the target phone is ringing when available and the
   * origin phone is dialing (see {@link com.globallabs.telephone.Telephone#dialing()}).
   */
  @Test
  public void test_dialing_phone() throws DialingMySelfException, BusyPhoneException {
    telephoneOne.setStatus(Status.DIALING);
    telephoneOne.setLastCall(telephoneTwo.getTelephoneId());
    telephoneTwo.setStatus(Status.RINGING);
    telephoneTwo.setIncomingCall(telephoneOne.getTelephoneId());
    Runnable runnable = () -> {
      try {
        telephoneOne.dialing();
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
    Status statustelephoneOne = telephoneOne.getStatus();
    Status statustelephoneTwo = telephoneTwo.getStatus();
    assertEquals(Status.DIALING, statustelephoneOne);
    assertEquals(Status.RINGING, statustelephoneTwo);
  }

  /**
   * Tests that after the target phone answers, both phones are busy
   * (see {@link com.globallabs.telephone.Telephone#answer()}).
   */
  @Test
  public void test_answer()
      throws DialingMySelfException, BusyPhoneException, 
      NoIncomingCallsException, NoCommunicationPathException,
      PhoneNotFoundException {
    telephoneOne.setStatus(Status.DIALING);
    telephoneOne.setLastCall(telephoneTwo.getTelephoneId());
    telephoneTwo.setStatus(Status.RINGING);
    telephoneTwo.setIncomingCall(telephoneOne.getTelephoneId());
    Runnable runnable = () -> {
      try {
        telephoneOne.dialing();
      } catch (Exception e) {
        System.out.println(e.toString());
      }
    };
    Thread t = new Thread(runnable);
    t.start();
    telephoneTwo.answer();
    Status statusTelephoneOne = telephoneOne.getStatus();
    Status statusTelephoneTwo = telephoneTwo.getStatus();
    assertEquals(Status.BUSY, statusTelephoneOne);
    assertEquals(Status.BUSY, statusTelephoneTwo);
  }

  /**
   * Test the case when the phone does not
   * have incoming calls and tries to answer a call
   * (see {@link com.globallabs.telephone.Telephone#answer()}).
   */
  @Test
  public void test_answer_without_incomingCall() {
    telephoneOne.setStatus(Status.OFF_CALL);
    telephoneOne.setIncomingCall(Telephone.PHONE_NOT_SET);
    
    assertThrows(NoIncomingCallsException.class, () -> {
      telephoneOne.answer();
    });
  }
  
  /**
   * Test the scenario when there is an ongoing call
   * between two telephones t1, t2 and t1 has an incoming
   * call of t3 and tries to answer it (see 
   * {@link com.globallabs.telephone.Telephone#answer()}).
   */
  @Test
  public void test_answer_whenBusy() {
    telephoneOne.setStatus(Status.BUSY);
    telephoneOne.setLastCall(telephoneTwo.getTelephoneId());
    telephoneOne.setIncomingCall(telephoneThree.getTelephoneId());
    
    telephoneTwo.setStatus(Status.BUSY);
    telephoneTwo.setLastCall(telephoneOne.getTelephoneId());
    
    telephoneThree.setStatus(Status.DIALING);
    telephoneThree.setLastCall(telephoneOne.getTelephoneId());
    
    assertThrows(BusyPhoneException.class, () -> {
      telephoneOne.answer();
    });
  }
  
  /**
   * Test that in at on going call between telephoneOne and telephoneTwo
   * if telephoneOne hang up the call, telephoneOne and telephoneTwo status
   * are OFF_CALL (see {@link com.globallabs.telephone.Telephone#hangUp()}).
   */
  @Test
  public void test_hangUp_ongoingCall() 
      throws NoCommunicationPathException, PhoneNotFoundException {
    telephoneOne.setStatus(Status.BUSY);
    telephoneOne.setLastCall(telephoneTwo.getTelephoneId());
    telephoneTwo.setStatus(Status.BUSY);
    telephoneTwo.setLastCall(telephoneOne.getTelephoneId());
    
    telephoneOne.hangUp();
    assertEquals(Status.OFF_CALL, telephoneOne.getStatus());
    assertEquals(Status.OFF_CALL, telephoneTwo.getStatus());
  }
  
  /**
   * Test that at incoming call from telephoneOne if telephoneTwo
   * cancel the incoming the call the status of telephoneOne
   * and telephoneTwo become OFF_CALL and the variable
   * incomingCall is null (see {@link com.globallabs.telephone.Telephone#hangUp()}).
   */
  @Test
  public void test_hangUp_incomingCall() 
      throws NoCommunicationPathException, PhoneNotFoundException {
    telephoneOne.setStatus(Status.DIALING);
    telephoneOne.setLastCall(telephoneTwo.getTelephoneId());
    telephoneTwo.setStatus(Status.RINGING);
    telephoneTwo.setIncomingCall(telephoneOne.getTelephoneId());
    
    telephoneTwo.hangUp();
    assertEquals(Status.OFF_CALL, telephoneOne.getStatus());
    assertEquals(Status.OFF_CALL, telephoneTwo.getStatus());
    assertEquals(Telephone.PHONE_NOT_SET, telephoneTwo.getIncomingCall());
  }

  /**
   * Test the case when a telephone wants to hang up but is does not have any
   * ongoing calls (see {@link com.globallabs.telephone.Telephone#hangUp()}).
   */
  @Test
  public void test_hangUp_whenThereIsNoCall() {
    telephoneOne.setStatus(Status.OFF_CALL);
    telephoneOne.setLastCall(Telephone.PHONE_NOT_SET);
    assertThrows(NoCommunicationPathException.class, () -> {
      telephoneOne.hangUp();
    });
  }
  
  /**
   * Tests that a phone becomes available if it's dial is not answered 
   * (see {@link com.globallabs.telephone.Telephone#dialing()}).
   */
  @Test
  public void test_dialing_unavailable_phone()
      throws DialingMySelfException, BusyPhoneException, 
      NoIncomingCallsException, InterruptedException {
    telephoneOne.setStatus(Status.DIALING);
    telephoneOne.setLastCall(telephoneTwo.getTelephoneId());
    telephoneTwo.setStatus(Status.RINGING);
    telephoneTwo.setIncomingCall(telephoneOne.getTelephoneId());
    Runnable runnable = () -> {
      try {
        telephoneOne.dialing();
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
    Status statustelephoneOne = telephoneOne.getStatus();
    assertEquals(Status.OFF_CALL, statustelephoneOne);
  }

}
