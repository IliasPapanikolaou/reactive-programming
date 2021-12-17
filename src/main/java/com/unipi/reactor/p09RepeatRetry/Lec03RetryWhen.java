package com.unipi.reactor.p09RepeatRetry;

import com.unipi.reactor.util.Util;
import reactor.core.publisher.Flux;
import reactor.util.retry.Retry;

import java.time.Duration;

/*
 * Repeat   ->  Resubscribe after complete signal
 * Retry    ->  Resubscribe after error signal
 */
public class Lec03RetryWhen {

    public static void main(String[] args) {

        // Subscriber
        getIntegers()
                // Retry with duration another two times after onError() signal
                .retryWhen(Retry.fixedDelay(2, Duration.ofSeconds(3)))
                .subscribe(Util.subscriber());

        Util.sleepSeconds(10);
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
