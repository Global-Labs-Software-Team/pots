package com.globallabs.telephone;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.globallabs.phonedata.TelephoneModel;
import com.globallabs.phoneexceptions.InvalidNumberException;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

public class TelephoneWithPipelineTests {
  private static TelephoneWithPipeline telephoneOne;
  private static int ID1 = 1;
  private static int numberOfTelephoneTwo = 2;
  private static String enrouteCallString = "enrouteCall";
  private static String openCallBetweenString = "openCallBetween";

  @BeforeAll
  public static void setUp() throws InvalidNumberException {
    telephoneOne = new TelephoneWithPipeline(new TelephoneModel(ID1));
  }

  /**
   * A telephone can use the enroute a call (use the enrouteCall method
   * from exchange) iff it is free. This means that it does not have any
   * ongoing call (Status in OFF_CALL) or receiving a call (Incoming Call
   * not set).
   */
  @Test
  public void test_isAbleTo_useEnrouteCallMethod_success() {
    telephoneOne.setStatus(Status.OFF_CALL);
    telephoneOne.setIncomingCall(Telephone.PHONE_NOT_SET);
    assertTrue(telephoneOne.isAbleTo(enrouteCallString));
  }

  /**
   * A telephone cannot use enrouteCall method if.
   * <ul>
   * <li> It is receiving a call (Incoming call set, status RINGING) or
   * <li> It is an ongoing call (Last call set, status BUSY)
   * </ul>
   * 
   */
  @Test
  public void test_isAbleTo_useEnrouteCallMethod_failed() {
    // It is receiving a call
    telephoneOne.setStatus(Status.RINGING);
    telephoneOne.setIncomingCall(numberOfTelephoneTwo);
    assertFalse(telephoneOne.isAbleTo(enrouteCallString));

    // It is busy with an ongoing call
    telephoneOne.setStatus(Status.BUSY);
    assertFalse(telephoneOne.isAbleTo(enrouteCallString));
  }

  /**
   * A telephone can open a communication with the phone that is
   * calling it, iff its status is RINGING and its incoming call variable
   * it is set.
   */
  @Test
  public void test_isAbleTo_useOpenCallBetweenMethod_success() {
    telephoneOne.setStatus(Status.RINGING);
    telephoneOne.setIncomingCall(numberOfTelephoneTwo);

    assertTrue(telephoneOne.isAbleTo(openCallBetweenString));

    assertEquals(Status.RINGING, telephoneOne.getStatus());
    assertEquals(numberOfTelephoneTwo, telephoneOne.getIncomingCall());
  }

  /**
   * A telephone cannot open a communication if it is not receiving a call
   * and its status is not RINGING.
   */
  @Test
  public void test_isAbleTo_useOpenCallBetweenMethod_fail() {
    telephoneOne.setStatus(Status.RINGING);
    telephoneOne.setIncomingCall(Telephone.PHONE_NOT_SET);

    assertFalse(telephoneOne.isAbleTo(openCallBetweenString)); // Incoming call not set

    telephoneOne.setStatus(Status.DIALING);
    telephoneOne.setIncomingCall(numberOfTelephoneTwo);

    assertFalse(telephoneOne.isAbleTo(openCallBetweenString)); // Wrong status
  }
}
