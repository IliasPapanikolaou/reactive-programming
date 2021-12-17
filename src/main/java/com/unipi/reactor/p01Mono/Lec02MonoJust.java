package com.unipi.reactor.p01Mono;

import reactor.core.publisher.Mono;

/*
 * In reactive programming, nothing happens until we 'Subscribe'
 */

public class Lec02MonoJust {

    public static void main(String[] args) {

        // Publisher - Mono is a Publisher implementations which returns 0 or 1 item.
        // Easiest way to create a mono is just
        Mono<Integer> mono = Mono.just(1);

        // Subscribe to the publisher and consume the data
        mono.subscribe(i -> System.out.println("Received: " + i));
    }
}
