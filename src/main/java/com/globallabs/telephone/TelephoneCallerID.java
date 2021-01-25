package com.globallabs.telephone;

import com.globallabs.abstractions.TelephoneWithPipelineSpecification;
import com.globallabs.decorators.TelephoneWithPipelineDecorator;
// import com.globallabs.operator.Exchange;
import com.globallabs.phoneexceptions.PhoneExistInNetworkException;

public class TelephoneCallerID extends TelephoneWithPipelineDecorator {

  // private int incoming;
  // private TelephoneWithPipeline phone;
  // private int information;
  // private Exchange exchange;

  public TelephoneCallerID(TelephoneWithPipelineSpecification telephone)
          throws PhoneExistInNetworkException {
    super(telephone);
  }
  
  /**
   * Comments.
   */
  // public int findCallerID(TelephoneWithPipeline incomingPhone)
  //         throws PhoneNotFoundException {
  //   incoming = incomingPhone.getIncomingCall();
  //   phone = exchange.getPhone(incoming);
  //   information = phone.getTelephoneId();
  //   return information;
  // }

  public int findCallerID() {
    return getIncomingCall();
  }
}
