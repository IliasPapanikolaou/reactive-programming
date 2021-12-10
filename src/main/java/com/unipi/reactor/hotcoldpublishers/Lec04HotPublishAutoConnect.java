package com.unipi.reactor.hotcoldpublishers;

import com.unipi.reactor.util.Util;
import reactor.core.publisher.Flux;

import java.time.Duration;
import java.util.stream.IntStream;
import java.util.stream.Stream;

/*
* RefCount vs AutoConnect
*
* With AutoConnect and minSubscribers = 0, the stream starts without
* subscriber. Once it is completed, it does not re-emit for new subscribers.
*/

public class Lec04HotPublishAutoConnect {

    public static void main(String[] args) {

        // Publisher
        Flux<String> movieStream = Flux.fromStream(() -> getMovie())
                .delayElements(Duration.ofSeconds(1))
                .publish()
                // Instead of refCount, we can use .autoConnect()
                .autoConnect(0); // minSubscribers = 0

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
        return IntStream.rangeClosed(1, 8).mapToObj(s -> "Scene " + s);
    }
}
