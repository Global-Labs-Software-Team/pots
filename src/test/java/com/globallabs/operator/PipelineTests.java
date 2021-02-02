package com.globallabs.operator;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.globallabs.phoneexceptions.InvalidNumberException;

import java.io.InvalidObjectException;
import java.util.LinkedList;
import java.util.NoSuchElementException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class PipelineTests {
  
  Pipeline defaultPipe;
  String name = "defaultPipe";
  String pipeName = "testingPipe";
  LinkedList<Integer> data;

  /**
   * Set up a new populated pipe for each
   * test. This pipe will contain initially the following
   * stream: {@code{ 0, 1, 1 }}
   */
  @BeforeEach
  public void setUp() {
    defaultPipe = new Pipeline(name);
    data = new LinkedList<Integer>();
    data.add(0);
    data.add(1);
    data.add(1);
    defaultPipe = new Pipeline(name, data);
  }

  /**
   * Test the constructor. Check if the information
   * is initialize with the given parameters.
   */
  @Test
  void test_constructorPipeline_one_success() {
    Pipeline pipe = new Pipeline(pipeName);

    assertEquals(pipeName, pipe.getPipeName());
  }

  /**
   * Test the alternative constructor. Check
   * if the information (pipeName and data) is
   * set correctly without alterations.
   */
  @Test
  void test_constructorPipeline_two_success() {
    LinkedList<Integer> stream = new LinkedList<Integer>();
    stream.add(0);
    stream.add(1);
    stream.add(0);
    Pipeline pipe = new Pipeline(pipeName, stream);
    assertEquals(pipeName, pipe.getPipeName());
    assertEquals(stream, pipe.getPipe());
  }

  /**
   * Test that the stream received by the constructor
   * only contains bit values (0 or 1).
   */
  @Test
  void test_constructorPipeline_two_error() {
    LinkedList<Integer> stream = new LinkedList<Integer>();
    stream.add(0);
    stream.add(1);
    stream.add(-1);

    assertThrows(InvalidObjectException.class, () -> {
      new Pipeline(pipeName, stream);
    }, "Create the object in spite of the no bit in the list (-1)");
  }

  /**
   * Test that a bit can be added correctly into the pipe.
   * This is made through the method 
   * {@link com.globallabs.operator.Pipeline.publish#publish(int)}.
   */
  @Test
  void test_publish_success() {
    int addedBit = 0;
    defaultPipe.publish(addedBit);
    data.add(addedBit);
    assertEquals(data, defaultPipe.getPipe());
    assertEquals(name, defaultPipe.getPipeName());
  }

  /**
   * Test that a value different from 0 or 1 cannot be publish
   * into the pipe.
   */
  @Test
  void test_publish_invalidNumber() {
    int addedBit = -1;
    data = defaultPipe.getPipe(); // This take a snapshot before using publish
    assertThrows(InvalidNumberException.class, () -> {
      defaultPipe.publish(addedBit);
    });

    // Assert that the pipe was not altered
    assertEquals(data, defaultPipe.getPipe());
    assertEquals(name, defaultPipe.getPipeName());
  }

  /**
   * Test that consume always get the first element of
   * the list. We take the first from the original data
   * and compared it with the one obtained from the method
   * {@link com.globallabs.operator.Pipeline.consume#consume()}
   */
  @Test
  void test_consume_populatePipe() {
    int expectedBit = data.remove(0); // Get the first bit in the queue
    int result = defaultPipe.consume();
    assertEquals(expectedBit, result);
    assertEquals(data, defaultPipe.getPipe());
  }

  /**
   * Test that a NoSuchElementException is thrown if
   * there is nothing to consume in the pipe (pipe is empty).
   */
  @Test
  void test_consume_emptyPipe() {
    String emptyPipeName = "emptyPipe";
    LinkedList<Integer> emptyList = new LinkedList<Integer>();
    Pipeline emptyPipe = new Pipeline(emptyPipeName);
    assertThrows(NoSuchElementException.class, () -> {
      emptyPipe.consume();
    });

    assertEquals(emptyList, emptyPipe.getPipe());
    assertEquals(emptyPipeName, emptyPipe.getPipeName());
  }
}
