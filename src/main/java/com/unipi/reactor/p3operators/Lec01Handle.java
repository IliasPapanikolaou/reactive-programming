package com.unipi.reactor.p3operators;

import com.unipi.reactor.util.Util;
import reactor.core.publisher.Flux;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.BiConsumer;

public class Lec01Handle {

    public static void main(String[] args) {

        // Handle = Filter + Map

        Flux.range(1, 20)
                // handle(BiConsumer<? super Integer, SynchronousSink<R>> handler) returns Flux<R>
                .handle((integer, synchronousSink) -> {
                    // Filter odd and even numbers
                    if (integer % 2 == 0)
                        synchronousSink.next(integer + " -> even");
                    else
                        synchronousSink.next(integer + " -> odd");
                    // Stop on 10
                    if (integer == 10)
                        synchronousSink.complete();
                }).subscribe(Util.subscriber());
    }
}

/*
 * BiConsumer is a Consumer Interface which takes two arguments.
 *
 * Consumer accepts one item with the method: void accept(T t);
 * BiConsumer accepts two items with the method: void accept(T t, U t);
 *
 **/

// Example with lambda expression
class BiConsumerDemo1 {

    public static void main(String[] args) {

        // Addition
        BiConsumer<Integer, Integer> addition = (a, b) ->
                System.out.println("Addition: " + a + " + " + b + " = " + (a + b));

        addition.accept(20, 40);

        // Subtraction
        BiConsumer<Integer, Integer> subtraction = (a, b) ->
                System.out.println("Subtraction: " + a + " - " + b + " = " + (a - b));

        subtraction.accept(50, 20);

        // Map - LinkedHashMap maintains insertion order of keys while HashMap does not
        Map<Integer, String> map = new LinkedHashMap<>();
        map.put(1, "Python");
        map.put(2, "Java");
        map.put(3, "JavaScript");
        map.put(4, "C#");

        // map.foreach accepts a BiConsumer (key, value) in lambda expression
        System.out.println("Most popular languages 2021");
        map.forEach((k, v) -> {
            System.out.println("#" + k + " -> " + v);
        });
    }
}

// Example by interface implementation
class BiConsumerDemo2 implements BiConsumer<Integer, Integer> {

    public static void main(String[] args) {

        // We use the BiConsumerDemo implementation which adds two numbers
        BiConsumer<Integer, Integer> biConsumer = new BiConsumerDemo2();
        biConsumer.accept(10, 20);

    }

    @Override
    public void accept(Integer a, Integer b) {
        System.out.println("Integer A + B: " + (a + b));
    }
}
