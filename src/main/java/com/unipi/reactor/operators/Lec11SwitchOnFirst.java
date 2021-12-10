package com.unipi.reactor.operators;

import com.unipi.reactor.util.Util;
import reactor.core.publisher.Flux;

import java.util.function.Function;

/*
* Inside the pipeline:
* If the FIRST item satisfies the condition, pass to then next operator for ALL the items.
* If not, apply an external filter/function for ALL the items.
*/

public class Lec11SwitchOnFirst {

    public static void main(String[] args) {

        getPerson()
                .switchOnFirst((signal, personFlux) ->
                        // signal: onNext() or OnError or onComplete()
                        signal.isOnNext() && signal.get().getAge() > 18 ? // Checks only the 'first' item
                                personFlux : applyFilterMap().apply(personFlux))
                .subscribe(Util.subscriber());

    }

    // Publisher method of a Person
    public static Flux<Person> getPerson()  {
        return Flux.range(1, 10)
                .map(i -> new Person());
    }

    /*
     * Java 8, Function is a functional interface;
     * it takes an argument (object of type T) and returns an object (object of type R).
     * The argument and output can be a different type.
     *
     * Example:
     * Function<String, Integer> func = x -> x.length();
     * Integer apply = func.apply("test");   // output: 4
     * System.out.println(apply);
     */
    public static Function<Flux<Person>, Flux<Person>> applyFilterMap() {
        return flux -> flux
                .filter(p -> p.getAge() > 18)
                .doOnNext(p -> p.setName(p.getName().toUpperCase()))
                .doOnDiscard(Person.class, p -> System.out.println("Not allowed: " + p));
    }
}
