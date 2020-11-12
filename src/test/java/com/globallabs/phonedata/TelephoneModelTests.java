package com.globallabs.phonedata;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

import com.globallabs.phoneexceptions.InvalidNumberException;

public class TelephoneModelTests {
	
	@Test
	void test_constructor_rightNumber() throws InvalidNumberException {
		int number = 1;
		TelephoneModel phone = new TelephoneModel(number);
		assertEquals(number, phone.getId());
	}
	
	@Test
	void test_constructor_negativeNumber() {
		int number = -1;
		assertThrows(InvalidNumberException.class, () -> {new TelephoneModel(number);});
	}
}
