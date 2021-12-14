package com.unipi.reactor.p9RepeatRetry;

import com.unipi.reactor.util.Util;
import reactor.core.publisher.Mono;
import reactor.util.retry.Retry;

import java.time.Duration;

/*
 * Repeat   ->  Resubscribe after complete signal
 * Retry    ->  Resubscribe after error signal
 */
public class Lec04RetryWhenAdvanced {

    // We don't want to .retry() on Error 404
    // but we want to .retry() on Error 500

    public static void main(String[] args) {
        orderService(Util.faker().business().creditCardNumber())
                .doOnError(err -> System.out.println(err.getMessage()))
                .retryWhen(Retry.from(
                        flux -> flux
                                .doOnNext(retrySignal -> {
                                    System.out.println(retrySignal.totalRetries());
                                    System.out.println(retrySignal.failure());
                                })
                                // handle() is kind of filter() + map()
                                .handle((retrySignal, synchronousSink) -> {
                                    if (retrySignal.failure()
                                            .getMessage().equals("Error 500 - Internal Server Error"))
                                        synchronousSink.next(1);
                                    else
                                        // if error 404 pass error to the downstream
                                        synchronousSink.error(retrySignal.failure());
                                })
                                // Intended delay for demo purpose
                                .delayElements(Duration.ofSeconds(1))
                ))
                .subscribe(Util.subscriber());

        Util.sleepSeconds(20);
    }

    // Order service
    private static Mono<String> orderService(String ccNumber) {
        return Mono.fromSupplier(() -> {
            // This crappy service throws 90% an error
            processPayment(ccNumber);
            return Util.faker().idNumber().valid();
        });
    }

    // Payment service
    private static void processPayment(String ccNumber) {
        int random = Util.faker().random().nextInt(1, 10);
        if (random < 8)
            throw new RuntimeException("Error 500 - Internal Server Error");
        else if (random < 10)
            throw new RuntimeException("Error 404 - Not Found - Bad Request");
        // if 10 then Success 200
    }
}
