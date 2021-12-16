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
public class Lec04SinkMulticast {

    public static void main(String[] args) {

        // Multicast: Sink 1:N (flux) that can have many subscriber
        // Handle through which we push items
        // onBackpressureBuffer() means that whoever subscribers, will see the new messages
        // and the first one will receive the buffered messages as well
        Sinks.Many<Object> sink = Sinks.many().multicast().onBackpressureBuffer();
        // directAllOrNothing() means that subscribes will receive only new messages()
        // Sinks.Many<Object> sink = Sinks.many().multicast().directAllOrNothing();

        // Handle through which the subscriber will receive items
        Flux<Object> flux = sink.asFlux();

        // 1st Message
        sink.tryEmitNext("Hi");

        // Subscribers 1
        flux.subscribe(Util.subscriber("Sam"));
        // Subscribers 2
        flux.subscribe(Util.subscriber("Mike"));

        // 2nd Message
        sink.tryEmitNext("How are you");

        // Subscribers 3 - John will not receive 1st and 2nd messages, he subscribed in a late point.
        flux.subscribe(Util.subscriber("John"));

        // 3rd Message
        sink.tryEmitNext("?");

        // Explicitly send complete signal
        sink.tryEmitComplete();
    }
}
