package com.unipi.reactor.flux;

import com.unipi.reactor.util.Util;
import reactor.core.publisher.Flux;

public class Lec15FluxGenerate {

    /* Flux generate looks like Flux create
    * - With the FluxSink() we have one instance, and we have to maintain the loop
    * - With the SynchronousSink() we don't have to maintain any loop, generate()
    *   method takes care of it.
    *
    *
    * */


    public static void main(String[] args) {

        Flux.generate(synchronousSink -> {
            // With SynchronousSink you are allowed to emit only one item
            synchronousSink.next(Util.faker().country().name());
            synchronousSink.next(Util.faker().country().name()); // This won't work
        }).subscribe(Util.subscriber());


        // Generate() and Take() - If we want only 3 items
        Flux.generate(synchronousSink -> {
            // With SynchronousSink you are allowed to emit only one item
            System.out.println("Emitting");
            synchronousSink.next(Util.faker().country().name());
            // synchronousSink.next(Util.faker().country().name()); // This won't work
        })
                .take(3) // Get only 3 items
                .subscribe(Util.subscriber());


        // Complete() stops the loop (onComplete)
        Flux.generate(synchronousSink -> {
                    // With SynchronousSink you are allowed to emit only one item
                    System.out.println("Emitting");
                    synchronousSink.next(Util.faker().country().name());
                    synchronousSink.complete(); // Complete stops the loop
                })
                .take(3) // This doesn't matter because complete() was used
                .subscribe(Util.subscriber());


        // Error() (onError)
        Flux.generate(synchronousSink -> {
                    // With SynchronousSink you are allowed to emit only one item
                    System.out.println("Emitting");
                    synchronousSink.next(Util.faker().country().name());
                    synchronousSink.error(new RuntimeException("This is an Error!")); // Stops the loop
                })
                .take(3) // This doesn't matter because complete() was used
                .subscribe(Util.subscriber());
    }
}
