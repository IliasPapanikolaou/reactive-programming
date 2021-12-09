package com.unipi.reactor.flux;

import com.unipi.reactor.util.Util;
import reactor.core.publisher.Flux;
import java.util.stream.IntStream;

/*
* Push and Create are about the same:
*
* Create is thread-safe, we can use it with multiple threads.
* Push is NOT thread-safe, we can use it in a single thread producer.
*
*/

public class Lec18FluxPush {

    public static void main(String[] args) {

        // Refactoring the above - Best practice
        NameProducer nameProducer = new NameProducer();

        // Publisher
        Flux.push(nameProducer)
                // Subscriber
                .subscribe(Util.subscriber());

        // Invoke producer to produce data
        nameProducer.produce();

        // Different threads - FlexSink() is a thread safe method.
        // Runnable runnable = () -> nameProducer.produce();
        // or
        Runnable runnable = nameProducer::produce;

        // Fire 10 threads
        IntStream.rangeClosed(1, 10).forEach(i ->
                new Thread(runnable).start()
        );

        // Intended delay
        Util.sleepSeconds(2);
    }
}
