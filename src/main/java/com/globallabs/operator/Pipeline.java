package com.globallabs.operator;

import java.util.LinkedList;

/**
 * Pipeline represents a queue where data can be stream.
 * This class allow us to connect different Telephones with each
 * other only by consuming or publishing information into these
 * objects. The Pipeline only contains bits because this is how
 * the information travel in real life.
 * 
 * <p>An simple example for the use of the pipeline is the following:
 * 
 * <p>We have to telephone t1 and t2, each with is own personal pipes
 * where they can put their messages. They exchange their pipe links
 * so t1 can be able to consume from t2 pipe and t2 can be able to
 * consume from t1. Important: They can only consume from the pipes
 * of the other pair, not publish. And with they can comunicate with
 * each other.
 * 
 * @author Daniel RODRIGUEZ
 * @since 0.0
 */
public class Pipeline {
  LinkedList<Integer> pipe;
  String pipeName;

  /**
   * First constructor. This was made for testing purposes
   * only. It receives the data you want to send, with the goal
   * to see if the data gets through correctly
   * @param name the name of the pipe
   * @param data a list containing a sequence of bits to stream
   */
  public Pipeline(String name, LinkedList<Integer> data) {
    pipe = new LinkedList<Integer>(data);
    pipeName = name;
  }

  /**
   * Main constructor. For the use in production. It creates
   * an empty pipeline ideal for the brand new Telephone to
   * use.
   * @param name String with the name of the pipe
   */
  public Pipeline(String name) {
    pipe = new LinkedList<Integer>();
    pipeName = name;
  }

  /**
   * Gets the name of the Pipe.
   * @return an String containing the pipe name
   */
  public String getPipeName() {
    return pipeName;
  }

  /**
   * Gets the list containing the data to send
   * of the pipe.
   * @return An integer LinkedList with the stream
   */
  public LinkedList<Integer> getPipe() {
    return pipe;
  }

  /**
   * It will add the bit to send in the end of the
   * queue. 
   * @param bit information to send
   */
  public void publish(int bit) {
    pipe.addLast(bit);
  }

  /**
   * It will read the first element of the pipeline (HEAD)
   * and return it.
   *
   * @return the bit at the head of the list
   */
  public int consume() {
    int bitRead = pipe.remove();
    return bitRead;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (!(o instanceof Pipeline)) {
      return false;
    }
    Pipeline pipe = (Pipeline) o;
    return getPipeName().equals(pipe.getPipeName()) 
        && getPipe().equals(pipe.getPipe());
  }
}
