/**
 * The phoneexceiptions package contains the classes of exceptions concerned with phone operations
 *
 * <p>The BusyPhoneException is the class exception that is called when one of 
 * the phones being called is busy. 
 *
 * <p>The DialingMySelfException is the class exception that is called when 
 * the number being dialed is the same as the one calling. 
 * 
 * <p>The InvalidNumberException is the class exception that is called when 
 * the number being dialed does not exist.
 * 
 * <p>The NoCommunicationPathException is the class exception that is called when
 * there does not exist a connection between the two numbers.
 * 
 * <p>The NoIncomingCallsException is the class exception that is called when
 * attempting to answer a call when no one is calling that number.
 * 
 * <p>The PhoneExistInNetworkException is the class exception that is called when
 * the phone does not exist in the network.
 * 
 * <p>The PhoneNotFoundException is the class exception that is called when
 * the phone could not be found in the network.
 *
 * @since 1.0
 */
package com.globallabs.phoneexceptions;