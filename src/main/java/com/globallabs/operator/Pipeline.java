package com.globallabs.operator;

import java.util.LinkedList;

public class Pipeline {
  LinkedList<Integer> pipe;
  String pipeName;

  public Pipeline(String name, LinkedList<Integer> data) {
    pipe = data;
    pipeName = name;
  }

  public Pipeline(String name) {
    pipeName = name;
    pipe = new LinkedList<Integer>();
  }

  public void publish(int bit) {
    pipe.addLast(bit);
  }

  public String getPipeName() {
    return pipeName;
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

  public boolean equals(Pipeline pipe) {
    return getPipeName().equals(pipe.getPipeName());
  }
}
