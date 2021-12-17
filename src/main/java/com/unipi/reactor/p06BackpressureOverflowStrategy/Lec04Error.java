package com.unipi.reactor.p06BackpressureOverflowStrategy;

import com.unipi.reactor.util.Util;
import reactor.core.publisher.Flux;
import reactor.core.scheduler.Schedulers;

import java.util.stream.IntStream;

/*
 * Overflow Strategy
 *
 * Strategy      -> Behavior
 * -------------------------
 * buffer        -> Keep in memory (Default)
 *
 * drop          -> Once the queue is full,
 *                  new items will be dropped
 *
 * latest        -> Once the queue is full, keep 1
 *                  latest item as and when it arrives.
 *                  Drop old
 *
 * error         -> Throw error to the downstream
 */
public class Lec04Error {

    public static void main(String[] args) {
        // Optional: We can tweak the values found on Queues class (default bufferSize.small = 256)
        // By default it will let you drain 75% of 256 = 192 or 75% of 16 = 12 items
        System.setProperty("reactor.bufferSize.small", "16");

        // Publisher
        // This part of the publisher doesn't take time to be created
        Flux<Object> flux = Flux.create(fluxSink -> {
                    IntStream.rangeClosed(1, 500).forEach(i -> {
                        // if fluxSink is cancelled, then stop pushing data
                        if (!fluxSink.isCancelled()) {
                            fluxSink.next(i);
                            System.out.println("Pushed: " + i);
                            // Emulate process time (delay of 1 millisecond)
                            Util.sleepMillis(1);
                        }
                    });
                    fluxSink.complete();
                })
                // Adding Overflow Strategy to drop items
                .onBackpressureError() // Throw error if the there is overflow
                .publishOn(Schedulers.boundedElastic())
                // Emulate process time (delay of 10 milliseconds)
                // This part of the publisher takes time to be completed
                // causing Bottleneck in the data flow
                .doOnNext(i -> Util.sleepMillis(10));

        // Subscriber
        flux.subscribe(v -> System.out.println(Thread.currentThread().getName() + " received " + v));

        Util.sleepSeconds(10);
    }
}
