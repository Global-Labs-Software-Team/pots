package com.globallabs.telephone;

import com.globallabs.operator.Exchange;
import com.globallabs.operator.Pipeline;
import com.globallabs.phonedata.TelephoneModel;
import com.globallabs.phoneexceptions.PhoneExistInNetworkException;

public class TelephoneWithPipeline extends Telephone {

  Pipeline publishPipe;
  Pipeline consumePipe;

  /**
   * Complete.
   * @param phoneInfo a
   * @param exchange a
   * @param publishPipe a
   * @throws PhoneExistInNetworkException a
   */
  public TelephoneWithPipeline(TelephoneModel phoneInfo, Exchange exchange, Pipeline publishPipe) 
      throws PhoneExistInNetworkException {
    super(phoneInfo, exchange);
    exchange.addPhoneToExchange(this);
    this.publishPipe = publishPipe;
  }

  public TelephoneWithPipeline(TelephoneModel phoneInfo, Exchange exchange)
      throws PhoneExistInNetworkException {
    super(phoneInfo, exchange);
    exchange.addPhoneToExchange(this); 
  }

  public TelephoneWithPipeline(TelephoneModel phoneInfo) {
    super(phoneInfo);
  }

  public void setConsumePipe(Pipeline consumePipe) {
    
  }

  public Pipeline getConsumePipe() {
    return new Pipeline();
  }

  public void setPublishPipe(Pipeline publishPipe) {

  }

  public Pipeline getPublishPipe() {
    return new Pipeline();
  }
}
