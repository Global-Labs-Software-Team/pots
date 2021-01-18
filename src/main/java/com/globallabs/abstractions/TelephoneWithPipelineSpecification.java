package com.globallabs.abstractions;

import com.globallabs.operator.Pipeline;

public interface TelephoneWithPipelineSpecification {
  
  /**
   * Complete javadoc.
   *
   * @param consumePipe complete
   */
  public void setConsumePipe(Pipeline consumePipe);
  
  public Pipeline getConsumePipe();

  /**
   * Complete javadoc.
   *
   * @return p
   */
  public Pipeline getPublishPipe();

  public void setPublishPipe(Pipeline publishPipe);

  /**
   * Check the status of the phone to see if it is able to
   * use the methods in exchange.
   * @param nameOfFunction the name of the method in exchange
   * @return true if it is able, otherwise false
   */
  public boolean isAbleTo(String nameOfFunction);
}
