package com.unipi.reactor.p9RepeatRetry;

import com.unipi.reactor.util.Util;
import reactor.core.publisher.Flux;

import java.util.concurrent.atomic.AtomicInteger;

/*
 * Repeat   ->  Resubscribe after complete signal
 * Retry    ->  Resubscribe after error signal
 */
public class Lec02Retry {

    public static void main(String[] args) {

        // Subscriber
        getIntegers()
                // Retry another two times after onError() signal
                .retry(2)
                .subscribe(Util.subscriber());
    }

    // Publisher
    private static Flux<Integer> getIntegers() {
        return Flux.range(1, 3)
                .doOnSubscribe(s -> System.out.println("--Subscribed"))
                .doOnComplete(() -> System.out.println("--Completed"))
                // Intended random error
                .map(i -> i / (Util.faker().random().nextInt(1, 5) > 3 ? 0 : 1))
                .doOnError(err -> System.out.println("--Error"));
    }
}
