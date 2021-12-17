package com.unipi.reactor.p04HotColdPublishers;


import com.unipi.reactor.util.Util;
import reactor.core.publisher.Flux;

import java.time.Duration;
import java.util.stream.IntStream;
import java.util.stream.Stream;

/*
 * Cold publishers start emitting separately to whomever subscribes to them.
 */
public class Lec01ColdPublishers {

    public static void main(String[] args) {

        // Publisher
        Flux<String> movieStream = Flux.fromStream(() -> getMovie())
                .delayElements(Duration.ofSeconds(1));

        // First Streamer
        movieStream
                .subscribe(Util.subscriber("Sam"));

        // After some seconds
        Util.sleepSeconds(5);

        // Second Streamer
        movieStream
                .subscribe(Util.subscriber("Maria"));

        // Intended delay so we can watch the results
        Util.sleepSeconds(60);
    }

    private static Stream<String> getMovie() {
        System.out.println("Got the movie streaming...");
        return IntStream.rangeClosed(1, 100).mapToObj(s -> "Scene " + s);
    }
}
