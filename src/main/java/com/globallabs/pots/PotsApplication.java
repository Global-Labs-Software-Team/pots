package com.globallabs.pots;

import com.globallabs.operator.Exchange;
import com.globallabs.phonedata.TelephoneModel;
import com.globallabs.phoneexceptions.BusyPhoneException;
import com.globallabs.phoneexceptions.DialingMySelfException;
import com.globallabs.phoneexceptions.InvalidNumberException;
import com.globallabs.phoneexceptions.NoCommunicationPathException;
import com.globallabs.phoneexceptions.NoIncomingCallsException;
import com.globallabs.phoneexceptions.PhoneExistInNetworkException;
import com.globallabs.phoneexceptions.PhoneNotFoundException;
import com.globallabs.telephone.Telephone;
import com.globallabs.telephone.TelephoneWithPipeline;

//import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * PotsApplication is the class that allows to simulate the use of the Telephone application.
 * It runs scenarious that simulate different use cases of the application
 *
 * @since 1.0
 */
@SpringBootApplication
public class PotsApplication {

  public static Exchange exchange = Exchange.getInstance();
  
  /**
   * The first scenario is the simplest one. It consists
   * of two phones t1 and t2 that call each other. t1 dial the
   * t2 number, t2 answer the call, they talk for a moment and then
   * t2 hang up the call.
   *
   * @throws PhoneExistInNetworkException If the phone already exist in the network
   * @throws BusyPhoneException If the phone is busy
   * @throws PhoneNotFoundException If the phone is not in the network
   * @throws DialingMySelfException If you are calling yourself
   * @throws NoCommunicationPathException There is no path between the phones
   * @throws NoIncomingCallsException If you do not have a call to answer
   */
  public static void firstScenario() throws PhoneExistInNetworkException, 
      BusyPhoneException, PhoneNotFoundException, DialingMySelfException,
      NoCommunicationPathException, NoIncomingCallsException, InvalidNumberException {
    Exchange exchange = Exchange.getInstance();
    Telephone t1 = new Telephone(new TelephoneModel(1), exchange);
    Telephone t2 = new Telephone(new TelephoneModel(2), exchange);

    t1.dial(2); // t1 dial t2
    System.out.println(t1 + "\nis dialing\n" + t2);
    System.out.println("------------------------");
    t2.answer(); // t2 pick up the phone
    System.out.println(t2 + "\nanswered\n" + t1);
    System.out.println("------------------------");
    // They talk for a while and then t2 hang up the call
    t2.hangUp();
    System.out.println(t2 + "\nhang up\n" + t1);
  }

  /**
   * Simple scenario where two telephones are talking with each other.
   */
  public static void secondScenario() 
      throws InvalidNumberException, PhoneExistInNetworkException, DialingMySelfException,
      PhoneNotFoundException, BusyPhoneException, NoIncomingCallsException, 
      NoCommunicationPathException {
    TelephoneModel myinfoOne = new TelephoneModel(1);
    TelephoneWithPipeline telephoneOne = new TelephoneWithPipeline(myinfoOne, exchange);
    TelephoneModel myinfoTwo = new TelephoneModel(2);
    TelephoneWithPipeline telephoneTwo = new TelephoneWithPipeline(myinfoTwo, exchange);
    telephoneOne.start();
    telephoneTwo.start();

    telephoneOne.dial(2);
    try {
      Thread.sleep(100);
    } catch (InterruptedException e) {
    }
    telephoneTwo.answer();
  }

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
          NoCommunicationPathException, NoIncomingCallsException, InvalidNumberException {
    // firstScenario();
    secondScenario(); // Connection between two telephone and transfer of data;
  }
}
