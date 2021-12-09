package com.unipi.reactor.flux;

import reactor.core.publisher.Flux;

public class Lec02MultipleSubscribers {

    /*
    * Multiple subscribers, works on Mono as well
    * */

    public static void main(String[] args) {

        // Publisher
        Flux<Integer> integerFlux = Flux.just(1, 2, 3, 4);

        // Subscriber 1
        integerFlux.subscribe(i -> System.out.println("Sub 1: " + i));

        // Subscriber 2
        integerFlux.subscribe(i -> System.out.println("Sub 2: " + i));

        // Filter only the even numbers
        Flux<Integer> evenFlux = integerFlux.filter(i -> i % 2 == 0);

        // Subscriber 3 - Only even numbers
        evenFlux.subscribe(i -> System.out.println("Sub 3: " + i));
    }
}
