package com.globallabs.telephone;

/**
 * The different status that a phone can have.
 * <ul>
 * <li> BUSY: When the phone is in a call with another phone.
 * <li> OFF_CALL: When the phone is free.
 * <li> RINGING: When is receiving a call from another peer.
 * <li> DIALING: When the phone is dialing another phone.
 * </ul>
 *
 * @author danielrs
 */
public enum Status {
  BUSY,
  OFF_CALL,
  RINGING,
  DIALING
}
