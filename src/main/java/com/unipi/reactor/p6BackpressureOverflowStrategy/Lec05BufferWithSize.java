package com.unipi.reactor.p6BackpressureOverflowStrategy;

import com.unipi.reactor.util.Util;
import reactor.core.publisher.Flux;
import reactor.core.publisher.FluxSink;
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
public class Lec05BufferWithSize {

    public static void main(String[] args) {
        // Optional: We can tweak the values found on Queues class (default bufferSize.small = 256)
        // By default it will let you drain 75% of 256 = 192 or 75% of 16 = 12 items
        System.setProperty("reactor.bufferSize.small", "16");

        // Publisher
        // This part of the publisher doesn't take time to be created
        Flux<Object> flux = Flux.create(fluxSink -> {
                    IntStream.rangeClosed(1, 500).forEach(i -> {
                        if (!fluxSink.isCancelled()) {
                            fluxSink.next(i);
                            System.out.println("Pushed: " + i);
                            // Emulate process time (delay of 1 millisecond)
                            Util.sleepMillis(1);
                        }
                    });
                    fluxSink.complete();
                })
                // Adding Overflow Strategy to drop items but keep latest
                // Buffer with size - Will throw if there is overflow error which is normal
                // We can show the items dropped with a second argument.
                .onBackpressureBuffer(20, o -> System.out.println("Dropped " + o))
                .publishOn(Schedulers.boundedElastic())
                // Emulate process time (delay of 10 milliseconds)
                // This part of the publisher takes time to be completed
                // causing Bottleneck in the data flow
                .doOnNext(i -> Util.sleepMillis(10));

        // We can also assign an Overflow Strategy on Flux.create() as a second argument
        // Publisher
        Flux<Object> flux1 = Flux.create(fluxSink -> {
                    fluxSink.next(1);
                }, FluxSink.OverflowStrategy.DROP)
                .publishOn(Schedulers.boundedElastic());

        // Subscriber
        flux1.subscribe(System.out::println);

        // Subscriber
        flux.subscribe(v -> System.out.println(Thread.currentThread().getName() + " received " + v));

        Util.sleepSeconds(10);
    }
}
