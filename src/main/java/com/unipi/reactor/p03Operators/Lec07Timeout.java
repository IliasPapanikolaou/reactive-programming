package com.unipi.reactor.p03Operators;

import com.unipi.reactor.util.Util;
import reactor.core.publisher.Flux;

import java.time.Duration;

public class Lec07Timeout {

    public static void main(String[] args) {


        getOrderNumbers()
                // If it doesn't get the requested items in 2 seconds, it will time out.
                //.timeout(Duration.ofSeconds(2))
                // If it doesn't get the requested items in 2 seconds, it will use fallback method.
                .timeout(Duration.ofSeconds(2), fallback())
                .subscribe(Util.subscriber());


        // Intended delay so we can watch the results
        Util.sleepSeconds(60);

    }

    private static Flux<Integer> getOrderNumbers() {
        return Flux.range(1, 10)
                .delayElements(Duration.ofSeconds(5));
    }

    private static Flux<Integer> fallback() {
        return Flux.range(100, 10)
                .delayElements(Duration.ofMillis(200));
    }
}
