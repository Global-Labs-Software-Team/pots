package com.globallabs.telephone;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.globallabs.operator.ExchangeForTests;
import com.globallabs.phonedata.TelephoneModel;
import com.globallabs.phoneexceptions.DialingMySelfException;
import com.globallabs.phoneexceptions.InvalidNumberException;
import com.globallabs.phoneexceptions.PhoneExistInNetworkException;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

/**
 * Test suite for the new feature CallerID.
 */
public class TelephoneCallerIdTests {
  
  public static ExchangeForTests exchange = ExchangeForTests.getInstance();
  public static int ID1 = 1;
  public static int ID2 = 2;
  public static int ID3 = 3;
  public static TelephoneWithPipeline telephoneOne;
  public static TelephoneWithPipeline telephoneTwo;
  public static TelephoneCallerID telephoneThree;

  /**
   * Populate the exchange with three telephones, two of them are plain telephones
   * and telephone three has the new feature callerID.
   * @throws PhoneExistInNetworkException if the telephone already exists in the exchange
   * @throws InvalidNumberException if the number is invalid
   */
  @BeforeAll
  public static void setUp() throws PhoneExistInNetworkException, InvalidNumberException {
    telephoneOne = new TelephoneWithPipeline(new TelephoneModel(ID1), exchange);
    telephoneTwo = new TelephoneWithPipeline(new TelephoneModel(ID2), exchange);
    telephoneThree = new TelephoneCallerID(
      new TelephoneWithPipeline(new TelephoneModel(ID3), exchange)
    );
  }

  /**
   * In this first test we set up the scenario where
   * telephone one is dialing telephone three. Therefore
   * telephone three (that has the new functionality of 
   * identifying the caller id) can see who is calling him.
   */
  @Test
  public void test_findCallerID_number_one() throws DialingMySelfException {
    telephoneOne.dial(ID3);

    assertEquals(ID1, telephoneThree.findCallerID());
  }
}
