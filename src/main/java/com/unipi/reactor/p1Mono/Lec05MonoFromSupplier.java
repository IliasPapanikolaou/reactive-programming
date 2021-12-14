package com.unipi.reactor.p1Mono;

import com.unipi.reactor.util.Util;
import reactor.core.publisher.Mono;

import java.util.concurrent.Callable;
import java.util.function.Supplier;

public class Lec05MonoFromSupplier {

    public static void main(String[] args) {

        /* The just() method executes immediately the method getName(),
         * that's why we should not use it.
         * Use just() only when you have the data ready, unless, if the process
         * of getting data is time-consuming, we block the publisher.
         * We should use Mono.fromSupplier()
         */
        Mono<String> mono = Mono.just(getName());

        /* Mono.fromSupplier() method is a java8 interface that does not accept
         * input parameters, but it is supposed to give an output
         */
        Mono<String> mono1 = Mono.fromSupplier(() -> getName());

        // getName() is invoked only if we subscribe
        mono1.subscribe(
                Util.onNext()
        );

        // Alternate method to use supplier
        Supplier<String> stringSupplier = () -> getName();
        Mono<String> mono2 = Mono.fromSupplier(stringSupplier);
        mono2.subscribe(
                s -> System.out.println("Received: " + s)
        );

        // Callable - Same as above using callable interface
        Callable<String> stringCallable = () -> getName();
        Mono.fromCallable(stringCallable)
                .subscribe(
                        Util.onNext()
                );
    }

    private static String getName() {
        System.out.println("Generating name... ");
        return Util.faker().name().firstName();
    }
}
