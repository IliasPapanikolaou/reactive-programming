package com.unipi.reactor.p4hotcoldpublishers;

import com.unipi.reactor.util.Util;
import reactor.core.publisher.Flux;

import java.time.Duration;
import java.util.stream.IntStream;
import java.util.stream.Stream;

/*
 * Hot publishers start emitting data in a specific moment and subscribers can
 * receive data from the moment they subscribed, but they lose the previous data emitted from the publisher.
 *
 * If the streaming is completed and someone joins the stream, the stream start from the beginning
 * like a cold publish
 *
 * It is like a TV program, or a Cinema Movie, if we are late, we lose the beginning of the movie.
 */
public class Lec03HotPublisher {

    public static void main(String[] args) {
        // Publisher
        Flux<String> movieStream = Flux.fromStream(() -> getMovie())
                .delayElements(Duration.ofSeconds(1))
                // ConnectableFlux<T>: Multiple users can connect to the flux.
                .publish()
                // minSubscribers to start emitting data
                .refCount(1);

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


