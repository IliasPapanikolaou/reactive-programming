package com.unipi.reactor.p3operators;

import com.unipi.reactor.util.Util;
import reactor.core.publisher.Flux;

public class Lec09SwitchIfEmpty {

    public static void main(String[] args) {

        getOrderNumbers()
                .filter(i -> i > 10)
                .switchIfEmpty(fallback()) // Resilience - Fallback method when empty
                .subscribe(Util.subscriber());
    }

    // Example a Redis cache
    private static Flux<Integer> getOrderNumbers() {
        return Flux.range(1, 10);
    }

    // Fallback could be the DB
    private static Flux<Integer> fallback() {
        return Flux.range(100, 10);
    }
}
