package com.globallabs.decorators;

import com.globallabs.abstractions.TelephoneWithPipelineSpecification;
import com.globallabs.operator.Pipeline;
import com.globallabs.telephone.Consumer;
import com.globallabs.telephone.Producer;

public abstract class TelephoneWithPipelineDecorator 
    extends TelephoneDecorator implements TelephoneWithPipelineSpecification {

  public TelephoneWithPipelineSpecification telephone;

  public TelephoneWithPipelineDecorator(TelephoneWithPipelineSpecification telephone) {
    super(telephone);
  }

  @Override
  public void setConsumePipe(Pipeline consumePipe) {
    telephone.setConsumePipe(consumePipe);
  }

  @Override
  public Pipeline getConsumePipe() {
    return telephone.getConsumePipe();
  }

  @Override
  public void setPublishPipe(Pipeline publishPipe) {
    telephone.setPublishPipe(publishPipe);
  }

  @Override
  public Pipeline getPublishPipe() {
    return telephone.getPublishPipe();
  }

  @Override
  public void activateConsumerProducerThreads() {
    telephone.activateConsumerProducerThreads();
  }

  @Override
  public Consumer getConsumer() {
    return telephone.getConsumer();
  }

  @Override
  public Producer getProducer() {
    return telephone.getProducer();
  }
}
