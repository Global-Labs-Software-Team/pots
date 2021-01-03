package com.globallabs.operator;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.globallabs.phonedata.TelephoneModel;
import com.globallabs.phoneexceptions.BusyPhoneException;
import com.globallabs.phoneexceptions.InvalidNumberException;
import com.globallabs.phoneexceptions.NoCommunicationPathException;
import com.globallabs.phoneexceptions.PhoneExistInNetworkException;
import com.globallabs.phoneexceptions.PhoneNotFoundException;
import com.globallabs.telephone.Status;
import com.globallabs.telephone.Telephone;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


public class ExchangeTests {
  
  private static ExchangeForTests exchange;
  private static Telephone telephone;
  private static Telephone telephoneTwo;
  
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
    telephone = new Telephone(new TelephoneModel(1), exchange);
    telephoneTwo = new Telephone(new TelephoneModel(2), exchange);
  }
  
  /**
   * Test that a phone is added successfully.
   * The tests checks the updated number of phones in the exchange, 
   * as well as the fact that the new phone can be retrieved by its id
   */
  @Test
  void test_addPhoneToExchange_success() throws PhoneExistInNetworkException, 
      InvalidNumberException, PhoneNotFoundException {
    new Telephone(new TelephoneModel(9), exchange);
    assertEquals(3, exchange.getNumberOfPhones());
    Telephone addedTelephone = exchange.getPhone(9);
    assertEquals(addedTelephone.getId(), 9);
  }
  
  /**
   * Test that a same phone is not added two times
   * in the exchange.
   */
  @Test
  void test_addPhoneToExchange_phoneExists() throws PhoneExistInNetworkException,
      InvalidNumberException {
    assertThrows(PhoneExistInNetworkException.class, () -> {
      new Telephone(new TelephoneModel(1), exchange);
    });
  }
  
  /**
   * Exchange try to create a path between telephone two and telephone one with two as origin.
   * Because one is in "OFF_CALL" then a path can be establish and the status of one is
   * updated to RINGING and the telephone two is set as an Incoming Call of phone one.
   */
  @Test 
  void test_enrouteCall_success() throws BusyPhoneException, 
      PhoneNotFoundException, PhoneExistInNetworkException {
    // Telephone two decides to call telephone one and
    // telephone one is free
    telephone.setStatus(Status.OFF_CALL);
    exchange.enrouteCall(2, 1);
    // The telephone one receive the notification
    assertEquals(Status.RINGING, telephone.getStatus());
    assertEquals(telephoneTwo, telephone.getIncomingCall());
  }
  
  /**
   * Exchange try to create a path between telephone two and one with two as origin
   * of the call. Because one is in a call then the path cannot be created and a
   * BusyPhoneException is thrown.
   */
  @Test
  void test_enrouteCall_when_busy() throws PhoneExistInNetworkException {
    telephone.setStatus(Status.BUSY);

    assertThrows(BusyPhoneException.class, () -> {
      exchange.enrouteCall(2, 1);
    });
  }
  
  /**
   * The exchange try to create a path between the telephone two and one
   * but one does not exist in the network. A PhoneNotFoundExcetion is
   * thrown.
   */
  @Test
  void test_enrouteCall_when_phone_no_exist() {
    Exchange exchange = Exchange.getInstance();
    assertThrows(PhoneNotFoundException.class, () -> {
      exchange.enrouteCall(4, 1);
    });
  }
  
  /**
   * The exchange has a path between the telephone one and telephone two because
   * telephone two is calling telephone one. The exchange will open the communication 
   * updating the status of telephone one to BUSY.
   */
  @Test
  void test_openCallBetween_with_incomingCall() 
      throws PhoneExistInNetworkException, NoCommunicationPathException, PhoneNotFoundException {
    // Two is calling one
    telephoneTwo.setLastCall(telephone); 
    telephone.setStatus(Status.DIALING);
    
    // Telephone one is receiving a call from telephone two
    telephone.setStatus(Status.RINGING);
    telephone.setIncomingCall(telephoneTwo);
    
    //  Telephone one decides to accept the call
    telephone.setStatus(Status.BUSY);
    exchange.openCallBetween(1, 2); // One sends signal to open the call to two
    
    // Then telephone two get the status busy also
    assertEquals(Status.BUSY, telephoneTwo.getStatus());
  }
  
  /**
   * Exchange try to open a communication path that does not exists. For example
   * this can happen when telephone one try to open a call with two but two is
   * not calling him.
   */
  @Test
  void test_openCallBetween_without_incomingCall() {
    telephone.setStatus(Status.OFF_CALL);
    telephone.setIncomingCall(null);
    telephoneTwo.setStatus(Status.OFF_CALL);
    telephoneTwo.setLastCall(null);
    assertThrows(NoCommunicationPathException.class, () -> {
      exchange.openCallBetween(1, 2);
    });
  }
  
  /**
   * Exchange close the call between one and two when one decided
   * to close it.
   */
  @Test
  void test_closeCallBetween_successfully() 
      throws NoCommunicationPathException, PhoneNotFoundException {
    // Set up of the scenario where telephoneOne is in a call with telephoneTwo
    telephone.setLastCall(telephoneTwo);
    telephone.setStatus(Status.BUSY);
    
    telephoneTwo.setLastCall(telephone);
    telephoneTwo.setStatus(Status.BUSY);
    
    // telephoneOne close the call
    exchange.closeCallBetween(1, 2);
    telephone.setStatus(Status.OFF_CALL);
    
    // Verification of status
    assertEquals(Status.OFF_CALL, telephoneTwo.getStatus());
  }
  
  /**
   * Exchange close the communication when a communication path
   * is not open. This mean that telephone1 is dialing telephone2
   * and before telephone2 responds, telephone1 cut the communication
   */
  @Test
  void test_closeCallBetween_when_a_communication_is_not_open() 
      throws NoCommunicationPathException, PhoneNotFoundException {
    // Set up of the scenario
    telephone.setLastCall(telephoneTwo);
    telephone.setStatus(Status.DIALING);
    
    telephoneTwo.setIncomingCall(telephone);
    telephoneTwo.setStatus(Status.RINGING);
    
    // TelephoneOne cancel the call
    exchange.closeCallBetween(1, 2);
    telephone.setStatus(Status.OFF_CALL);
    
    // Verification of status
    assertEquals(Status.OFF_CALL, telephoneTwo.getStatus());
    assertEquals(null, telephoneTwo.getIncomingCall());
  }
  
  /**
   * Exchange try to close a communication path between one and two
   * but there is no path there. So NoCommunicationPathException is thrown
   */
  @Test
  void test_closeCallBetween_when_a_communication_path_does_not_exist() 
      throws PhoneExistInNetworkException, InvalidNumberException {
    Telephone telephoneThree = new Telephone(new TelephoneModel(3), exchange);

    // Set up of the scenario: One is in a call with three
    telephone.setLastCall(telephoneThree);
    telephone.setStatus(Status.BUSY);
    
    telephoneThree.setLastCall(telephone);
    telephoneThree.setStatus(Status.BUSY);

    telephoneTwo.setLastCall(null);
    telephoneTwo.setStatus(Status.OFF_CALL);
    
    // Exchange try to cancel a call between 1 and 2 but there is no connection
    assertThrows(NoCommunicationPathException.class, () -> {
      exchange.closeCallBetween(2, 1);
    });
  }

  /**
   * Test that the getNumberOfPhones works correctly. First resetting
   * the exchange and asserting that the number of phones is zero. After
   * adding a new phone and asserting that the number of phones increases
   * by one.
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
    new Telephone(new TelephoneModel(newPhoneId), exchange); // Add a new phone
    int exchangeExpectedSize = 1;
    assertEquals(exchangeExpectedSize, exchange.getNumberOfPhones());
  }
}
