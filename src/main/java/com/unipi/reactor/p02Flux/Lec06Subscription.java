package com.unipi.reactor.p02Flux;

import com.unipi.reactor.util.Util;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;
import reactor.core.publisher.Flux;

import java.util.concurrent.atomic.AtomicReference;

/*
* Custom Implementation of Subscribe -> subscribeWith()
* */

public class Lec06Subscription {

    public static void main(String[] args) {

        // Thread safe reference - It holds the subscription
        AtomicReference<Subscription> atomicReference = new AtomicReference<>();

        // Publisher in range
        Flux.range(1, 20)
                .log()
                // Subscriber
                .subscribeWith(new Subscriber<Integer>() {
                    @Override
                    public void onSubscribe(Subscription subscription) {
                        // Subscriber has to request the date from publisher
                        System.out.println("Received Sub: " + subscription);
                        atomicReference.set(subscription);
                    }

                    @Override
                    public void onNext(Integer integer) {
                        System.out.println("onNext: " + integer);
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        System.out.println("onError: " + throwable);
                    }

                    @Override
                    public void onComplete() {
                        System.out.println("onComplete");
                    }
                });

        Util.sleepSeconds(3);
        atomicReference.get().request(3); // request 3 items
        Util.sleepSeconds(5);
        atomicReference.get().request(3); // request another 3 items
        Util.sleepSeconds(5);
        System.out.println("Cancelling Subscription...");
        atomicReference.get().cancel(); // cancel subscription
        Util.sleepSeconds(3);
        // request items again - this won't work because we cancelled
        // the subscription
        atomicReference.get().request(4);
        Util.sleepSeconds(3);

    }
}
