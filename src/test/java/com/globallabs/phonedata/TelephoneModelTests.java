package com.globallabs.phonedata;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.globallabs.phoneexceptions.InvalidNumberException;
import org.junit.jupiter.api.Test;


public class TelephoneModelTests {
  
  /**
   * Verifies the constructor of the Telephone model for a number that is accepted
   * (see {@link com.globallabs.phonedata.TelephoneModel#TelephoneModel(int)}).
   */
  @Test
  void test_constructor_rightNumber() throws InvalidNumberException {
    int number = 1;
    TelephoneModel phone = new TelephoneModel(number);
    assertEquals(number, phone.getId());
  }
  
  /**
   * Verifies the constructor of the Telephone model for a number that is not
   * allowed (see {@link com.globallabs.phonedata.TelephoneModel#TelephoneModel(int)}).
   */
  @Test
  void test_constructor_negativeNumber() {
    int number = -1;
    assertThrows(InvalidNumberException.class, () -> {
      new TelephoneModel(number);
    });
  }
  
  /**
   * Verifies that two TelephoneModels created with the same id are identical
   * (see {@link com.globallabs.phonedata.TelephoneModel#TelephoneModel(int)}).
   */
  @Test
  void test_equals_success() throws InvalidNumberException {
    int number = 1;
    TelephoneModel phoneOne = new TelephoneModel(number);
    TelephoneModel phoneTwo = new TelephoneModel(number);
    assertTrue(phoneOne.equals(phoneTwo));
  }
  
  /**
   * Verifies that two TelephoneModels created with the differtent ids are different
   * (see {@link com.globallabs.phonedata.TelephoneModel#TelephoneModel(int)}).
   */
  @Test
  void test_equals_failure() throws InvalidNumberException {
    int numberOne = 1;
    int numberTwo = 2;
    TelephoneModel phoneOne = new TelephoneModel(numberOne);
    TelephoneModel phoneTwo = new TelephoneModel(numberTwo);
    assertFalse(phoneOne.equals(phoneTwo));
  }
  
  @Test
  void test_equals_comparingMyself() throws InvalidNumberException {
    int numberOne = 1;
    TelephoneModel phoneOne = new TelephoneModel(numberOne);
    assertTrue(phoneOne.equals(phoneOne));
  }
  
  @Test
  void test_equals_comparingWithOtherObject() throws InvalidNumberException {
    int numberOne = 1;
    TelephoneModel phoneOne = new TelephoneModel(numberOne);
    assertFalse(phoneOne.equals(numberOne));
  }
  
  /**
   * Verifies the correct behaviour of the toString method in TelephoneModel
   * (see {@link com.globallabs.phonedata.TelephoneModel#toString()}).
   */
  @Test
  void test_toString() throws InvalidNumberException {
    int numberOne = 1;
    TelephoneModel phoneOne = new TelephoneModel(numberOne);
    String expected = "Telephone (" + numberOne + ")";
    assertEquals(expected, phoneOne.toString());
  }
}
