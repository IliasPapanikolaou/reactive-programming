package com.unipi.reactor.p2flux;

import com.unipi.reactor.util.Util;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/*
* Flux from Mono
* Mono from Flux
* */

public class Lec09FluxFromMono {

    public static void main(String[] args) {

        // Flux from Mono

        // Mono Publisher
        Mono<String> mono = Mono.just("A String");

        // Convert Mono to Flux Publisher
        Flux<String> flux = Flux.from(mono);

        // Now we can call the methods with the flux argument
        doSomething(flux);

        // Subscriber
        flux.subscribe(Util.onNext());

        // Mono From Flux
        Flux.range(1, 10)
                // next() method converts a flux to mono. Emits one item and stops
                .next()
                .subscribe(
                        Util.onNext(),
                        Util.onError(),
                        Util.onComplete()
                );

        // If we want a specific number, we use .filter() before .next()
        Flux.range(1, 10)
                .filter(i -> i > 3)
                // next() method converts a flux to mono. Emits one item and stops
                .next()
                .subscribe(
                        Util.onNext(),
                        Util.onError(),
                        Util.onComplete()
                );
    }

    public static void doSomething(Flux<String> flux) {
        // Do something...
    }
}
