package com.unipi.reactor.p5ThreadingSchedulers;

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

public class Lec04PublishOn {

    public static void main(String[] args) {

        /*
         * Schedulers != Parallel-Execution inside the pipeline
         * - All the operations are always executed sequential
         * - Data is processed one by one on 1 thread in the TreadPool for a Subscriber
         * - Scheduler.parallel() - is a thread pool for CPU tasks. Doesn't mean parallel
         *   execution.
         */

        // In the example below, everything is executed by the main thread (default)
        // Publisher
        Flux<Object> flux = Flux.create(fluxSink -> {
                    printThreadName("Create");
                    IntStream.rangeClosed(1, 4).forEach(i -> {
                                fluxSink.next(i);
                            }
                    );
                    fluxSink.complete();
                })
                // If we have multiple schedulers, the closest to publisher will do the process
                // .subscribeOn(Schedulers.newParallel("Another Thread Pool"))
                .doOnNext(i -> printThreadName("Next_1 " + i));

        // A different thread in assigned to execute the pipeline process for every subscriber
        // but inside the pipeline, the process happens in this single thread.

        // Subscriber
        flux
                // everything beyond this point will be executed in boundedElastic Thread-Pool (downstream)
                .publishOn(Schedulers.boundedElastic())
                .doOnNext(i -> printThreadName("Next_2 " + i)) // Network intensive task (use bounded elastic)
                .publishOn(Schedulers.parallel())
                .subscribe(v -> printThreadName("Sub " + v)); // Cpu intensive task (use parallel)

        // Intended blocking in order to watch the results
        Util.sleepSeconds(5);
    }

    public static void printThreadName(String msg) {
        System.out.println(msg + "\t\t: Thread: " + Thread.currentThread().getName());
    }
}
