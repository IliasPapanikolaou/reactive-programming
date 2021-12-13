package com.unipi.reactor.p7CombiningPublishers;

import com.unipi.reactor.util.Util;
import reactor.core.publisher.Flux;

public class Lec02Concat {

    public static void main(String[] args) {

        // Publishers
        Flux<String> flux1 = Flux.just("a", "b");
        Flux<String> flux2 = Flux.error(new RuntimeException("Intended Error!"));
        Flux<String> flux3 = Flux.just("c", "d", "e");

        // Combine the two publishers in one flux
        Flux<String> concatFlux = flux1.concatWith(flux3);
        // or same as above, different syntax
        // Flux<String> concFlux = Flux.concat(flux1, flux2);

        // If we want to not stop on errors, continue and report the error at the end.
        Flux<String> fluxWithError = Flux.concatDelayError(flux1, flux2, flux3);

        // Subscribers 1
        concatFlux.subscribe(Util.subscriber());
        // concFlux.subscribe(Util.subscriber());

        // Subscriber 2
        fluxWithError.subscribe(Util.subscriber());
    }
}
