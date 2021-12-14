package com.unipi.reactor.p8Batching;

import com.unipi.reactor.util.Util;
import reactor.core.publisher.Flux;

import java.time.Duration;

public class Lec02OverlapAndDrop {

    public static void main(String[] args) {

        // Consumer
        eventStream()
                // Patterns
                // Skip 1 is like 0-1-2, 1-2-3, 2-3-4, 3-4-5, 4-5-6 ...
                // Skip 2 is like 0-1-2, 2-3-4, 4-5-6, 6-7-8, 8-9-10 ...
                // Skip 5 is like 0-1-2, 5-6-7, 10-11-12, 15-16-17 ...
                .buffer(3, 1)
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
