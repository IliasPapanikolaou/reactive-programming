package com.unipi.reactor.p5ThreadingSchedulers;

import com.unipi.reactor.util.Util;
import reactor.core.publisher.Flux;

import java.util.stream.IntStream;

public class Lec01ThreadDemo {

    public static void main(String[] args) {

        // In the example below, everything is executed by the main thread (default)
        System.out.println("Main Thread:");
        Flux<Object> flux = Flux.create(fluxSink -> {
                    printThreadName("Create");
                    fluxSink.next(1);
                    fluxSink.complete();
                })
                .doOnNext(i -> printThreadName("Next " + i));

        flux.subscribe( v -> printThreadName("Sub " + v));

        // Subscribers in two different threads
        System.out.println("Multithreading:");
        Runnable  runnable = () -> flux.subscribe(v -> printThreadName("Sub " + v));
        IntStream.rangeClosed(1,2).forEach(
                i -> new Thread(runnable).start()
        );

        Util.sleepSeconds(5);
    }

    public static void printThreadName(String msg) {
        System.out.println(msg + "\t\t: Thread: " + Thread.currentThread().getName());
    }
}
