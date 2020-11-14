package com.globallabs.pots;

import com.globallabs.operator.Exchange;
import com.globallabs.phonedata.TelephoneModel;
import com.globallabs.phoneexceptions.BusyPhoneException;
import com.globallabs.phoneexceptions.DialingMySelfException;
import com.globallabs.phoneexceptions.NoCommunicationPathException;
import com.globallabs.phoneexceptions.NoIncomingCallsException;
import com.globallabs.phoneexceptions.PhoneExistInNetworkException;
import com.globallabs.phoneexceptions.PhoneNotFoundException;
import com.globallabs.telephone.Telephone;
import java.util.ArrayList;
import java.util.List;
//import org.springframework.boot.SpringApplication;
//import org.springframework.boot.autoconfigure.SpringBootApplication;

//@SpringBootApplication
public class PotsApplication {

  /**
   * Simulation of the pots applications.
   *
   * @param args arguments
   * @throws PhoneExistInNetworkException if the phone you are trying to add already exists
   * @throws BusyPhoneException if the phone you are dialing is on another call
   * @throws PhoneNotFoundException if the phone you are dialing is not part of the network
   * @throws DialingMySelfException if the phone you are dialing is you
   * @throws NoCommunicationPathException if there is no communication path between
   *                                      the phones
   * @throws NoIncomingCallsException if there is no call to answer
  */
  public static void main(String[] args) throws PhoneExistInNetworkException, 
          BusyPhoneException, PhoneNotFoundException, DialingMySelfException,
          NoCommunicationPathException, NoIncomingCallsException {
    // TODO Auto-generated method stub
    Exchange exchange = new Exchange();
    List<Telephone> telephones = new ArrayList<Telephone>();
        
    // Create phones with the numbers: 0, 1, 2, 3
    System.out.println("Telephones in the network:");
    for (int number = 0; number < 4; number++) {
      Telephone telephone = new Telephone(new TelephoneModel(number), exchange);
      telephones.add(telephone);
      System.out.println(telephone);
    }
    System.out.println();
    // Simulating a call from 0 to 1 and 2 to 3
    telephones.get(0).dial(1);
    System.out.println(telephones.get(0) + " is calling " + telephones.get(1));
        
    // Telephone 1 accept the call of telephone 0
    telephones.get(1).answer();
    System.out.println(telephones.get(0) + " is talking " + telephones.get(1));
        
    // Telephone 3 try to call one when it is busy
    try {
      System.out.println(telephones.get(3) + " will try to call " + telephones.get(1));
      telephones.get(3).dial(1);
    } catch (Exception e) {
      System.out.println(telephones.get(3) + " phone call failed: " + e.getMessage());
    }
  }
}
