package com.globallabs.telephone;

import java.util.Random;

/**
 * Consumer class to add fields to a thread in order to manage a queue and the time for which it runs
 * 
 * @version 1.0
 * @author Byron Barkhuizen
 */

public class Producer extends Thread {
    Pipeline queue;
    int time;
    Random rand = new Random();

    public Producer(String str, Pipeline q, int seconds) {
        super(str);
        queue = q;
        time = seconds;
    }

    public void run() {
        int current_message;
        for (int = 0; i < time; i++) {
            current_message = rand.nextInt(2);
            publishMessage (current_message);
            try {
                sleep((int)(Math.random()*500));
            } catch (InterruptedException e) {}
        }
    }

    syncrhonized void publishMessage(int current_message) {
        System.out.println("Produceer" + getName() + "produced " + Integer.toSring(current_message));
        queue.publish(current_message)
    }
}
