package com.unipi.reactor.p05ThreadingSchedulers;

import com.unipi.reactor.util.Util;
import reactor.core.publisher.Flux;
import reactor.core.scheduler.Schedulers;

import java.util.stream.IntStream;

/*
 * Schedulers        -> Usage:
 * -------------------------------------------------
 * boundedElastic    -> Network/time-consuming calls (4 core cpu -> 40 thread, x10 the cpu cores)
 * parallel          -> CPU intensive tasks (4 core cpu -> 4 thread, one core per cpu)
 * single            -> A single dedicated thread for one-off tasks
 * immediate         -> Current thread
 *
 * Operators for Scheduling -> Usage
 * -------------------------------------------------
 * subscribeOn       -> for upstream (subscriber to publisher orientation)
 * publishOn         -> for downstream (publisher to subscriber orientation)
 */

public class Lec02SubscribeOn {
    public static void main(String[] args) {

        System.out.println("Bounded Elastic Scheduler:");
        Flux<Object> flux = Flux.create(fluxSink -> {
                    printThreadName("Create");
                    fluxSink.next(1);
                    fluxSink.complete();
                })
                // If we have multiple schedulers, the closest to publisher will do the process
                // .subscribeOn(Schedulers.newParallel("Another Thread Pool"))
                .doOnNext(i -> printThreadName("Next " + i));

//        flux
//                // After SubscribeOn(), the process is executed in bounded elastic
//                .doFirst(() -> printThreadName("First2"))
//                .subscribeOn(Schedulers.boundedElastic())
//                // This executed by the main thead
//                .doFirst(() -> printThreadName("First1"))
//                .subscribe( v -> printThreadName("Sub " + v));

        // Example with runnable in two threads
        Runnable runnable = () -> flux
                // After SubscribeOn(), the processes are executed in bounded elastic
                .doFirst(() -> printThreadName("First_2"))
                .subscribeOn(Schedulers.boundedElastic())
                // Thread 0 and 1 will execute the process to this point.
                .doFirst(() -> printThreadName("First_1"))
                .subscribe( v -> printThreadName("Sub " + v));

        // Run two threads
        IntStream.rangeClosed(1,2).forEach(
                i -> new Thread(runnable).start()
        );

        Util.sleepSeconds(5);
    }

    public static void printThreadName(String msg) {
        System.out.println(msg + "\t\t: Thread: " + Thread.currentThread().getName());
    }
}
