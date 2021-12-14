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

public class Lec05PublishOnSubscribeOn {

    public static void main(String[] args) {

        /*
         * Schedulers != Parallel-Execution inside the pipeline
         * - All the operations are always executed sequential
         * - Data is processed one by one on 1 thread in the TreadPool for a Subscriber
         * - Scheduler.parallel() - is a thread pool for CPU tasks. Doesn't mean parallel
         *   execution.
         */

        // Publisher
        Flux<Object> flux = Flux.create(fluxSink -> {
                    printThreadName("Create");
                    IntStream.rangeClosed(1, 2).forEach(i -> {
                                fluxSink.next(i);
                            }
                    );
                    fluxSink.complete();
                })
                .doOnNext(i -> printThreadName("Next_1 " + i));

        // Subscriber
        flux
                .publishOn(Schedulers.parallel())
                .doOnNext(i -> printThreadName("Next_2 " + i))
                /*
                 * Publisher will execute process to boundedElastic Thread-Pool
                 * until it reached publishOn(), from there and downstream,
                 * everything will be executed to parallel Thread-Pool.
                 */
                .subscribeOn(Schedulers.boundedElastic())
                .subscribe(v -> printThreadName("Sub " + v));

        // Intended blocking in order to watch the results
        Util.sleepSeconds(5);
    }

    public static void printThreadName(String msg) {
        System.out.println(msg + "\t\t: Thread: " + Thread.currentThread().getName());
    }
}
