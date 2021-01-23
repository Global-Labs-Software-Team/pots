package com.globallabs.telephone;

import com.globallabs.operator.Exchange;
import com.globallabs.operator.Pipeline;
import com.globallabs.phonedata.TelephoneModel;
import com.globallabs.phoneexceptions.PhoneExistInNetworkException;
import com.globallabs.phoneexceptions.PhoneNotFoundException;

public class TelephoneCallerID extends TelephoneWithPipeline {

  private int incoming;
  private TelephoneWithPipeline phone;
  private int information;
  private Exchange exchange;

  public TelephoneCallerID(TelephoneModel phoneInfo, Exchange exchange, Pipeline publishPipe)
          throws PhoneExistInNetworkException {
    super(phoneInfo, exchange, publishPipe);
    this.exchange = exchange;
  }
  
  /**
   * Comments.
   */
  public int findCallerID(TelephoneWithPipeline incomingPhone)
          throws PhoneNotFoundException {
    incoming = incomingPhone.getIncomingCall();
    phone = exchange.getPhone(incoming);
    information = phone.getTelephoneId();
    return information;
  }
}
