package com.unipi.reactortest;

import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

public class Lec02StepVerifierErrorTest {

    @Test
    public void test1() {

        Flux<Integer> just = Flux.just(1, 2, 3);
        Flux<Integer> error = Flux.error(new RuntimeException("Error!"));
        // Concat fluxes
        Flux<Integer> concat = Flux.concat(just, error);

        // Step Verifier accepts a Publisher and subscribes to it internally
        StepVerifier.create(concat)
                .expectNext(1, 2, 3)
                // .verifyError();
                // or
                //.verifyError(RuntimeException.class);
                // or
                .verifyErrorMessage("Error!");
    }
}
