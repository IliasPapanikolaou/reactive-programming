package com.unipi.reactor.p02Flux;

import com.unipi.reactor.util.Util;
import reactor.core.publisher.Flux;

public class Lec01FluxIntro {

    public static void main(String[] args) {

        // Easiest way to create a flux is just
        // Publisher
        Flux<Integer> flux = Flux.just(1);

        // Subscriber
        flux.subscribe(System.out::println);
        // or
        flux.subscribe(
                Util.onNext(),
                Util.onError(),
                Util.onComplete()
        );

        // With flux we can emit more than one item.
        // Publisher
        Flux<Integer> fluxMore = Flux.just(1, 2, 3, 4, 5, 6, 7, 8, 9, 10);

        // Subscriber
        fluxMore.subscribe(
                Util.onNext(),
                Util.onError(),
                Util.onComplete()
        );

        // Flux.empty() means there are no more items to emit and invokes onComplete method
        // Publisher
        Flux<Integer> fluxEmpty = Flux.empty();

        // Subscriber
        fluxEmpty.subscribe(
                Util.onNext(),
                Util.onError(),
                Util.onComplete() // .empty() invokes only this one
        );

        // We can emit multiple objects with flux
        // Publisher
        Flux<Object> fluxObject = Flux.just(1, 2, "A", Util.faker().name().fullName());

        // Subscriber
        fluxObject.subscribe(
                Util.onNext(),
                Util.onError(),
                Util.onComplete()
        );
    }
}
