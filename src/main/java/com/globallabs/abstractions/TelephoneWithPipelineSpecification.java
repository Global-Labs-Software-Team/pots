package com.globallabs.abstractions;

import com.globallabs.operator.Pipeline;

public interface TelephoneWithPipelineSpecification {
  
  /**
   * Complete javadoc.
   * @param consumePipe complete
   */
  public void setConsumePipe(Pipeline consumePipe);

  /**
   * Complete javadoc.
   * @return
   */
  public Pipeline getPublishPipe();
}
