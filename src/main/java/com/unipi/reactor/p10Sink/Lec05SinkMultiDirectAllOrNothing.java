package com.unipi.reactor.p10Sink;

import com.unipi.reactor.util.Util;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Sinks;
import reactor.util.concurrent.Queues;

import java.time.Duration;
import java.util.stream.IntStream;

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
public class Lec05SinkMultiDirectAllOrNothing {

    public static void main(String[] args) {

        // Queues
        System.setProperty("reactor.bufferSize.small", "16");

        // Multicast: Sink 1:N (flux) that can have many subscriber
        // Handle through which we push items
        // directAllOrNothing() means that subscribers will receive only new messages()
        // Both subscribers will receive up to 32 values, because 2nd subscriber slows all the subscribers
        // Sinks.Many<Object> sink = Sinks.many().multicast().directAllOrNothing();
        // We can use directBestEffort()
        Sinks.Many<Object> sink = Sinks.many().multicast().directBestEffort();

        // Handle through which the subscriber will receive items
        Flux<Object> flux = sink.asFlux();

        // Subscribers 1
        flux.subscribe(Util.subscriber("Sam"));
        // Subscribers 2 - Too slow to process the messages
        flux.delayElements(Duration.ofMillis(200)).subscribe(Util.subscriber("Mike"));

        // Emit 100 values
        IntStream.rangeClosed(1, 100).forEach(sink::tryEmitNext); // or sink.tryEmitNext(i);

        Util.sleepSeconds(10);

    }
}
