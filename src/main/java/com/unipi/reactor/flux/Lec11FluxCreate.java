package com.unipi.reactor.flux;

import com.unipi.reactor.util.Util;
import reactor.core.publisher.Flux;
import reactor.core.publisher.FluxSink;

import java.util.function.Consumer;
import java.util.stream.IntStream;

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
