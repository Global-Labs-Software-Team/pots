package com.globallabs.telephone;

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
   * <li> It is receiving a call (Incoming call set, status RINGING)
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
}
