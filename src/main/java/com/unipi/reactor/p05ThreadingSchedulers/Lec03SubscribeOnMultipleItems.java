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

public class Lec03SubscribeOnMultipleItems {

    public static void main(String[] args) {

        /*
         * Schedulers != Parallel-Execution inside the pipeline
         * - All the operations are always executed sequential
         * - Data is processed one by one on 1 thread in the TreadPool for a Subscriber
         * - Scheduler.parallel() - is a thread pool for CPU tasks. Doesn't mean parallel
         *   execution.
         */

        System.out.println("Bounded Elastic Scheduler:");
        Flux<Object> flux = Flux.create(fluxSink -> {
                    printThreadName("Create");
                    IntStream.rangeClosed(1, 4).forEach(i -> {
                                fluxSink.next(i);
                                // Intended blocking of the thread to emulate process time
                                Util.sleepSeconds(1);
                            }
                    );
                    fluxSink.complete();
                })
                // If we have multiple schedulers, the closest to publisher will do the process
                // .subscribeOn(Schedulers.newParallel("Another Thread Pool"))
                .doOnNext(i -> printThreadName("Next " + i));

        // A different thread in assigned to execute the pipeline process for every subscriber
        // but inside the pipeline, the process happens in this single thread.

        // Subscriber 1
        flux
                .subscribeOn(Schedulers.boundedElastic())
                .subscribe(v -> printThreadName("Sub " + v));

        // Subscriber 2
        flux
                .subscribeOn(Schedulers.boundedElastic())
                .subscribe(v -> printThreadName("Sub " + v));

        // Every subscriber can have its own scheduler with no confusion
        // Subscriber 3
        flux
                .subscribeOn(Schedulers.parallel())
                .subscribe(v -> printThreadName("Sub " + v));

        // Intended blocking in order to watch the results
        Util.sleepSeconds(5);
    }

    public static void printThreadName(String msg) {
        System.out.println(msg + "\t\t: Thread: " + Thread.currentThread().getName());
    }
}
