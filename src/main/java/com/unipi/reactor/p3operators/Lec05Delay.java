package com.unipi.reactor.p3operators;

import com.unipi.reactor.util.Util;
import reactor.core.publisher.Flux;

import java.time.Duration;

public class Lec05Delay {

    public static void main(String[] args) {

        // Optional: If we want to tweak the reactor buffer size.
        System.setProperty("reactor.bufferSize.x", "9"); // Default = 32

        Flux.range(1, 1000)
                .log()
                // Producer sends 32 items and delayElements() delays them with 1 second interval
                // In 75% of the items drained, it sends another request to produce items.
                // @see reactor.util.concurrent.Queues
                .delayElements(Duration.ofSeconds(1))
                .subscribe(Util.subscriber());

        // Intended delay so we can watch the results
        Util.sleepSeconds(60);
    }
}
