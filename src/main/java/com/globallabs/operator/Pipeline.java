package com.globallabs.operator;

import java.util.LinkedList;

public class Pipeline {
  LinkedList<Integer> pipe;
  String pipeName;

  public Pipeline() {
    pipe = new LinkedList<Integer>();
  }

  public Pipeline(LinkedList<Integer> data) {
    pipe = data;
  }

  public Pipeline(LinkedList<Integer> data, String name) {
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
