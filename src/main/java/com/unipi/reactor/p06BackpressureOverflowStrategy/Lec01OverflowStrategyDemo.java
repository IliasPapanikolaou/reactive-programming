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
public class Lec01OverflowStrategyDemo {

    public static void main(String[] args) {

        // Publisher
        // This part of the publisher doesn't take time to be created
        Flux<Object> flux = Flux.create(fluxSink -> {
                    IntStream.rangeClosed(1, 500).forEach(i -> {
                        fluxSink.next(i);
                        System.out.println("Pushed: " + i);
                    });
                    fluxSink.complete();
                })
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
