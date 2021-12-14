package com.unipi.reactor.p2Flux;

import com.unipi.reactor.util.Util;
import reactor.core.publisher.Flux;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

public class Lec04FluxFromStream {

    public static void main(String[] args) {

        // Java 9
        List<Integer> list = List.of(1, 2, 3, 4, 5);
        // or <= Java 8
        List<Integer> integerList = Arrays.asList(1, 2, 3, 4, 5);

        // Stream - Streams are one time use - if consumed, they can't reused.
        Stream<Integer> stream = list.stream();
        stream.forEach(System.out::println); // after this, the stream is closed.

        // Flux from stream
        Stream<Integer> stream1 = integerList.stream();
        // Publisher
        Flux<Integer> integerFlux = Flux.fromStream(stream1);

        // Subscriber
        integerFlux.subscribe(
                Util.onNext(),
                Util.onError(),
                Util.onComplete()
        );

        // This subscriber won't receive data because they are consumed by the previous one.
        integerFlux.subscribe(
                Util.onNext(),
                Util.onError(), // The onError will be invoked
                Util.onComplete()
        );

        // In order to prevent the data, best practice is to
        // create a list from stream as part of the Supplier.
        // Publisher - return a list from stream
        Flux<Integer> integerFlux1 = Flux.fromStream(() -> list.stream());

        // Subscriber 1
        integerFlux1.subscribe(
                Util.onNext(),
                Util.onError(),
                Util.onComplete()
        );

        // Subscriber 2 - Now will get the data
        integerFlux1.subscribe(
                Util.onNext(),
                Util.onError(),
                Util.onComplete()
        );
    }
}
