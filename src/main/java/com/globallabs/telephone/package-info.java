/**
 * Provides the classes necessary to create a telephone and modify the status status.
 *
 * <p>The Telephone is the object that provides the APi for the telephone
 * along with its constructor and methods. It is an abstraction of
 * TelephoneSpecification.java (see the {@link com.globallabs.abstractions.TelephoneSpecification} 
 * class for more information).
 *
 * @since 1.0
 * @author Byron Barkhuizen
 * 
 * <p>Some important points to consider for the use and operation of the tests are;
 * 1) lastCall can be null if the phone is being initialized, or has not been used
 * 2) incomingCall can be null if the host phone is not being called
 * 
 */
package com.globallabs.telephone;