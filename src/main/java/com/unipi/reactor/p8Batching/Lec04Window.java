package com.unipi.reactor.p8Batching;

import com.unipi.reactor.util.Util;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.concurrent.atomic.AtomicInteger;

/*
 * Window is like Buffer but instead of returning a List<T>, it returns a Flux<T>
 */
public class Lec04Window {

    private static AtomicInteger atomicInteger = new AtomicInteger(1);

    public static void main(String[] args) {

        eventStream()
                // Returns a Flux<Flux<String>>
                // Items based
                .window(5)
                // Time based
                // .window(Duration.ofSeconds(2))
                .flatMap(flux -> saveEvents(flux))
                .subscribe(Util.subscriber());

        Util.sleepSeconds(60);
    }

    // Processes the flux streams that are produced by .windows(5)
    private static Mono<Integer> saveEvents(Flux<String> flux) {
        return flux
                .doOnNext(e -> System.out.println("Saving " + e))
                .doOnComplete(() -> {
                    System.out.println("Saved this batch");
                    System.out.println("----------------");
                })
                // .then() returns void or Mono<T> and emits a Complete signal
                .then(Mono.just(atomicInteger.getAndIncrement()));
    }

    // Publisher
    private static Flux<String> eventStream() {
        return Flux.interval(Duration.ofMillis(500))
                .map(i -> "Event-" + i);
    }
}
