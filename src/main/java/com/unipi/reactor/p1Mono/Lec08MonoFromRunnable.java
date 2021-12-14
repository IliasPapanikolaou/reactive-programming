package com.unipi.reactor.p1Mono;

import com.unipi.reactor.util.Util;
import reactor.core.publisher.Mono;

public class Lec08MonoFromRunnable {

    public static void main(String[] args) {

        // We use it when we want to be notified when a method is completed
        Mono.fromRunnable(timeConsumingProcess())
                .subscribe(
                        Util.onNext(),
                        Util.onError(),
                        // onComplete
                        () -> {
                            System.out.println("The process is done. Sending emails...");
                        }
                );
    }

    private static Runnable timeConsumingProcess() {
        return () -> {
            Util.sleepSeconds(5);
            System.out.println("Thread: " + Thread.currentThread().getName()
                    + " -> Process is completed");
        };
    }
}
