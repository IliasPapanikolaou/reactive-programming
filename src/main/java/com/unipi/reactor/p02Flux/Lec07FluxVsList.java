package com.unipi.reactor.p02Flux;

import com.unipi.reactor.util.Util;
import reactor.core.publisher.Flux;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

public class Lec07FluxVsList {

    public static void main(String[] args) {

        // List takes all the names together when they are ready.
        List<String> names = NameGenerator.getName(5);
        System.out.println(names);

        // Flux: When a name is ready, it emits it one by one.
        NameGenerator.getNameFlux(5)
                .subscribe(Util.onNext());
    }
}

class NameGenerator {

    // Return a List of 10 generated names.
    public static List<String> getName(int count) {
        List<String> list = new ArrayList<>(count); // set initial capacity of the ArrayList
        IntStream.rangeClosed(1, count).forEach(n -> list.add(getName()));
        return list;
    }

    // More efficient way to do this with Flux
    public  static Flux<String> getNameFlux(int count) {
        // Publisher
        return Flux.range(0, count)
                .map(i -> getName());
    }

    private static String getName() {
        System.out.println("Generating name... ");
        Util.sleepSeconds(1); // Simulate time consuming process
        return Util.faker().name().firstName();
    }
}
