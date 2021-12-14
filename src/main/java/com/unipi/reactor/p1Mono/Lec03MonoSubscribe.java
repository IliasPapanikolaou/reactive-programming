package com.unipi.reactor.p1Mono;


/*
 * Subscribe
 * ---------
 * onNext() - Consumer<T>
 * onError() - Consumer<Throwable>
 * onComplete - Runnable
 */

import com.unipi.reactor.util.Util;
import reactor.core.publisher.Mono;

public class Lec03MonoSubscribe {

    public static void main(String[] args) {

        // Publisher
        Mono<String> mono = Mono.just("A guitar");

        // Subscriber
        mono.subscribe(
                System.out::println, // Consumer
                err -> System.out.println(err.getMessage()), // Throwable
                () -> System.out.println("Completed") // Runnable
        );

        // Publisher
        Mono<Integer> mono1 = Mono.just("Another Item")
                .map(String::length)
                .map(l -> (l / 0)); // this will throw an intended divide by 0 error

        // Subscriber
        mono1.subscribe(
                System.out::println, // Consumer
                err -> System.out.println(err.getMessage()), // Throwable
                () -> System.out.println("Completed") // Runnable
        );

        // or same as above using the Util static methods
        mono1.subscribe(
                Util.onNext(),
                Util.onError(),
                Util.onComplete()
        );
    }
}
