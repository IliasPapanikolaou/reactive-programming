package com.unipi.reactor.p07CombiningPublishers;

import com.unipi.reactor.util.Util;
import reactor.core.publisher.Flux;

import java.time.Duration;

/*
 * If we have two publishers emitting items, combineLatest() will wait until
 * both publishers have items to emit and will combine the latest items of both of them.
 *
 * Publisher 1:
 * ---------------A---B-----------C---D------------->
 * Publisher 2:
 * ------------------------1----------------2------->
 *                         |      |   |     |
 * combineLatest:          B1     C1  D1    D2
 *
 * Tip: Useful in Stock Prices
 */
public class Lec05CombineLatest {

    public static void main(String[] args) {

        Flux.combineLatest(getString(), getNumber(), (s, i) -> s + i)
                .subscribe(Util.subscriber());

        Util.sleepSeconds(10);
    }

    // Stream 1 (Publisher 1)
    private static Flux<String> getString() {
        return Flux.just("A", "B", "C", "D")
                .delayElements(Duration.ofSeconds(1));
    }

    // Stream 2 (Publisher 2)
    private static Flux<Integer> getNumber() {
        return Flux.just(1, 2, 3)
                .delayElements(Duration.ofSeconds(3));
    }
}
