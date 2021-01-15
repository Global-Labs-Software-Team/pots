package com.globallabs.telephone;

import com.globallabs.operator.Exchange;
import com.globallabs.operator.Pipeline;
import com.globallabs.phonedata.TelephoneModel;
import com.globallabs.phoneexceptions.PhoneExistInNetworkException;

public class TelephoneWithPipeline extends Telephone {

  Pipeline publishPipe;
  Pipeline consumePipe;

  public TelephoneWithPipeline(TelephoneModel phoneInfo, Exchange exchange, Pipeline publishPipe) 
      throws PhoneExistInNetworkException {
    super(phoneInfo, exchange);
    this.publishPipe = publishPipe;
  }

  public void setConsumePipe(Pipeline consumePipe) {
    
  }

  public Pipeline getPublishPipe() {
    return new Pipeline();
  }
}
