package com.unipi.reactortest;

import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

public class Lec01StepVerifierTest {

    @Test
    public void test1() {
        Flux<Integer> just = Flux.just(1, 2, 3);

        // Step Verifier accepts a Publisher and subscribes to it internally
        StepVerifier.create(just)
                .expectNext(1)
                .expectNext(2)
                .expectNext(3)
                //.expectComplete().verify()
                // or
                .verifyComplete();
    }

    @Test
    public void test2() {
        Flux<Integer> just = Flux.just(1, 2, 3);

        // Step Verifier accepts a Publisher and subscribes to it internally
        StepVerifier.create(just)
                .expectNext(1, 2, 3)
                //.expectComplete().verify()
                // or
                .verifyComplete();
    }

}
