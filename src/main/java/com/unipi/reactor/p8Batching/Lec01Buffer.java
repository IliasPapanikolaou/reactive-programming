package com.unipi.reactor.p8Batching;

import com.unipi.reactor.util.Util;
import reactor.core.publisher.Flux;

import java.time.Duration;

public class Lec01Buffer {

    public static void main(String[] args) {

        // Consumer
        eventStream()
                // Buffer collect the given number of items and returns them as a list
                // Items based buffer
                //.buffer(5)
                // Time based buffer
                //.buffer(Duration.ofSeconds(2))
                // If we want a combination of Item Based Buffer and Time Based Buffer:
                .bufferTimeout(5, Duration.ofSeconds(2))
                .subscribe(Util.subscriber());

        Util.sleepSeconds(60);
    }

    // Publisher
    private static Flux<String> eventStream() {
        return Flux.interval(Duration.ofMillis(300))
                // If the source emits less than the given buffer size,
                // the buffer will return only these items (3 items)
                // .take(3)
                .map(i -> "Event-" + i);
    }
}
