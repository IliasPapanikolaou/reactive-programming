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
public class Lec02SinkUnicast {

    public static void main(String[] args) {

        // Unicast: Sink 1:N (flux) that can have one subscriber
        // Handle through which we push items
        Sinks.Many<Object> sink = Sinks.many().unicast().onBackpressureBuffer();

        // Handle through which the subscriber will receive items
        Flux<Object> flux = sink.asFlux();

        // Subscribers
        flux.subscribe(Util.subscriber("Sam"));
        // This subscriber will not be able to subscribe because we use "unicast"
        flux.subscribe(Util.subscriber("Mike"));

        // Create items to be published
        sink.tryEmitNext("Hi");
        sink.tryEmitNext("How are you");
        sink.tryEmitNext("?");
        // Explicitly send complete signal
        sink.tryEmitComplete();
    }
}
