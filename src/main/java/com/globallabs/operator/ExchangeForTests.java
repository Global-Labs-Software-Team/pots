package com.globallabs.operator;

import com.globallabs.abstractions.ExchangeSpecification;
import com.globallabs.decorators.ExchangeDecorator;
import java.util.LinkedList;

/**
 * This is a special class created only for tests. The class
 * extends from the Exchange class but it will add a method
 * capable of restarting the Exchange. This means to reinitialize
 * the list of phones registered.
 * 
 * <p>IMPORTANT: Not for production use
 */
public class ExchangeForTests extends ExchangeDecorator {
  
  public ExchangeForTests(ExchangeSpecification exchange) {
    super(exchange);
  }

  /**
   * This private class will create an only exchange
   * when a Telephone try to get it (Singleton Pattern).
   * If the exchange does not exists then it will create it,
   * else it will retrieve it. 
   */
  private static class ExchangeForTestsHolder {
    private static final ExchangeForTests INSTANCE = new ExchangeForTests(Exchange.getInstance());
  }

  /**
   * Access to the singleton of Exchange.
   *
   * @return the exchange
   */
  public static ExchangeForTests getInstance() {
    return ExchangeForTestsHolder.INSTANCE;
  }

  /**
   * Restart the exchange. Reinitialize the list
   * of phones to an empty list. This is for testing
   * only. It is to be able to have a clean slate for each test
   */
  public void resetExchange() {
    exchange.setTelephones(new LinkedList<>());
  }
}
