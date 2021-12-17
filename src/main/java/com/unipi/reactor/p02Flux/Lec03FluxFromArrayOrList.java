package com.unipi.reactor.p02Flux;

import com.unipi.reactor.util.Util;
import reactor.core.publisher.Flux;

import java.util.Arrays;
import java.util.List;

public class Lec03FluxFromArrayOrList {

    public static void main(String[] args) {

        // or Java 9
        List<Integer> integerList = List.of(1, 2, 3);
        // or <= Java 8
        List<String> stringList = Arrays.asList("a", "b", "c");

        // Publishers - fromIterable() is like just() but for lists
        Flux<String> stringFlux = Flux.fromIterable(stringList);

        // Subscriber - Consumer
        stringFlux.subscribe(
                Util.onNext(),
                Util.onError(),
                Util.onComplete()
        );

        // Arrays
        Integer[] arr = {2, 5, 6, 3, 7, 8, 1, 4, 9, 0};

        // Publisher - sort
        Flux<Integer> sortedFlux = Flux.fromArray(arr).sort();

        // Subscriber - Consumer
        sortedFlux.subscribe(
                Util.onNext(),
                Util.onError(),
                Util.onComplete()
        );

    }
}
