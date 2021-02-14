package com.globallabs.telephone;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.globallabs.operator.ExchangeForTests;
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
  // private static TelephoneWithPipeline telephoneOne;
  private static ExchangeForTests exchange = ExchangeForTests.getInstance();
  // private static int ID1 = 1;
  private static int ID3 = 3;
  private static int ID4 = 4;

  @BeforeAll
  public static void setUp() throws InvalidNumberException, PhoneExistInNetworkException {
    exchange.resetExchange();
    // telephoneOne = new TelephoneWithPipeline(new TelephoneModel(ID1), exchange);
  }

  /**
   * A telephone cannot use enrouteCall method if.
   * <ul>
   * <li> It is receiving a call (Incoming call set, status RINGING) or
   * <li> It is an ongoing call (Last call set, status BUSY)
   * </ul>
   * 
   */


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
