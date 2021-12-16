package com.unipi.reactor.p10Sink;

import com.unipi.reactor.util.Util;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Sinks;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.IntStream;

public class Lec03SinkThreadSafety {

    public static void main(String[] args) {

        // Unicast: Sink 1:N (flux) that can have one subscriber
        // Handle through which we push items
        Sinks.Many<Object> sink = Sinks.many().unicast().onBackpressureBuffer();

        // Handle through which the subscriber will receive items
        Flux<Object> flux = sink.asFlux();

        List<Object> list = new ArrayList<>();

        // Add new published items to list
        flux.subscribe(list::add);

        // Generate 1000 values in another thread
        IntStream.rangeClosed(1, 1000).forEach(i -> {
            final int j = i;
            CompletableFuture.runAsync(() -> {
               sink.tryEmitNext(j);
            });
        });

        // Sleep 3 seconds
        Util.sleepSeconds(3);

        // Print the size of the list
        // Every time we get different list size, it isn't thread safe
        System.out.println(list.size());
        // Clear list
        list.clear();

        // We must use it like this
        IntStream.rangeClosed(1, 1000).forEach(i -> {
            final int j = i;
            CompletableFuture.runAsync(() -> {
                // When emitting data, if face andy error, retry
                sink.emitNext(j, ((signalType, emitResult) -> true));
            });
        });

        // Sleep 3 seconds
        Util.sleepSeconds(3);

        // Print the size of the list
        // Every time we get different list size, it isn't thread safe
        System.out.println(list.size());

        // We can consider the second method as thread safe
    }
}
