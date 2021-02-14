package com.globallabs.operator;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.globallabs.phonedata.TelephoneModel;
import com.globallabs.phoneexceptions.BusyPhoneException;
import com.globallabs.phoneexceptions.InvalidNumberException;
import com.globallabs.phoneexceptions.NoCommunicationPathException;
import com.globallabs.phoneexceptions.PhoneExistInNetworkException;
import com.globallabs.phoneexceptions.PhoneNotFoundException;
import com.globallabs.telephone.Status;
import com.globallabs.telephone.Telephone;
import com.globallabs.telephone.TelephoneWithPipeline;
import java.util.LinkedList;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


public class ExchangeTests {
  
  private static ExchangeForTests exchange;
  private static TelephoneWithPipeline telephoneOne;
  private static TelephoneWithPipeline telephoneTwo;
  private static Pipeline pipelineOne;
  private static Pipeline pipelineTwo;
  
  /**
   * Set up all the necessary functions for the tests. Each time
   * a test is executed a exchange with two phones registered is
   * provided.
   * 
   * <p>If you are making a test and you want a clean slate you
   * can reset the exchange inside your new test with the resetExchange()
   * method.
   */
  @BeforeEach
  public void setUp() throws PhoneExistInNetworkException, InvalidNumberException {
    exchange = ExchangeForTests.getInstance();
    exchange.resetExchange();
    pipelineOne = new Pipeline("pipe1", new LinkedList<Integer>());
    pipelineTwo = new Pipeline("pipe2", new LinkedList<Integer>());
    telephoneOne = new TelephoneWithPipeline(new TelephoneModel(1), exchange, pipelineOne);
    telephoneTwo = new TelephoneWithPipeline(new TelephoneModel(2), exchange, pipelineTwo);
  }
  
  /**
   * Test that a phone is added successfully.
   * The tests checks the updated number of phones in the exchange, 
   * as well as the fact that the new phone can be retrieved by its id.
   * This test aims to check the correct behaviour of:
   * <li> {@link com.globallabs.telephone.Telephone#Telephone(TelephoneModel, Exchange)}
   * <li> {@link com.globallabs.operator.ExchangeForTests#getPhone(int)}
   */
  @Test
  void test_addPhoneToExchange_success() throws PhoneExistInNetworkException, 
      InvalidNumberException, PhoneNotFoundException {
    int expectedNumberPhonesBefore = 2;
    int expectedNumberPhonesAfter = 3;
    TelephoneWithPipeline newPhone = new TelephoneWithPipeline(new TelephoneModel(9));
    assertEquals(expectedNumberPhonesBefore, exchange.getNumberOfPhones());
    exchange.addPhoneToExchange(newPhone); 
    assertEquals(expectedNumberPhonesAfter, exchange.getNumberOfPhones());
    Telephone addedTelephone = (Telephone) exchange.getPhone(9);
    assertEquals(addedTelephone.getTelephoneId(), newPhone.getTelephoneId());
  }
  
  /**
   * Test that a same phone is not added two times
   * in the exchange.
   * This test aims to check the correct behaviour of:
   * <li> {@link com.globallabs.telephone.Telephone#Telephone(TelephoneModel, Exchange)}
   */
  @Test
  void test_addPhoneToExchange_phoneExists() throws PhoneExistInNetworkException,
      InvalidNumberException {
    int expectedNumberPhonesBefore = 2;
    assertEquals(expectedNumberPhonesBefore, exchange.getNumberOfPhones());
    assertThrows(PhoneExistInNetworkException.class, () -> {
      TelephoneWithPipeline repeatedPhone = new TelephoneWithPipeline(new TelephoneModel(1));
      exchange.addPhoneToExchange(repeatedPhone);
    });
    assertEquals(expectedNumberPhonesBefore, exchange.getNumberOfPhones());
  }
  
  /**
   * Exchange try to create a path between telephone two and telephone one with two as origin.
   * Because one is in "OFF_CALL" then a path can be establish and the status of one is
   * updated to RINGING and the telephone two is set as an Incoming Call of phone one.
   * This test aims to check the correct behaviour of:
   * <li> {@link com.globallabs.operator.ExchangeForTests#enrouteCall(int, int)}
   */
  @Test 
  void test_enrouteCall_success() throws BusyPhoneException, 
      PhoneNotFoundException, PhoneExistInNetworkException {
    // Telephone two decides to call telephone one and
    // telephone one is free
    telephoneOne.setStatus(Status.OFF_CALL);
    exchange.enrouteCall(telephoneTwo.getTelephoneId(), telephoneOne.getTelephoneId());
    // The telephone one receive the notification
    assertEquals(Status.RINGING, telephoneOne.getStatus());
    assertEquals(telephoneTwo.getTelephoneId(), telephoneOne.getIncomingCall());
  }
  
  /**
   * Exchange try to create a path between telephone two and one with two as origin
   * of the call. Because one is in a call then the path cannot be created and a
   * BusyPhoneException is thrown.
   * This test aims to check the correct behaviour of:
   * <li> {@link com.globallabs.operator.ExchangeForTests#enrouteCall(int, int)}
   */
  @Test
  void test_enrouteCall_when_busy() throws PhoneExistInNetworkException {
    telephoneOne.setStatus(Status.BUSY);
    telephoneTwo.setStatus(Status.OFF_CALL);
    assertThrows(BusyPhoneException.class, () -> {
      exchange.enrouteCall(telephoneTwo.getTelephoneId(), telephoneOne.getTelephoneId());
    });
    assertEquals(Status.OFF_CALL, telephoneTwo.getStatus());
    assertEquals(Status.BUSY, telephoneOne.getStatus());
  }
  
  /**
   * The exchange try to create a path between the telephone two and one
   * but one does not exist in the network. A PhoneNotFoundExcetion is
   * thrown.
   * This test aims to check the correct behaviour of:
   * <li> {@link com.globallabs.operator.ExchangeForTests#enrouteCall(int, int)}
   */
  @Test
  void test_enrouteCall_when_phone_no_exist() {
    int phoneNumberNotExists = 4;
    telephoneOne.setStatus(Status.OFF_CALL);
    telephoneOne.setIncomingCall(Telephone.PHONE_NOT_SET);
    assertThrows(PhoneNotFoundException.class, () -> {
      exchange.enrouteCall(phoneNumberNotExists, telephoneOne.getTelephoneId());
    });
    assertEquals(Status.OFF_CALL, telephoneOne.getStatus());
    assertEquals(Telephone.PHONE_NOT_SET, telephoneOne.getIncomingCall());
  }
  
  /**
   * The exchange has a path between the telephone one and telephone two because
   * telephone two is calling telephone one. The exchange will open the communication 
   * updating the status of telephone one to BUSY.
   * This test aims to check the correct behaviour of:
   * <li> {@link com.globallabs.operator.ExchangeForTests#openCallBetween(int, int)}
   */
  @Test
  void test_openCallBetween_with_incomingCall() 
      throws PhoneExistInNetworkException, NoCommunicationPathException, PhoneNotFoundException {
    // Two is calling one
    telephoneTwo.setStatus(Status.DIALING);
    telephoneTwo.setLastCall(telephoneOne.getTelephoneId()); 
    
    // Telephone one is receiving a call from telephone two
    telephoneOne.setStatus(Status.RINGING);
    telephoneOne.setIncomingCall(telephoneTwo.getTelephoneId());
    
    //  Telephone one decides to accept the call
    exchange.openCallBetween(telephoneOne.getTelephoneId(), 
        telephoneTwo.getTelephoneId()); // One sends signal to open the call to two
    telephoneOne.setStatus(Status.BUSY);

    // Then telephone two also get the status busy
    assertEquals(Status.BUSY, telephoneTwo.getStatus());
    assertEquals(Status.BUSY, telephoneOne.getStatus());
    assertEquals(telephoneOne.getLastCall(), telephoneTwo.getTelephoneId());
    assertEquals(telephoneTwo.getLastCall(), telephoneOne.getTelephoneId());
    // Verification of the pipelines setup
    
    assertEquals(telephoneOne.getConsumePipe(), 
        telephoneTwo.getPublishPipe());
    assertEquals(telephoneTwo.getConsumePipe(), 
        telephoneOne.getPublishPipe());
  }
  
  /**
   * Exchange try to open a communication path that does not exists. For example
   * this can happen when telephone one try to open a call with two but two is
   * not calling him.
   * This test aims to check the correct behaviour of:
   * <li> {@link com.globallabs.operator.ExchangeForTests#openCallBetween(int, int)}
   */
  @Test
  void test_openCallBetween_without_incomingCall() {
    telephoneOne.setStatus(Status.OFF_CALL);
    telephoneOne.setIncomingCall(Telephone.PHONE_NOT_SET);
    telephoneTwo.setStatus(Status.OFF_CALL);
    telephoneTwo.setLastCall(Telephone.PHONE_NOT_SET);
    assertThrows(NoCommunicationPathException.class, () -> {
      exchange.openCallBetween(telephoneOne.getTelephoneId(), telephoneTwo.getTelephoneId());
    });

    assertEquals(Status.OFF_CALL, telephoneOne.getStatus());
    assertEquals(Telephone.PHONE_NOT_SET, telephoneOne.getIncomingCall());
    assertEquals(Status.OFF_CALL, telephoneTwo.getStatus());
    assertEquals(Telephone.PHONE_NOT_SET, telephoneTwo.getIncomingCall());
  }
  
  /**
   * Exchange close the call between one and two when one decided
   * to close it.
   * This test aims to check the correct behaviour of:
   * <li> {@link com.globallabs.operator.ExchangeForTests#closeCallBetween(int, int)}
   */
  @Test
  void test_closeCallBetween_successfully() 
      throws NoCommunicationPathException, PhoneNotFoundException {
    // Set up of the scenario where telephone is in a call with telephoneTwo
    telephoneOne.setLastCall(telephoneTwo.getTelephoneId());
    telephoneOne.setStatus(Status.BUSY);
    
    telephoneTwo.setLastCall(telephoneOne.getTelephoneId());
    telephoneTwo.setStatus(Status.BUSY);
    
    // telephoneOne close the call
    exchange.closeCallBetween(telephoneOne.getTelephoneId(), telephoneTwo.getTelephoneId());
    telephoneOne.setStatus(Status.OFF_CALL);
    
    // Verification of status
    assertEquals(Status.OFF_CALL, telephoneTwo.getStatus());
    assertEquals(Telephone.PHONE_NOT_SET, telephoneTwo.getIncomingCall());
    // Verification of the pipelines setup
    assertNull(telephoneOne.getConsumePipe());
    assertNull(telephoneTwo.getConsumePipe());
  }
  
  /**
   * Exchange close the communication when a communication path
   * is not open. This mean that telephone1 is dialing telephone two
   * and before telephone two responds, telephone one cuts the communication.
   * This test aims to check the correct behaviour of:
   * <li> {@link com.globallabs.operator.ExchangeForTests#closeCallBetween(int, int)}
   */
  @Test
  void test_closeCallBetween_when_a_communication_is_not_open() 
      throws NoCommunicationPathException, PhoneNotFoundException {
    // Set up of the scenario
    telephoneOne.setLastCall(telephoneTwo.getTelephoneId());
    telephoneOne.setStatus(Status.DIALING);
    
    telephoneTwo.setIncomingCall(telephoneOne.getTelephoneId());
    telephoneTwo.setStatus(Status.RINGING);
    
    // TelephoneOne cancel the call
    exchange.closeCallBetween(1, 2);
    telephoneOne.setStatus(Status.OFF_CALL);
    
    // Verification of status
    assertEquals(Status.OFF_CALL, telephoneTwo.getStatus());
    assertEquals(Telephone.PHONE_NOT_SET, telephoneTwo.getIncomingCall());
  }
  
  /**
   * Exchange try to close a communication path between one and two
   * but there is no path there. So NoCommunicationPathException is thrown.
   * This test aims to check the correct behaviour of:
   * <li> {@link com.globallabs.operator.ExchangeForTests#closeCallBetween(int, int)}
   */
  @Test
  void test_closeCallBetween_when_a_communication_path_does_not_exist() 
      throws PhoneExistInNetworkException, InvalidNumberException {
    Telephone telephoneThree = new Telephone(new TelephoneModel(3), exchange);

    // Set up of the scenario: One is in a call with three
    telephoneOne.setLastCall(telephoneThree.getTelephoneId());
    telephoneOne.setStatus(Status.BUSY);
    
    telephoneThree.setLastCall(telephoneOne.getTelephoneId());
    telephoneThree.setStatus(Status.BUSY);

    telephoneTwo.setLastCall(Telephone.PHONE_NOT_SET);
    telephoneTwo.setStatus(Status.OFF_CALL);
    
    // Exchange try to cancel a call between 1 and 2 but there is no connection
    assertThrows(NoCommunicationPathException.class, () -> {
      exchange.closeCallBetween(telephoneTwo.getTelephoneId(), telephoneOne.getTelephoneId());
    });

    assertEquals(Status.BUSY, telephoneOne.getStatus());
    assertEquals(telephoneThree.getTelephoneId(), telephoneOne.getLastCall());
    assertEquals(Status.BUSY, telephoneThree.getStatus());
    assertEquals(telephoneOne.getTelephoneId(), telephoneThree.getLastCall());
    assertEquals(Status.OFF_CALL, telephoneTwo.getStatus());
    assertEquals(Telephone.PHONE_NOT_SET, telephoneTwo.getLastCall());
  }

  /**
   * Test that the getNumberOfPhones works correctly. First resetting
   * the exchange and asserting that the number of phones is zero. After
   * adding a new phone and asserting that the number of phones increases
   * by one.
   * This test aims to check the correct behaviour of:
   * <li> {@link com.globallabs.operator.ExchangeForTests#getNumberOfPhones()}
   *
   * @throws InvalidNumberException if the number is invalid. Less than zero
   * @throws PhoneExistInNetworkException if the number already in the list
   mantained by exchange.
   */
  @Test
  void test_getNumberOfPhones() 
      throws InvalidNumberException, PhoneExistInNetworkException {
    int emptyList = 0;
    exchange.resetExchange(); // Empty the list. size = 0
    assertEquals(emptyList, exchange.getNumberOfPhones());
    
    int newPhoneId = 9;
    new TelephoneWithPipeline(new TelephoneModel(newPhoneId), exchange); // Add a new phone
    int exchangeExpectedSize = 1;
    assertEquals(exchangeExpectedSize, exchange.getNumberOfPhones());
  }

  /**
   * The scenario:
   * 
   * <p>There is two telephones: t1 and t2. t1 is currently talking
   * to t2, so t1 and t2 status are BUSY and t1 last call is t2 and
   * t2 last call is t1. So the function will return true.
   */
  @Test
  void test_communicationExists_scenario_one() {
    // Setting the scenario
    telephoneOne.setStatus(Status.BUSY);
    telephoneOne.setLastCall(telephoneTwo.getTelephoneId());
    telephoneTwo.setStatus(Status.BUSY);
    telephoneTwo.setLastCall(telephoneOne.getTelephoneId());

    assertTrue(exchange.communicationExists(telephoneOne, telephoneTwo));
    assertTrue(exchange.communicationExists(telephoneTwo, telephoneOne), 
        "The method must work independently of the position of the parameters");

    // Postconditions, the same state have to be mantain for both telephones
    assertEquals(Status.BUSY, telephoneOne.getStatus());
    assertEquals(telephoneTwo.getTelephoneId(), telephoneOne.getLastCall());
    assertEquals(Status.BUSY, telephoneTwo.getStatus());
    assertEquals(telephoneOne.getTelephoneId(), telephoneTwo.getLastCall());
  }

  /**
   * The scenario:
   * 
   * <p>There is two telephone: t1 and t2. t1 is currently dialing
   * t2, so t1 status is DIALING and t1 last call t2 and t2 is RINGING
   * and the incoming call is t1. So there is a communication, the
   * function must return true.
   */
  @Test
  void test_communicationExists_scenario_two() {
    // Setting the scenario
    telephoneOne.setStatus(Status.DIALING);
    telephoneOne.setLastCall(telephoneTwo.getTelephoneId());
    telephoneTwo.setStatus(Status.RINGING);
    telephoneTwo.setIncomingCall(telephoneOne.getTelephoneId());

    assertTrue(exchange.communicationExists(telephoneOne, telephoneTwo));
    assertTrue(exchange.communicationExists(telephoneTwo, telephoneOne), 
        "The method must work independently of the position of the parameters");

    assertEquals(Status.DIALING, telephoneOne.getStatus());
    assertEquals(telephoneTwo.getTelephoneId(), telephoneOne.getLastCall());
    assertEquals(Status.RINGING, telephoneTwo.getStatus());
    assertEquals(telephoneOne.getTelephoneId(), telephoneTwo.getIncomingCall());
  }

  /**
   * The scenario:
   * 
   * <p>In here the two telephones statuses do not enter in any of the
   * cases exposed in the previous two scenarios to have a communication.
   * The method must return false in these cases
   */
  @Test
  void test_communicationExists_scenario_three() {
    telephoneOne.setStatus(Status.DIALING);
    telephoneOne.setLastCall(telephoneTwo.getTelephoneId());
    telephoneTwo.setStatus(Status.OFF_CALL);
    telephoneTwo.setIncomingCall(telephoneOne.getTelephoneId());

    assertFalse(exchange.communicationExists(telephoneOne, telephoneTwo));
    assertFalse(exchange.communicationExists(telephoneTwo, telephoneTwo), 
        "The method must work independently of the position of the parameters");
  }
}
