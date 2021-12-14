package com.unipi.reactor.p9RepeatRetry;

import com.unipi.reactor.util.Util;
import reactor.core.publisher.Flux;

import java.util.concurrent.atomic.AtomicInteger;

/*
 * Repeat   ->  Resubscribe after complete signal
 * Retry    ->  Resubscribe after error signal
 */
public class Lec01Repeat {

    private static AtomicInteger atomicInteger = new AtomicInteger(1);

    public static void main(String[] args) {

        // Subscriber
        getIntegers()
                // Repeat another two times after onComplete() signal
                .repeat(2)
                // Repeat without argument, will always try to resubscribe to the source
                //.repeat() // Indefinitely but onError stops
                // With boolean supplier: Condition if i < 10 continue while true
                // .repeat(() -> atomicInteger.get() < 10)
                .subscribe(Util.subscriber());
    }

    // Publisher
    private static Flux<Integer> getIntegers() {
        return Flux.range(1, 3)
                .doOnSubscribe(s -> System.out.println("--Subscribed"))
                .doOnComplete(() -> System.out.println("--Completed"));
                // Depending on the implementation, we may get different results
                // .map( i -> atomicInteger.getAndIncrement());
                // Intended error
                // .map(i -> i / 0); // This will stop .repeat()
    }
}
