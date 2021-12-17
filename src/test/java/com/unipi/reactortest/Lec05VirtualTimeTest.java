package com.unipi.reactortest;

import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import java.time.Duration;

public class Lec05VirtualTimeTest {

    /*
     * We don't want to hang the system by testing long timeouts.
     * For this very reason, there is StepVerifier.withVirtualTime()
     */

    @Test
    public void test1() {

        // Step Verifier accepts a Publisher and subscribes to it internally
        // .withVirtualTime()
        StepVerifier.withVirtualTime(() -> timeConsumingFlux())
                // This would take 20 seconds to complete without .withVirtualTime()
                .thenAwait(Duration.ofSeconds(30))
                .expectNext("1a", "2a", "3a", "4a")
                .verifyComplete();
    }

    // Expect that nothing will happen / be received the first 4 seconds after the subscription
    @Test
    public void test2() {

        // Step Verifier accepts a Publisher and subscribes to it internally
        // .withVirtualTime()
        StepVerifier.withVirtualTime(() -> timeConsumingFlux())
                // Subscription is an event, so we have to expect this one first
                .expectSubscription()
                // Expect no event the first 4 seconds
                .expectNoEvent(Duration.ofSeconds(4))
                .thenAwait(Duration.ofSeconds(20))
                .expectNext("1a", "2a", "3a", "4a")
                .verifyComplete();
    }

    private Flux<String> timeConsumingFlux() {
        return Flux.range(1, 4)
                .delayElements(Duration.ofSeconds(5))
                .map(i -> i + "a");
    }
}
