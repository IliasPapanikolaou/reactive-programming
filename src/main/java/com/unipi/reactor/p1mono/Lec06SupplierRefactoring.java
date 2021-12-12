package com.unipi.reactor.p1mono;

import com.unipi.reactor.util.Util;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

public class Lec06SupplierRefactoring {

    public static void main(String[] args) {

        // These won't take time
        getName();
        getName();
        getName();

        // This one will take time
        getName()
                .subscribe(Util.onNext());

        // Block method
        // If we don't want to provide a consumer, and we want to get the result of the method
        // we use the blocking function - block()
        // Useful only for Testing - Do not use in production
        String name = getName()
                .subscribeOn(Schedulers.boundedElastic())
                .block(); // This is a blocking subscriber - consumer
        System.out.println("Received: " + name);

        // To make the process completely asynchronous,
        // because Reactor runs in main thread, we have to do the following:
        getName()
                .subscribeOn(Schedulers.boundedElastic()) // Assigns process to another thread
                .subscribe(Util.onNext());

        // the above is executed asynchronously and the main thread is exiting.
        // We intended sleep fo some seconds in order to see the response from the other thread
        Util.sleepSeconds(4);
    }

    // Method acts like Publisher
    /*
    * When building the below pipeline, it doesn't take time
    * When executing the pipeline, it takes time
    */
    private static Mono<String> getName() {
        System.out.println("Entered into getName() method");
        return Mono.fromSupplier(() -> {
            System.out.println("Thread: " + Thread.currentThread().getName()
                    + " -> Generating name... ");
            // Intended delay
            Util.sleepSeconds(3);
            // Return names from faker
            return Util.faker().name().firstName();
        }).map(String::toUpperCase);
    }
}
