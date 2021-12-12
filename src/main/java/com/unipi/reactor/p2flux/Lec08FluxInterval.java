package com.unipi.reactor.p2flux;

import com.unipi.reactor.util.Util;
import reactor.core.publisher.Flux;

import java.time.Duration;

public class Lec08FluxInterval {

    public static void main(String[] args) {

        // Emits periodically for the specified time
        // Emit every 1 sec
        // Publisher
        Flux.interval(Duration.ofSeconds(1))
                .subscribe(Util.onNext()); // Subscriber

        // Block the main thread in order to see results
        Util.sleepSeconds(5);
    }
}
