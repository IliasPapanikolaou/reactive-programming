package com.unipi.reactortest;

import org.junit.jupiter.api.Test;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;
import reactor.test.StepVerifierOptions;
import reactor.util.context.Context;

public class Lec07ContextTest {

    // Verify that we get an error if no context has been provided
    @Test
    public void test1() {
        StepVerifier.create(getWelcomeMessage())
                .verifyError(RuntimeException.class);
    }

    // Verify that we get 'Welcome' message if context has been provided
    @Test
    public void test2() {
        // Provide context
        StepVerifierOptions options = StepVerifierOptions.create().withInitialContext(Context.of("user", "sam"));
        // Test context
        StepVerifier.create(getWelcomeMessage(), options)
                .expectNext("Welcome sam")
                .verifyComplete();
    }

    private static Mono<String> getWelcomeMessage() {
        return Mono
                .deferContextual(contextView -> {
                    if (contextView.hasKey("user")) {
                        return Mono.just("Welcome " + contextView.get("user"));
                    }
                    else {
                        return Mono.error(new RuntimeException("unauthenticated"));
                    }
                });
    }
}
