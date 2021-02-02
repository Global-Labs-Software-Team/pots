package com.globallabs.telephone;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.globallabs.operator.Exchange;
import com.globallabs.phonedata.TelephoneModel;
import com.globallabs.phoneexceptions.BusyPhoneException;
import com.globallabs.phoneexceptions.DialingMySelfException;
import com.globallabs.phoneexceptions.InvalidNumberException;
import com.globallabs.phoneexceptions.NoCommunicationPathException;
import com.globallabs.phoneexceptions.NoIncomingCallsException;
import com.globallabs.phoneexceptions.PhoneExistInNetworkException;
import com.globallabs.phoneexceptions.PhoneNotFoundException;
import java.util.Arrays;
import java.util.LinkedList;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

public class TelephoneWithPipelineTests {
  private static TelephoneWithPipeline telephoneOne;
  private static Exchange exchange = Exchange.getInstance();
  private static int ID1 = 1;
  private static int ID3 = 3;
  private static int ID4 = 4;
  private static int numberOfTelephoneTwo = 2;
  private static String enrouteCallString = "enrouteCall";
  private static String openCallBetweenString = "openCallBetween";
  private static String closeCallBetweenString = "closeCallBetween";

  @BeforeAll
  public static void setUp() throws InvalidNumberException, PhoneExistInNetworkException {
    telephoneOne = new TelephoneWithPipeline(new TelephoneModel(ID1), exchange);
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

  /**
   * There are three situations when a Telephone can close a call.
   * <ul>
   * <li> It is in an ongoing call with another phone: Status BUSY, last call set
   * <li> It is receiving a call. Status RINGING, incoming call set
   * <li> It is making a call. Status DIALING, last call set
   * </ul>
   */
  @Test
  public void test_isAbleTo_useCloseCallBetweenMethod_success() {
    // First scenario
    telephoneOne.setStatus(Status.BUSY);
    telephoneOne.setLastCall(numberOfTelephoneTwo);
    assertTrue(telephoneOne.isAbleTo(closeCallBetweenString), 
        "Invalid state for first scenario");

    // Second scenario
    telephoneOne.setStatus(Status.RINGING);
    telephoneOne.setIncomingCall(numberOfTelephoneTwo);
    assertTrue(telephoneOne.isAbleTo(closeCallBetweenString),
        "Invalid state for second scenario");

    // Third scenario
    telephoneOne.setStatus(Status.DIALING);
    telephoneOne.setLastCall(numberOfTelephoneTwo);
    assertTrue(telephoneOne.isAbleTo(closeCallBetweenString), 
        "Invalid state for third scenario");
  }

  /**
   * Integration test. The scenario is the following two phones are communicating and send 
   * a different stream of information. The test is to see if the information is transmitted
   * correctly.
   *
   * @throws DialingMySelfException If you are calling yourself
   * @throws BusyPhoneException If the other phone is busy
   * @throws NoIncomingCallsException You do not have any receiving call
   * @throws NoCommunicationPathException There is no communication path between the phones
   * @throws PhoneNotFoundException The phone does not exists in the exchange
   * @throws PhoneExistInNetworkException The phone already exist in the network
   * @throws InvalidNumberException the number for the phone is invalid
   */
  @Test
  public void test_communicationBetweenTwoPhones() 
      throws DialingMySelfException, BusyPhoneException, NoIncomingCallsException,
      NoCommunicationPathException, PhoneNotFoundException, PhoneExistInNetworkException, 
      InvalidNumberException {
    // Stream to send
    LinkedList<Integer> streamFromThree = 
        new LinkedList<Integer>(Arrays.asList(0, 1, 0, 1, 0, 1, 1));
    LinkedList<Integer> streamFromFour = 
        new LinkedList<Integer>(Arrays.asList(1, 1, 0, 0, 1, 1, 1));
    
    // Creation of the two phones that will communicate with each other
    TelephoneWithPipelineForTests telephoneThree = 
        new TelephoneWithPipelineForTests(new TelephoneModel(ID3), exchange, streamFromThree);

    TelephoneWithPipelineForTests telephoneFour = 
        new TelephoneWithPipelineForTests(new TelephoneModel(ID4), exchange, streamFromFour);
    
    telephoneThree.dial(ID4); // Telephone Three call telephone Two
    telephoneFour.answer(); // Telephone Four answer the call
    
    // Start the communication between each other
    telephoneThree.start();
    telephoneFour.start();

    // Talking period
    try {
      Thread.sleep(10000);
    } catch (InterruptedException e) {
      System.out.println(e);
    }

    telephoneThree.hangUp();

    LinkedList<Integer> streamReceivedByThree = telephoneThree.getConsumer().getBitsReceived();
    LinkedList<Integer> streamReceivedByFour = telephoneFour.getConsumer().getBitsReceived();
    
    // Verify that the information was send properly
    assertEquals(streamFromThree, streamReceivedByFour);
    assertEquals(streamFromFour, streamReceivedByThree);
  }
}
