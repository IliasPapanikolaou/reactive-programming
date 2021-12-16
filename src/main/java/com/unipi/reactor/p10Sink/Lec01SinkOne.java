package com.unipi.reactor.p10Sink;

import com.unipi.reactor.util.Util;
import reactor.core.publisher.Mono;
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
public class Lec01SinkOne {

    public static void main(String[] args) {

        // Sinks.one() is a Mono Publisher, emits 0 or 1 value / empty / error
        // "Here we can publish a value"
        Sinks.One<Object> sink = Sinks.one();
        // Try to emit the value, like emitValue with failure handler
        // sink.tryEmitValue();
        // Like emitValue() with failure handler
        // sink.tryEmitEmpty(); // If we don't have something to provide
        // Like emitError() but with failure handler
        // sink.tryEmitError();

        // "Here we can subscribe to the publisher"
        Mono<Object> mono = sink.asMono();// Becomes a Mono publisher

        // Subscriber 1: Sam
        // "Here is the subscription to the publisher"
        mono.subscribe(Util.subscriber("Sam"));
        // Subscriber 2: Mike
        mono.subscribe(Util.subscriber("Mike"));

        // "Publish a value"
        // Whoever has subscribed to the publisher, will get the value
        sink.tryEmitValue("hi");

        // Emit error
        // sink.tryEmitValue(new RuntimeException("err"));

        // Mono is supposed to emit one value, with the second one, will throw an error
        // Disable the above sink if you don't want to get an error
        // Emit with failure handler
        sink.emitValue("hi", ((signalType, emitResult) -> {
            System.out.println(signalType.name());
            System.out.println(emitResult.name());
            return false; // retry false -> infinite loop
        }));

        // Mono is supposed to emit one value, with the second one, will throw an error
        // Emit with failure handler
        sink.emitValue("hello", ((signalType, emitResult) -> {
            System.out.println(signalType.name());
            System.out.println(emitResult.name());
            return false; // retry true -> infinite loop
        }));
    }
}
