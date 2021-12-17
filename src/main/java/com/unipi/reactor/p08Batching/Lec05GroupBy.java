package com.unipi.reactor.p08Batching;

import com.unipi.reactor.util.Util;
import reactor.core.publisher.Flux;

import java.time.Duration;

public class Lec05GroupBy {

    public static void main(String[] args) {

        Flux.range(1, 30)
                .delayElements(Duration.ofSeconds(1))
                // Group by color (key: color)
                // .groupBy(b -> b.getColor())
                // Group by Odd - Even (key: 0, 1), Cardinality = 2
                .groupBy(i -> i % 2)
                // groupBy returns GroupedFlux
                .subscribe(gf -> process(gf, gf.key()));

        // The method will be called two times,
        // one time for every new group (cardinality: 2)
        Util.sleepSeconds(6);
    }

    private static void process(Flux<Integer> flux, int key) {
        System.out.println("Called process()");
        flux.subscribe(i -> System.out.println("Key: " + key + ", Item: " + i));
    }
}
