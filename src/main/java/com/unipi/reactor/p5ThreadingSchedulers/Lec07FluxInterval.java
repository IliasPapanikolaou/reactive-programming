package com.unipi.reactor.p5ThreadingSchedulers;

import com.unipi.reactor.util.Util;
import reactor.core.publisher.Flux;

import java.time.Duration;

public class Lec07FluxInterval {

    public static void main(String[] args) {

        // Publisher
        Flux.interval(Duration.ofSeconds(1))
                .doOnNext(s -> System.out.println("Next: " + s + " Thread: "
                        + Thread.currentThread().getName()))
                // Subscriber
                .subscribe(Util.subscriber());

        /*
        * This task will end immediately if we don't use thread sleep at the end
        * because Flux<T> uses parallel() internal and works asynchronously.
        */
        Util.sleepSeconds(6);
    }
}
