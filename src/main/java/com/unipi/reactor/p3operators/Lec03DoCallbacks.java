package com.unipi.reactor.p3operators;

import com.unipi.reactor.util.Util;
import reactor.core.publisher.Flux;

import java.util.stream.IntStream;

/*
 * Pipeline lifecycle hooks
 *
 * Important! Pipeline is like Last In - First Out (LIFO) que:
 * When we subscribe to  a Publisher, the functions are executed from the bottom up:
 * Check the below example .doFirst() hook
 * Except .doOnSubscribe() which passes from publisher to subscriber
 *
 * LifeCycle order:
 * ----------------
 * 1. doFirst()
 * 2. doOnSubscribe()
 * 3. doOnRequest() -> The .create() is invoked
 * 4. doDoNext()
 * 5. doOnComplete()
 * 6. doOnTerminate()
 * 7. doFinally() -> Like try/catch/finally
 *
 * onError
 * ----------------
 * If we got an error (onComplete doesn't invoked)
 * doOnError() -> Error Message
 * doOnTerminate()
 * doFinally()
 *
 * .take()
 * ----------------
 * After to items, doOnCancel is invoked
 * doOnCancel()
 * doFinally()
 * doOnDiscard() // the rest items are discarded
 *
 * doFinally() is the best choice if we want to do
 * something after the pipeline is emitted, because
 * it always is invoked
 *
 */

public class Lec03DoCallbacks {

    public static void main(String[] args) {

        Flux.create(fluxSink -> {
                    System.out.println("Inside create");
                    IntStream.rangeClosed(1, 5).forEach(i -> {
                        fluxSink.next(i);
                    });
                    // fluxSink.error(new RuntimeException("Intended Error!"));
                    fluxSink.complete();
                    System.out.println("Completed");
                })
                .doOnComplete(() -> System.out.println("doOnComplete")) // doOnComplete callback
                .doFirst(() -> System.out.println("doFirst 1")) // doFirst callback
                .doOnNext(o -> System.out.println("doOnNext: " + o)) // doOnNext callback
                .doOnSubscribe(s -> System.out.println("doOnSubscribe 1: " + s)) // doOnSubscribe callback
                .doOnRequest(l -> System.out.println("doOnRequest: " + l)) // doOnRequest callback
                .doFirst(() -> System.out.println("doFirst 2"))
                .doOnError(err -> System.out.println("doOnError: " + err.getMessage())) // doOnError callback
                .doOnTerminate(() -> System.out.println("doOnTerminate")) // doOnTerminate callback
                .doOnCancel(() -> System.out.println("doOnCancel")) // doOnCancel callback
                .doOnSubscribe(s -> System.out.println("doOnSubscribe 2: " + s)) // doOnSubscribe callback
                .doFinally(signal -> System.out.println("doFinally 1: " + signal)) // doFinally
                .doFirst(() -> System.out.println("doFirst 3"))
                .doOnDiscard(Object.class, o -> System.out.println("doOnDiscard: " + o)) // doOnDiscard
                // .take(2) // Take only 2 items
                // .doFinally(signal -> System.out.println("doFinally 2: " + signal)) // doFinally
                .subscribe(Util.subscriber());
    }
}
