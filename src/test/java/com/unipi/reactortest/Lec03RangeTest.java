package com.unipi.reactortest;

import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

public class Lec03RangeTest {

    // Expect a range of values

    @Test
    public void test1() {
        Flux<Integer> range = Flux.range(1, 50);
        // Step Verifier accepts a Publisher and subscribes to it internally
        StepVerifier.create(range)
                // Expect 50 values
                .expectNextCount(50)
                .verifyComplete();
    }

    // Often, we don't know the exact number of values we receive
    // Range with 'while' condition
    @Test
    public void test2() {
        Flux<Integer> range = Flux.range(1, 50);
        // Step Verifier accepts a Publisher and subscribes to it internally
        StepVerifier.create(range)
                // Expect 50 values
                .thenConsumeWhile(i -> i < 100)
                .verifyComplete();
    }
}
