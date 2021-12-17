package com.unipi.reactor.p02Flux;

import com.unipi.reactor.util.Util;
import reactor.core.publisher.Flux;

public class Lec05FluxRangeAndLogging {

    public static void main(String[] args) {

        // New way to write for loops, like IntStream.range()

        // Publisher that will emit 10 integers starting from 1
        Flux<Integer> integerFlux = Flux.range(1, 10);

        // Subscriber
        integerFlux.subscribe(
                Util.onNext()
        );

        // Publisher that will emit 10 items starting from 3
        Flux<Integer> integerFlux1 = Flux.range(3, 10);

        // Subscriber - will count until 12
        integerFlux1.subscribe(
                Util.onNext()
        );

        // Logging
        // Print names 10 times - Publisher and Consumer using Logging
        Flux.range(1, 10)
                .log() // helps debugging
                .map(i -> Util.faker().name().fullName())
                .log()
                .subscribe(Util.onNext()); // Subscriber
    }
}
