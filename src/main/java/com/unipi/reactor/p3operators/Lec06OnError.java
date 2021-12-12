package com.unipi.reactor.p3operators;


import com.unipi.reactor.util.Util;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/*
* Creating Highly Resilient Pipelines
*/
public class Lec06OnError {

    public static void main(String[] args) {

        Flux.range(1, 100)
                .log()
                // When i reaches 5, we will get a divided by 0 error
                .map(i -> 10 / (5 - i))
                // Resilience
                // .onErrorReturn(-1) // Return a static value and cancel
                // .onErrorResume(err -> fallback()) // Return a value from function and cancel
                .onErrorContinue((err, obj) -> {
                    System.out.println("Object " + obj +" caused an error: " + err.getMessage());
                }) // It continues after the error, it does not cancel
                .subscribe(Util.subscriber());

    }

    private static Mono<Integer> fallback() {
        return Mono.fromSupplier(() -> Util.faker().random().nextInt(100, 200));
    }
}
