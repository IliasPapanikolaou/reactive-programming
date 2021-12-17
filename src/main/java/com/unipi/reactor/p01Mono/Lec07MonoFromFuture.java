package com.unipi.reactor.p01Mono;

import com.unipi.reactor.util.Util;
import reactor.core.publisher.Mono;

import java.util.concurrent.CompletableFuture;

public class Lec07MonoFromFuture {

    public static void main(String[] args) {

        Mono.fromFuture(getName())
                .subscribe(Util.onNext());

        // Intended delay on main thread, so we can get the result from the other thread.
        Util.sleepSeconds(1);
    }

    // CompletableFuture is feature that introduced as part of java 8
    // It is the equivalent of JavaScript Promise
    private static CompletableFuture<String> getName() {
        return CompletableFuture.supplyAsync(() -> {
            System.out.println("Thread: " + Thread.currentThread().getName());
            return Util.faker().name().fullName();
        });
    }
 }
