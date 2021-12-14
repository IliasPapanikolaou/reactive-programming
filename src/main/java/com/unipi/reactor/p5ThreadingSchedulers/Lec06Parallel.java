package com.unipi.reactor.p5ThreadingSchedulers;

import com.unipi.reactor.util.Util;
import reactor.core.publisher.Flux;
import reactor.core.scheduler.Schedulers;

import java.util.ArrayList;
import java.util.List;

/*
 * Use of parallel processing inside a pipeline
 */

public class Lec06Parallel {

    public static void main(String[] args) {

        // Simple example:
        // Publisher
//        Flux.range(1, 2)
//                // Optionally can pass number of thread as argument
//                .parallel() // ParallelFlux<T>
//                .runOn(Schedulers.parallel())
//                .doOnNext(i -> printThreadName("Next " + i))
//                // Subscriber
//                .subscribe(v -> printThreadName("Sub " + v));

        // Second Example:
        // Warning! ArrayList is not thread safe, result will vary.
        List<Integer> list = new ArrayList<>();

        Flux.range(1, 1000)
                // Optionally can pass number of thread as argument
                .parallel(4) // ParallelFlux<T>
                .runOn(Schedulers.parallel())
                // .doOnNext(i -> printThreadName("Next " + i))
                // Subscriber
                .subscribe(v -> list.add(v));

        // Third Example:
        Flux.range(1, 10)
                // Optionally can pass number of thread as argument
                // Parallel doesn't have publishOn and SubscribeOn
                .parallel(10) // ParallelFlux<T>
                .runOn(Schedulers.boundedElastic())
                .doOnNext(i -> printThreadName("Next " + i))
                // Sequential will make publisher bring back Flux instead of ParallelFlux
                // We can now use publishOn and SubscribeOn.
                .sequential()
                // Subscriber
                .subscribe(v -> printThreadName("Sub " + v));



        Util.sleepSeconds(5);
        System.out.println("Second example list size: " + list.size());
    }

    public static void printThreadName(String msg) {
        System.out.println(msg + "\t\t: Thread: " + Thread.currentThread().getName());
    }
}
