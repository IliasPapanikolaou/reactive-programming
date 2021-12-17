package com.unipi.reactortest;

import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;
import reactor.test.StepVerifierOptions;

public class Lec06ScenarioNameTest {

    // Name the testing Scenario
    @Test
    public void test1() {

        Flux<String> flux = Flux.just("a", "b", "c");

        StepVerifierOptions scenarioName = StepVerifierOptions.create().scenarioName("Alphabet-test");

        StepVerifier.create(flux, scenarioName)
                .expectNextCount(5)
                .verifyComplete();
    }

    // Alternate way to name each individual test
    @Test
    public void test2() {

        Flux<String> flux = Flux.just("a", "b1", "c");

        StepVerifier.create(flux)
                .expectNext("a").as("a-test")
                .expectNext("b").as("b-test")
                .expectNext("c").as("c-test")
                .verifyComplete();
    }
}
