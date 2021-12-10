package com.unipi.reactor.hotcoldpublishers;

import com.unipi.reactor.util.Util;
import reactor.core.publisher.Flux;

import java.time.Duration;
import java.util.stream.IntStream;
import java.util.stream.Stream;

/*
 * With cash() stores internally the past data and when someone new subscriber joins,
 * it emits to the subscriber all the past data AT ONCE and continues the streaming from
 * the current point.
 *
 * We can specify the number of the past items to be cashed.
 */
public class Lec05HotPublishCache {

    public static void main(String[] args) {

        // Publisher
        Flux<String> movieStream = Flux.fromStream(() -> getMovie())
                .delayElements(Duration.ofSeconds(1))
                .cache();
                // .cache(2); // Specify the number of items to be cashed (2: last two items)

        // First Streamer
        // After some seconds
        Util.sleepSeconds(5);
        System.out.println("Sam is about to join");
        movieStream
                .subscribe(Util.subscriber("Sam"));

        // After some seconds
        Util.sleepSeconds(5);
        System.out.println("Maria is about to join");
        // Second Streamer
        movieStream
                .subscribe(Util.subscriber("Maria"));

        // Intended delay so we can watch the results
        Util.sleepSeconds(60);
    }

    private static Stream<String> getMovie() {
        System.out.println("Got the movie streaming...");
        return IntStream.rangeClosed(1, 10).mapToObj(s -> "Scene " + s);
    }
}
