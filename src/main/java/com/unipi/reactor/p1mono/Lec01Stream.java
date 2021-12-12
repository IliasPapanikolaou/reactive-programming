package com.unipi.reactor.p1mono;

import java.time.Duration;
import java.util.stream.Stream;

public class Lec01Stream {

    public static void main(String[] args) {
        // Stream is 'lazy', it won't do anything unless we call a terminal operator
        Stream<Integer> stream = Stream.of(1)
                .map(i -> {
                    try {
                        Thread.sleep(Duration.ofSeconds(1).toMillis());
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    return i * 2;
                });
        stream.forEach(System.out::println);
    }
}
