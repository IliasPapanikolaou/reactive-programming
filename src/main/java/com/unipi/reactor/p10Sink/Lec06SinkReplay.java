package com.unipi.reactor.p10Sink;

import com.unipi.reactor.util.Util;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Sinks;

/*
 * Sinks
 *
 * Type             Behavior        Pub:Sub
 * -----------------------------------------
 * one              Mono            1:N
 * many-unicast     Flux            1:1
 * many-multicast   Flux            1:N
 * many-replay      Flux            1:N
 * (with replay of all values to later subscribers)
 */
public class Lec06SinkReplay {

    public static void main(String[] args) {

        // replay.all(): Sink 1:N (flux) that can have many subscriber and caches previous items
        // Every subscriber will receive all messages when subscribes
        // Handle through which we push items
        Sinks.Many<Object> sink = Sinks.many().replay().all();

        // Handle through which the subscriber will receive items
        Flux<Object> flux = sink.asFlux();

        // Everyone will receive past messages
        // 1st Message
        sink.tryEmitNext("Hi");

        // Subscribers 1
        flux.subscribe(Util.subscriber("Sam"));
        // Subscribers 2
        flux.subscribe(Util.subscriber("Mike"));

        // 2nd Message
        sink.tryEmitNext("How are you");

        // Subscribers 3
        flux.subscribe(Util.subscriber("John"));

        // 3rd Message
        sink.tryEmitNext("?");

        // Explicitly send complete signal
        sink.tryEmitComplete();
    }
}
