package com.globallabs.pots;

import com.globallabs.phoneexceptions.BusyPhoneException;
import com.globallabs.phoneexceptions.DialingMySelfException;
import com.globallabs.phoneexceptions.InvalidNumberException;
import com.globallabs.phoneexceptions.NoCommunicationPathException;
import com.globallabs.phoneexceptions.NoIncomingCallsException;
import com.globallabs.phoneexceptions.PhoneExistInNetworkException;
import com.globallabs.phoneexceptions.PhoneNotFoundException;

import org.junit.jupiter.api.Test;

class PotsApplicationTests {
  @Test
  void firstScenario() throws InvalidNumberException, 
      NoIncomingCallsException, NoCommunicationPathException, 
      DialingMySelfException, PhoneNotFoundException, 
      PhoneNotFoundException, BusyPhoneException, PhoneExistInNetworkException {
    PotsApplication.firstScenario();
  }
}
