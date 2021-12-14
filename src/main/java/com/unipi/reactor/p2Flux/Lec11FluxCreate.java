package com.unipi.reactor.p2Flux;

import com.unipi.reactor.util.Util;
import reactor.core.publisher.Flux;

import java.util.stream.IntStream;

/*
 * Create and Push are about the same:
 *
 * Create is thread-safe, we can use it with multiple threads.
 * Push is NOT thread-safe, we can use it in a single thread producer.
 *
 */

public class Lec11FluxCreate {

    public static void main(String[] args) {

        // Custom made Flux
        Flux.create(fluxSink -> {

            fluxSink.next(1);
            fluxSink.next(2);
            fluxSink.next(3);
            fluxSink.complete();

        }).subscribe(Util.subscriber());

        // Custom made Flux recursively
        Flux.create(fluxSink -> {

            IntStream.rangeClosed(1, 10)
                    .forEach(i ->
                            fluxSink.next(Util.faker().country().name())
                    );
        }).subscribe(Util.subscriber());

        // Emit while
        Flux.create(fluxSink -> {

            String country;

            do {
                country = Util.faker().country().name();
                fluxSink.next(country);
            }
            while (!country.equalsIgnoreCase("canada"));

            fluxSink.complete();

        }).subscribe(Util.subscriber());
    }
}
